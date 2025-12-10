package com.github.docpreview.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PreviewController {

    @GetMapping("/preview")
    public String preview(@RequestParam("url") String url, @RequestParam("type") String fileType, Model model) {
        if ("jpg".equals(fileType) || "jpeg".equals(fileType) || "png".equals(fileType)) {
            model.addAttribute("url", url);
            model.addAttribute("type", fileType);
            return "img";
        } else if ("pdf".equals(fileType)) {
            model.addAttribute("url", url);
            model.addAttribute("type", fileType);
            return "pdf";
        } else {
            model.addAttribute("error", "该文件类型暂不支持预览");
            return "error";
        }
    }
}