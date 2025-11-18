package com.github.docpreview.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PreviewController {

    @GetMapping("/preview")
    public String preview(Model model) {
        model.addAttribute("error", "该文件类型暂不支持预览");
        return "error";
    }
}
