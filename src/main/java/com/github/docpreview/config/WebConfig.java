package com.github.docpreview.config;

import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        File folder = new File(ConfigUtils.getFilePath());
        if (!folder.exists() && !folder.mkdir()) {
            throw new RuntimeException("创建文件夹失败");
        }
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/", "file:" + ConfigUtils.getFilePath())
                .setCacheControl(CacheControl.maxAge(7, TimeUnit.DAYS));
    }
}
