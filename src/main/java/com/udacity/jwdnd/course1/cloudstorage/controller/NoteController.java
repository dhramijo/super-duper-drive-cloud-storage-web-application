package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/notes")
public class NoteController {

    private UserService userService;
    private NoteService noteService;

    public NoteController(UserService userService,NoteService noteService) {
        this.userService = userService;
        this.noteService = noteService;
    }


    @PostMapping("/add")
    public String createNote(Note note, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            String username = authentication.getName();
            int userId = userService.getUser(username).getUserId();
            note.setUserId(userId);
            noteService.createNote(note);
            redirectAttributes.addFlashAttribute("successMessage", "Your note was updated successful.");
            return "redirect:/result";
        }  catch (Exception e) {
            System.out.println("Cause: " + e.getCause() + ". Message: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Something went wrong with the note update. Please try again!");
            return "redirect:/result";
        }
    }



    @PostMapping("/edit")
    public String updateNote(Note note, RedirectAttributes redirectAttributes) {
        try {
            noteService.updateNote(note);
            redirectAttributes.addFlashAttribute("successMessage", "Your note was updated successful.");
            return "redirect:/result";
        } catch (Exception e) {
            System.out.println("Cause: " + e.getCause() + ". Message: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Something went wrong with the note update. Please try again!");
            return "redirect:/result";
        }
    }


    @GetMapping("/delete/{noteId}")
    public String deleteNote(@PathVariable int noteId, RedirectAttributes redirectAttributes) {
        try {
            noteService.deleteNote(noteId);
            redirectAttributes.addFlashAttribute("successMessage", "Your note was deleted successful.");
            return "redirect:/result";
        } catch (Exception e) {
            System.out.println("Cause: " + e.getCause() + ". Message: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Something went wrong with the note delete. Please try again!");
            return "redirect:/result";
        }
    }
}
