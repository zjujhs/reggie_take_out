package com.jjjhs.reggie.controller;

import com.jjjhs.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {
    @Value("${reggie.path}")
    private String path;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String new_name = UUID.randomUUID() + suffix;
        File dir = new File(path);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(path+new_name));
        } catch (IOException e) {
//            throw new RuntimeException(e);
        }
        return R.success(new_name);
    }

    @GetMapping("/download")
    public void download(HttpServletResponse response, String name) throws IOException {

        FileInputStream fileInputStream = null;
        ServletOutputStream servletOutputStream = null;
        try {
            response.setContentType("image/jpeg");

            fileInputStream = new FileInputStream(new File(path+name));
            servletOutputStream = response.getOutputStream();

            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = fileInputStream.read(bytes)) != -1) {
                servletOutputStream.write(bytes, 0, len);
                servletOutputStream.flush();
            }

            fileInputStream.close();
            servletOutputStream.close();
        } catch (Exception e) { }

    }
}
