package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import com.udacity.jwdnd.course1.cloudstorage.service.security.EncryptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.SecureRandom;
import java.util.Base64;

@Controller
@RequestMapping("/credential")
public class CredentialController {

    private Logger logger = LoggerFactory.getLogger(CredentialController.class);

    private UserService userService;
    private CredentialService credentialService;
    private EncryptionService encryptionService;

    public CredentialController(UserService userService, CredentialService credentialService, EncryptionService encryptionService) {
        this.userService = userService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }


    /**
     * Create new credential
     * @param credential - Credential to create
     * @param authentication - Authenticated user
     */
    @PostMapping("/add")
    public String createCredential(Credential credential, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            String username = authentication.getName();
            int userId = userService.getUser(username).getUserId();
            String secretKey = generateSecretKey();
            String encryptPassword = encryptionService.encryptValue(credential.getPassword(), secretKey);
            credential.setUserId(userId);
            credential.setKey(secretKey);
            credential.setPassword(encryptPassword);
            credentialService.createCredential(credential);
            redirectAttributes.addFlashAttribute("successMessage", "Your Credentials were created successful.");
            return "redirect:/result";
        }  catch (Exception e) {
            logger.error("Cause: " + e.getCause() + ". Message: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Something went wrong with the Credential creation. Please try again!");
            return "redirect:/result";
        }
    }


    /**
     * Update credentials
     * @param credential - Credential to be updated
     */
    @PostMapping("/edit")
    public String updateNote(Credential credential, RedirectAttributes redirectAttributes) {
        try {
            String secretKey = generateSecretKey();
            String encryptPassword = encryptionService.encryptValue(credential.getPassword(), secretKey);
            credential.setKey(secretKey);
            credential.setPassword(encryptPassword);
            credentialService.updateCredential(credential);
            redirectAttributes.addFlashAttribute("successMessage", "Your credentials were updated successful.");
            return "redirect:/result";
        } catch (Exception e) {
            logger.error("Cause: " + e.getCause() + ". Message: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Something went wrong with the credentials update. Please try again!");
            return "redirect:/result";
        }
    }


    /**
     * Delete credentials
     * @param credentialId - Note to be deleted with the given id
     */
    @GetMapping("/delete/{credentialId}")
    public String deleteNote(@PathVariable int credentialId, RedirectAttributes redirectAttributes) {
        try {
            credentialService.deleteCredential(credentialId);
            redirectAttributes.addFlashAttribute("successMessage", "Your credentials were deleted successful.");
            return "redirect:/result";
        } catch (Exception e) {
            logger.error("Cause: " + e.getCause() + ". Message: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Something went wrong with the credentials delete. Please try again!");
            return "redirect:/result";
        }
    }


    /**
     * Method to generate a secret key
     * @return secret key
     */
    private String generateSecretKey() {
        try{
            SecureRandom random = new SecureRandom();
            byte[] key = new byte[16];
            random.nextBytes(key);
            return Base64.getEncoder().encodeToString(key);
        } catch (Exception e) {
            logger.error("Cause: " + e.getCause() + ". Message: " + e.getMessage());
        }
        return null;
    }
}
