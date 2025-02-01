package com.example.aparts.controllers;

import com.example.aparts.models.Client;
import com.example.aparts.models.autoparts.AutoPart;
import com.example.aparts.models.enums.AutoPartCategory;
import com.example.aparts.services.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;


@Controller
public class ClientController {
    private final ClientService clientService;
    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @Operation(summary = "Login page", description = "Endpoint for displaying login page")
    @GetMapping("/login")
    public String login() {
        return "authorization/login";
    }

    @Operation(summary = "Registration page", description = "Endpoint for displaying registration page")
    @GetMapping("/registration")
    public String registration() {
        return "authorization/registration";
    }

    @Operation(summary = "Create user", description = "Endpoint for creating a new user")
    @PostMapping("/registration")
    public String createUser(
            @Parameter(description = "User object to be created", required = true)
            @ModelAttribute
            @Validated
            Client client, BindingResult bindingResult,
            Model model) {
        model.addAttribute("client", client);
        System.out.println(bindingResult.getAllErrors());
        System.out.println(client);
        if (bindingResult.hasErrors()) {
            model.addAttribute("validationErrors", bindingResult);
            return "authorization/registration";
        }
        if (!clientService.createClient(client)) {
            model.addAttribute("errorMessage", "Пользователь с email: " + client.getEmail() + " уже существует");
            return "authorization/registration";
        }
        return "redirect:/login";
    }
    @Operation(summary = "Login error page", description = "Endpoint for displaying login error page")
    @GetMapping("/login-error")
    public String login(HttpServletRequest request, Model model){
        HttpSession session = request.getSession(false);
        String errorMessage = null;
        if (session!=null) {
            AuthenticationException exception = (AuthenticationException) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (exception != null){
                errorMessage = exception.getMessage();
            }
        }
        System.out.println(errorMessage);
        model.addAttribute("errorMessage", "Введены неверные данные");
        return "/authorization/login";
    }
    @Operation(summary = "Main page", description = "Endpoint for displaying main page with products")
    @GetMapping("")
    public String categories(Model model, Principal principal) {
        List<AutoPart> autoParts = clientService.getAllAutoParts();
        model.addAttribute("autoParts", autoParts);
        model.addAttribute("user", clientService.getClientByPrincipal(principal));
        model.addAttribute("types", AutoPartCategory.values());
        return "user/main";
    }
    @Operation(summary = "Clone main page", description = "Endpoint for displaying clone main page with products")
    @GetMapping("/main")
    public String cloneCategories(Model model, Principal principal) {
        List<AutoPart> autoParts = clientService.getAllAutoParts();
        model.addAttribute("autoParts", autoParts);
        model.addAttribute("user", clientService.getClientByPrincipal(principal));
        model.addAttribute("types", AutoPartCategory.values());
        return "user/main";
    }

    @Operation(summary = "Account page", description = "Endpoint for displaying user account page")
    @GetMapping("/account")
    public String account(
            @Parameter(description = "Authenticated client", required = true) @AuthenticationPrincipal Client client,
            Model model) {
        model.addAttribute("user", client);
        model.addAttribute("orders", clientService.getHistory(client));
        return "user/account";
    }
}