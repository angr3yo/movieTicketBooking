package com.moviebooking.controller;

import com.moviebooking.service.UserService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * CONTROLLER LAYER (MVC)
 * Handles authentication: login and registration.
 *
 * GRASP Controller: dedicated controller for auth use cases.
 * SOLID SRP: only authentication concerns.
 */
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // ── Login ────────────────────────────────────────────────────────────────
    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String logout,
                            Model model) {
        if (error != null)  model.addAttribute("error", "Invalid email or password.");
        if (logout != null) model.addAttribute("message", "You have been logged out.");
        return "auth/login";
    }

    // ── Register ─────────────────────────────────────────────────────────────
    @GetMapping("/register")
    public String registerPage() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam @NotBlank String name,
                               @RequestParam @Email String email,
                               @RequestParam @NotBlank String password,
                               @RequestParam @NotBlank String confirmPassword,
                               RedirectAttributes redirectAttributes) {
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match.");
            return "redirect:/register";
        }
        try {
            userService.registerUser(name, email, password);
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }
}
