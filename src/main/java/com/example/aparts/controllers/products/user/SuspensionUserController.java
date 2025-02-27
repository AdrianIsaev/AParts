package com.example.aparts.controllers.products.user;


import com.example.aparts.exceptions.AutoPartException;
import com.example.aparts.models.Client;
import com.example.aparts.models.autoparts.Chassis;
import com.example.aparts.models.autoparts.Other;
import com.example.aparts.models.autoparts.Suspension;
import com.example.aparts.models.enums.AutoPartCategory;
import com.example.aparts.repositories.autoparts.SuspensionRepository;
import com.example.aparts.services.autoparts.SuspensionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/suspension")
public class SuspensionUserController {
    private final SuspensionService suspensionService;
    @Autowired
    public SuspensionUserController(SuspensionService suspensionService){
        this.suspensionService = suspensionService;
    }

    @Operation(summary = "Show suspension", description = "Endpoint for displaying all available chassis")
    @GetMapping("/selling")
    public String showSuspension(Model model, Principal principal){
        List<Suspension> suspensions = suspensionService.getAllAParts();
        model.addAttribute("user", suspensionService.getClientByPrincipal(principal));
        model.addAttribute("suspension", suspensions);
        model.addAttribute("types", AutoPartCategory.values());
        return "/autoparts/suspension/suspension";
    }


    @Operation(summary = "Get suspension page", description = "Endpoint for displaying a specific suspension")
    @GetMapping("/{id}")
    public String getSuspensionPage(
            @PathVariable("id") Long id,
            Principal principal, Model model) throws AutoPartException {
        Client client = suspensionService.getClientByPrincipal(principal);
        Suspension suspension = suspensionService.getAPartById(id);
        model.addAttribute("user", client);
        model.addAttribute("suspension", suspension);
        return "autoparts/suspension/suspensionPage";
    }
}
