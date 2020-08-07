package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private FileService fileService;
    private NoteService noteService;
    private CredentialService credentialService;
    private UserService userService;

    public HomeController(FileService fileService, NoteService noteService, CredentialService credentialService, UserService userService) {
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @GetMapping("/home")
    public String getHome(Note note, Credential credential, Authentication authentication, Model model){
        String username = authentication.getName();
        int userId = userService.getUser(username).getUserId();
        List<File> files = fileService.getFiles(userId);
        List<Note> notes = noteService.getNotes(userId);
        List<Credential> credentials = credentialService.getCredentials(userId);
        model.addAttribute("notes",notes);
        model.addAttribute("files",files);
        model.addAttribute("credentials",credentials);
        return "home";
    }

    @GetMapping("/result")
    public String result() {
        return "result";
    }
}
