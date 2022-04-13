package com.example.test_task.controller;

import com.example.test_task.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/upload")
public class FileUploadController {

    @Autowired
    AttachmentService attachmentService;

    @GetMapping()
    public String getUploadPage() {
        return "file-upload-form-db";
    }

    @PostMapping()
    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        return attachmentService.uploadFileToDb(file, redirectAttributes);
    }
}
