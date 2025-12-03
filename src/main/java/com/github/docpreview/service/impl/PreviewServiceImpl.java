package com.github.docpreview.service.impl;

import com.github.docpreview.config.ConfigUtils;
import com.github.docpreview.service.PreviewService;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
public class PreviewServiceImpl implements PreviewService {

    private static final long MAX_FILE_SIZE = 15 * 1024 * 1024L;
    private static final Logger log = LoggerFactory.getLogger(PreviewServiceImpl.class);

    private final RestTemplate restTemplate;

    public PreviewServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public int convertPdfToImg(String url) {
        String md5 = DigestUtils.md5DigestAsHex(url.getBytes(StandardCharsets.UTF_8));
        String folderPath = ConfigUtils.getFilePath() + md5;
        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            String[] files = folder.list();
            if (files != null) {
                return files.length;
            }
        }
        if (!folder.mkdirs()) {
            throw new RuntimeException("创建文件夹失败");
        }
        File tempFile = null;
        try {
            tempFile = File.createTempFile(md5, ".pdf");
            downloadFile(url, tempFile);
            PDDocument document = Loader.loadPDF(tempFile);
            int pageCount = document.getNumberOfPages();
            PDFRenderer renderer = new PDFRenderer(document);
            for (int i = 0; i < pageCount; i++) {
                BufferedImage image = renderer.renderImageWithDPI(i, 160);
                ImageIO.write(image, "JPG", new File(folderPath + File.separator + i + ".jpg"));
            }
            return pageCount;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (tempFile != null && tempFile.exists()) {
                if (!tempFile.delete()) {
                    log.warn("临时文件删除失败，文件名{}.pdf", md5);
                }
            }
        }
    }

    @Override
    public String download(String url, String type) {
        String md5 = DigestUtils.md5DigestAsHex(url.getBytes(StandardCharsets.UTF_8));
        String filePath = ConfigUtils.getFilePath() + md5 + '.' + type;
        File file = new File(filePath);
        if (!file.exists()) {
            downloadFile(url, file);
        }
        return md5;
    }

    private void downloadFile(String url, File file) {
        restTemplate.execute(url, HttpMethod.GET, null, response -> {
            long contentLength = response.getHeaders().getContentLength();
            if (contentLength > MAX_FILE_SIZE) {
                throw new RuntimeException("文件大小超过限制");
            }
            try (InputStream inputStream = response.getBody(); FileOutputStream fos = new FileOutputStream(file)) {
                long totalRead = 0;
                byte[] buffer = new byte[16384];
                int read;
                while ((read = inputStream.read(buffer)) >= 0) {
                    totalRead += read;
                    if (totalRead > MAX_FILE_SIZE) {
                        if (file.exists() && !file.delete()) {
                            log.warn("删除超过大小限制的不完整文件失败，文件路径：{}", file.getAbsolutePath());
                        }
                        throw new RuntimeException("文件大小超过限制");
                    }
                    fos.write(buffer, 0, read);
                }
                return null;
            } catch (IOException e) {
                throw new RuntimeException("文件下载失败", e);
            }
        });
    }
}