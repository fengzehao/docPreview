package com.github.docpreview.service;

import com.github.docpreview.model.ConvertResult;

public interface PreviewService {

    ConvertResult convertPdfToImg(String url);

    String download(String url, String type);
}
