package com.github.docpreview.service;

public interface PreviewService {

    void download(String url, String type);

    int convertPdfToImage(String hash);
}
