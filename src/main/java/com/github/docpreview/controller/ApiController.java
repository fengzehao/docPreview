package com.github.docpreview.controller;

import com.github.docpreview.model.ConvertResult;
import com.github.docpreview.service.PreviewService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final PreviewService service;

    public ApiController(PreviewService service) {
        this.service = service;
    }

    @PostMapping("/download")
    public String download(@RequestParam("url") String url, @RequestParam("type") String type) {
        try {
            String md5 = service.download(url, type);
            return "{\"flag\":true,\"msg\":\"" + md5 + "\"}";
        } catch (Exception e) {
            return "{\"flag\":false,\"msg\":\"" + e.getCause().getMessage() + "\"}";
        }
    }

    @PostMapping("/convert")
    public String convertToImage(@RequestParam("url") String url) {
        try {
            ConvertResult result = service.convertPdfToImg(url);
            return "{\"flag\":true,\"msg\":{\"md5\":\"" + result.md5() + "\",\"total\":" + result.total() + "}}";
        } catch (Exception e) {
            return "{\"flag\":false,\"msg\":\"" + e.getCause().getMessage() + "\"}";
        }
    }
}
