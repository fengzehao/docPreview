package com.github.docpreview.controller;

import com.github.docpreview.service.PreviewService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final PreviewService service;

    public ApiController(PreviewService service) {
        this.service = service;
    }

    @PostMapping(value = "/download", produces = MediaType.APPLICATION_JSON_VALUE)
    public String download(@RequestParam("url") String url, @RequestParam("type") String type) {
        try {
            String md5 = service.download(url, type);
            return "{\"flag\":true,\"msg\":\"" + md5+ "\"}";
        } catch (Exception e) {
            return "{\"flag\":false,\"msg\":\"" + e.getCause().getMessage() + "\"}";
        }
    }
}
