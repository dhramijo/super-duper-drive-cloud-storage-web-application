package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
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


    @PostMapping("/upload")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile fileUpload, Model model, RedirectAttributes redirectAttributes, Authentication authentication) throws IOException {
        String username = authentication.getName();
        int userId = userService.getUser(username).getUserId();
        if (fileService.getFile(fileUpload.getOriginalFilename()) != null){
            String messageError = "Sorry, you cannot upload two files with the same name!";
            redirectAttributes.addFlashAttribute("messageError", messageError);
        } else {
            fileService.createFile(new File(null, fileUpload.getOriginalFilename(), fileUpload.getContentType(), String.valueOf(fileUpload.getSize()), userId, fileUpload.getBytes()));
            model.addAttribute("files", fileService.getFiles());
        }
        return "redirect:/home";
    }

}
