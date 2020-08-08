package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private Logger logger = LoggerFactory.getLogger(NoteController.class);

    private UserService userService;
    private NoteService noteService;

    public NoteController(UserService userService,NoteService noteService) {
        this.userService = userService;
        this.noteService = noteService;
    }


    /**
     * Create or Update a new note
     * @param note - Note to create or update
     * @param authentication - Authenticated user
     */
    @PostMapping
    public String createOrUpdateNote(Note note, Authentication authentication, RedirectAttributes redirectAttributes) {

        String username = authentication.getName();
        int userId = userService.getUser(username).getUserId();
        note.setUserId(userId);

        if(note.getNoteId().intValue() > 0){
            try {
                noteService.updateNote(note);
                redirectAttributes.addFlashAttribute("successMessage", "Your note was updated successful.");
                return "redirect:/result";
            } catch (Exception e) {
                logger.error("Cause: " + e.getCause() + ". Message: " + e.getMessage());
                redirectAttributes.addFlashAttribute("errorMessage", "Something went wrong with the note update. Please try again!");
                return "redirect:/result";
            }
        }else{
            try {
                noteService.createNote(note);
                redirectAttributes.addFlashAttribute("successMessage", "Your note was created successful.");
                return "redirect:/result";
            }  catch (Exception e) {
                logger.error("Cause: " + e.getCause() + ". Message: " + e.getMessage());
                redirectAttributes.addFlashAttribute("errorMessage", "Something went wrong with the note update. Please try again!");
                return "redirect:/result";
            }
        }
    }

    /**
     * Delete note
     * @param noteId - Note to be deleted with the given id
     */
    @GetMapping("/delete/{noteId}")
    public String deleteNote(@PathVariable int noteId, RedirectAttributes redirectAttributes) {
        try {
            noteService.deleteNote(noteId);
            redirectAttributes.addFlashAttribute("successMessage", "Your note was deleted successful.");
            return "redirect:/result";
        } catch (Exception e) {
            logger.error("Cause: " + e.getCause() + ". Message: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Something went wrong with the note delete. Please try again!");
            return "redirect:/result";
        }
    }
}