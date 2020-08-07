package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/file")
public class FileController {

    private FileService fileService;
    private UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }


    /**
     * Upload new file
     * @param fileUpload - File to upload
     * @param model - File data model
     * @param authentication - Authenticated user
     * @throws IOException
     */
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile fileUpload, Model model, RedirectAttributes redirectAttributes, Authentication authentication) throws IOException {
        String username = authentication.getName();
        int userId = userService.getUser(username).getUserId();
        if (fileService.getFileByName(fileUpload.getOriginalFilename()) != null){
            String messageError = "Sorry, you cannot upload two files with the same name!";
            redirectAttributes.addFlashAttribute("messageError", messageError);
        } else {
            fileService.createFile(new File(null, fileUpload.getOriginalFilename(), fileUpload.getContentType(), String.valueOf(fileUpload.getSize()), userId, fileUpload.getBytes()));
            model.addAttribute("files", fileService.getFiles());
        }
        return "redirect:/home";
    }


    /**
     * Download the file
     * @param fileId
     */
    @GetMapping("/view/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable int fileId) {
        File file = fileService.getFile(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(new ByteArrayResource(file.getFileData()));
    }


    /**
     * Delete the @File with fileId
     * @param fileId
     */
    @GetMapping("/delete/{fileId}")
    public String deleteFile(@PathVariable int fileId) {
        fileService.deleteFile(fileId);
        return "redirect:/home";
    }
}
