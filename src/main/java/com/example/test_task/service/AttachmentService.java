package com.example.test_task.service;

import com.example.test_task.model.Attachment;
import com.example.test_task.repository.AttachmentRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;

@Service
public class AttachmentService {

    @Autowired
    AttachmentRepository attachmentRepository;

    private final String upload_path = "D:\\Spring\\test_task\\src\\main\\resources\\static\\files";


    public String uploadFileToDb(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            if (checkDirectory().length() > 0){
                String path = checkDirectory();

                String filename = file.getOriginalFilename();
                String extra = FilenameUtils.getExtension(filename);
                String createdName = UUID.randomUUID() + "_unique" + "." + extra;
                file.transferTo(new File(path + "/" + createdName));

                Attachment attachment = new Attachment();
                attachment.setAttachmentPath(path);
                attachment.setCreatedName(createdName);
                attachment.setOriginName(filename);
                attachment.setContentType(file.getContentType());
                attachment.setSize(file.getSize());
                attachment.setUnixTime(System.currentTimeMillis() / 1000L);

                attachmentRepository.save(attachment);

                redirectAttributes.addFlashAttribute("msg", "Successfully uploaded in Database!!");
                return "redirect:/home/getFiles";
            } else {
                redirectAttributes.addFlashAttribute("msg", "Error uploading");
            }
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("msg", "Error uploading");
        }
        return "error";
    }

    public String checkDirectory() {

        LocalDate date = LocalDate.now();
        int year = date.getYear();
        Month month = date.getMonth();
        int day = date.getDayOfMonth();
        boolean files_dir = false;
        boolean years_dir = false;
        boolean months_dir = false;
        boolean days_dir = false;

        File files = new File(AttachmentService.this.upload_path);
        if (!files.exists()) {
            if (files.mkdir()) {
                System.out.println("Files Directory is created!");
                files_dir = true;
            } else {
                System.out.println("Failed to create files!");
            }
        } else {
            files_dir = true;
        }

        File years = new File(AttachmentService.this.upload_path + "/" + year);
        if (!years.exists()) {
            if (years.mkdirs()) {
                System.out.println("Year directories are created!");
                years_dir = true;
            } else {
                System.out.println("Failed to create year directories!");
            }
        } else {
            years_dir = true;
        }

        File months = new File(AttachmentService.this.upload_path + "/" + year + "/" + month);
        if (!months.exists()) {
            if (months.mkdirs()) {
                System.out.println("Month directories are created!");
                months_dir = true;
            } else {
                System.out.println("Failed to create month directories!");
            }
        } else {
            months_dir = true;
        }

        String fullPath = "";
        File days = new File(AttachmentService.this.upload_path + "/" + year + "/" + month + "/" + day);
        if (!days.exists()) {
            if (days.mkdirs()) {
                System.out.println("Day directories are created!");
                days_dir = true;
                fullPath = AttachmentService.this.upload_path + "/" + year + "/" + month + "/" + day;
            } else {
                System.out.println("Day to create year directories!");
            }
        } else {
            fullPath = AttachmentService.this.upload_path + "/" + year + "/" + month + "/" + day;
            days_dir = true;
        }

        return files_dir && years_dir && months_dir && days_dir ? fullPath : null;
    }

//    public void getFileById(UUID fileId, HttpServletResponse res) {
//        try {
//            if (attachmentRepository.findById(fileId).isPresent()) {
//                Attachment attachment = attachmentRepository.findById(fileId).get();
//                res.setHeader("Content-Disposition", "attachment; filename=" + attachment.getOriginName());
//                res.setHeader("Content-Transfer-Encoding", "binary");
//                BufferedOutputStream outputStream = new BufferedOutputStream(res.getOutputStream());
//                FileInputStream inputStream = new FileInputStream(attachment.getAttachmentPath() + "/" + attachment.getCreatedName());
//                int len;
//                byte[] bytes = new byte[1024];
//                while ((len = inputStream.read(bytes)) > 0) {
//                    outputStream.write(bytes, 0, len);
//                }
//                outputStream.close();
//                res.flushBuffer();
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
