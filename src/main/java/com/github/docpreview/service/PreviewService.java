package com.github.docpreview.service;

public interface PreviewService {

    int convertPdfToImg(String url);

    String download(String url, String type);
}
