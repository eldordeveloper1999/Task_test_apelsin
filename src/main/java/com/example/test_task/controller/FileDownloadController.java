package com.example.test_task.controller;

import com.example.test_task.model.Attachment;
import com.example.test_task.repository.AttachmentRepository;
import com.example.test_task.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/download")
public class FileDownloadController {

    @Autowired
    AttachmentService attachmentService;

    @Autowired
    AttachmentRepository attachmentRepository;

    @GetMapping("/{fileId}")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable UUID fileId, HttpServletResponse res) throws IOException {

        Attachment attachment = attachmentRepository.getById(fileId);

        BufferedOutputStream outputStream = new BufferedOutputStream(res.getOutputStream());

        FileInputStream inputStream = new FileInputStream(attachment.getAttachmentPath() + "/" + attachment.getCreatedName());

        int len;

        byte[] bytes = new byte[1024];

        while ((len = inputStream.read(bytes)) > 0) {
            outputStream.write(bytes, 0, len);
        }
        outputStream.close();

        res.flushBuffer();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + attachment.getOriginName() + "\"")
                .body(new ByteArrayResource(bytes));
    }

}
