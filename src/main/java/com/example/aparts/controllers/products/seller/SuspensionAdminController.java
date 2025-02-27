package com.example.aparts.controllers.products.seller;


import com.example.aparts.exceptions.AutoPartException;
import com.example.aparts.models.autoparts.Chassis;
import com.example.aparts.models.autoparts.Engine;
import com.example.aparts.models.autoparts.Other;
import com.example.aparts.models.autoparts.Suspension;
import com.example.aparts.services.autoparts.EngineService;
import com.example.aparts.services.autoparts.SuspensionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Controller
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@RequestMapping("/suspension")
@RequiredArgsConstructor
public class SuspensionAdminController {
    private final SuspensionService suspensionService;

    @Operation(summary = "Get suspension panel", description = "Endpoint for displaying suspension panel")
    @GetMapping("/panel")
    public String getSuspensionPanel(
            @Parameter(description = "Suspension name (optional)") @RequestParam(name = "name", required = false) String name,
            @PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable,
            Model model, Principal principal) {
        model.addAttribute("user", suspensionService.getClientByPrincipal(principal));
        model.addAttribute("name", name);

        Page<Suspension> usersPage = suspensionService.getChassisByName(name, pageable);
        model.addAttribute("suspensions", usersPage.getContent());
        model.addAttribute("currentPage", usersPage.getNumber());
        model.addAttribute("totalPages", usersPage.getTotalPages());
        model.addAttribute("totalItems", usersPage.getTotalElements());

        return "admin/suspensionPanel";
    }

    @Operation(summary = "Create suspension", description = "Endpoint for creating a new suspension")
    @PostMapping("/create")
    public String createSuspension(
            @RequestParam("file1") MultipartFile file1,
            @Parameter(description = "Suspension object to be created", required = true) Suspension suspension,
            Principal principal, Model model) throws IOException {
        String st = suspensionService.createAPart(principal, suspension, file1);
        if (st.equals("Success")) {
            return "redirect:/suspension/selling";
        } else {
            model.addAttribute("user", suspensionService.getClientByPrincipal(principal));
            model.addAttribute("errorMessage", st);
            return "autoparts/suspension/suspensionCreator";
        }
    }
    @Operation(summary = "Create suspension page", description = "Endpoint for displaying create suspension page")
    @GetMapping("/create")
    public String createSuspensionPage(Model model, Principal principal) {
        model.addAttribute("user", suspensionService.getClientByPrincipal(principal));
        return "autoparts/suspension/suspensionCreator";
    }

    @Operation(summary = "Edit suspension form", description = "Endpoint for displaying edit suspension form")
    @GetMapping("/edit/{id}")
    public String editSuspensionForm(
            @Parameter(description = "Suspension ID", required = true) @PathVariable("id") Long id,
            Model model, Principal principal) throws AutoPartException {
        model.addAttribute("user", suspensionService.getClientByPrincipal(principal));
        Suspension suspension = suspensionService.getAPartById(id);
        model.addAttribute("suspension", suspension);

        double price = suspension.getPrice();
        String formattedPrice = Double.toString(price).replace(" ", "");
        System.out.println(formattedPrice);
        model.addAttribute("formattedPrice", formattedPrice);

        return "autoparts/suspension/suspensionEditor";
    }

    @Operation(summary = "Update suspension", description = "Endpoint for updating an existing suspension")
    @PostMapping("/edit/{id}")
    public String updateSuspension(
            @RequestParam("file1") MultipartFile file1,
            @Parameter(description = "Suspension ID", required = true) @PathVariable("id") Long id,
            @Parameter(description = "Updated suspension information", required = true) Suspension suspension,
            Model model, Principal principal) throws IOException, AutoPartException {
        String st = suspensionService.updateAPart(id, suspension, file1);
        if (st.equals("Success")) {
            return "redirect:/suspension/selling";
        } else {
            model.addAttribute("user", suspensionService.getClientByPrincipal(principal));
            model.addAttribute("suspension", suspension);

            Suspension orig = suspensionService.getAPartById(id);
            double price = orig.getPrice();
            String formattedPrice = Double.toString(price).replace(" ", "");
            model.addAttribute("formattedPrice", formattedPrice);

            model.addAttribute("errorMessage", st);
            return "autoparts/suspension/suspensionEditor";
        }
    }



}
