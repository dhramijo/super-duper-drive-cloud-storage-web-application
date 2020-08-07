package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Controller
@RequestMapping("/file")
public class FileController {

    private Logger logger = LoggerFactory.getLogger(FileController.class);

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
     */
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile fileUpload, Model model, RedirectAttributes redirectAttributes, Authentication authentication) {
        String username = authentication.getName();
        int userId = userService.getUser(username).getUserId();
        if (fileService.getFileByName(fileUpload.getOriginalFilename()) != null){
            redirectAttributes.addFlashAttribute("errorMessage", "Sorry, you cannot upload two files with the same name!");
        } else {
            try {
                fileService.createFile(new File(null, fileUpload.getOriginalFilename(), fileUpload.getContentType(), String.valueOf(fileUpload.getSize()), userId, fileUpload.getBytes()));
                model.addAttribute("files", fileService.getFiles(userId));
                redirectAttributes.addFlashAttribute("successMessage", "Your file upload was successful.");
            } catch (Exception e) {
                logger.error("Cause: " + e.getCause() + ". Message: " + e.getMessage());
                redirectAttributes.addFlashAttribute("errorMessage", "Something went wrong with the file upload. Please try again!");
                return "redirect:/result";
            }
        }
        return "redirect:/result";
    }


    /**
     * Download the file
     * @param fileId
     */
    @GetMapping("/view/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable int fileId) {
        try {
            File file = fileService.getFileById(fileId);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(file.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                    .body(new ByteArrayResource(file.getFileData()));
        } catch (Exception e) {
            logger.error("Cause: " + e.getCause() + ". Message: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }


    /**
     * Delete the File with fileId
     * @param fileId
     */
    @GetMapping("/delete/{fileId}")
    public String deleteFile(@PathVariable int fileId, RedirectAttributes redirectAttributes) {
        try {
            fileService.deleteFile(fileId);
            redirectAttributes.addFlashAttribute("successMessage", "Your file was deleted successful.");
            return "redirect:/result";
        } catch (Exception e) {
            logger.error("Cause: " + e.getCause() + ". Message: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Something went wrong with the file delete. Please try again!");
            return "redirect:/result";
        }
    }
}
