package com.example.aparts.controllers.products.seller;


import com.example.aparts.exceptions.AutoPartException;
import com.example.aparts.models.autoparts.Chassis;
import com.example.aparts.models.autoparts.Engine;
import com.example.aparts.models.autoparts.Fasteners;
import com.example.aparts.models.autoparts.Other;
import com.example.aparts.services.autoparts.EngineService;
import com.example.aparts.services.autoparts.OtherService;
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
@RequestMapping("/other")
@RequiredArgsConstructor
public class OtherAdminController {
    private final OtherService otherService;

    @Operation(summary = "Get other panel", description = "Endpoint for displaying other panel")
    @GetMapping("/panel")
    public String getOtherPanel(
            @Parameter(description = "Other name (optional)") @RequestParam(name = "name", required = false) String name,
            @PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable,
            Model model, Principal principal) {
        model.addAttribute("user", otherService.getClientByPrincipal(principal));
        model.addAttribute("name", name);

        Page<Other> usersPage = otherService.getChassisByName(name, pageable);
        model.addAttribute("others", usersPage.getContent());
        model.addAttribute("currentPage", usersPage.getNumber());
        model.addAttribute("totalPages", usersPage.getTotalPages());
        model.addAttribute("totalItems", usersPage.getTotalElements());

        return "admin/otherPanel";
    }

    @Operation(summary = "Create other", description = "Endpoint for creating a new other")
    @PostMapping("/create")
    public String createOther(
            @RequestParam("file1") MultipartFile file1,
            @Parameter(description = "Other object to be created", required = true) Other other,
            Principal principal, Model model) throws IOException {
        String st = otherService.createAPart(principal, other, file1);
        if (st.equals("Success")) {
            return "redirect:/other/selling";
        } else {
            model.addAttribute("user", otherService.getClientByPrincipal(principal));
            model.addAttribute("errorMessage", st);
            return "autoparts/other/otherCreator";
        }
    }
    @Operation(summary = "Create other page", description = "Endpoint for displaying create other page")
    @GetMapping("/create")
    public String createOtherPage(Model model, Principal principal) {
        model.addAttribute("user", otherService.getClientByPrincipal(principal));
        return "autoparts/other/otherCreator";
    }


    @Operation(summary = "Edit other form", description = "Endpoint for displaying edit other form")
    @GetMapping("/edit/{id}")
    public String editOtherForm(
            @Parameter(description = "Other ID", required = true) @PathVariable("id") Long id,
            Model model, Principal principal) throws AutoPartException {
        model.addAttribute("user", otherService.getClientByPrincipal(principal));
        Other other = otherService.getAPartById(id);
        model.addAttribute("other", other);

        double price = other.getPrice();
        String formattedPrice = Double.toString(price).replace(" ", "");
        System.out.println(formattedPrice);
        model.addAttribute("formattedPrice", formattedPrice);

        return "autoparts/other/otherEditor";
    }

    @Operation(summary = "Update other", description = "Endpoint for updating an existing other")
    @PostMapping("/edit/{id}")
    public String updateOther(
            @RequestParam("file1") MultipartFile file1,
            @Parameter(description = "Other ID", required = true) @PathVariable("id") Long id,
            @Parameter(description = "Updated other information", required = true) Other other,
            Model model, Principal principal) throws IOException, AutoPartException {
        String st = otherService.updateAPart(id, other, file1);
        if (st.equals("Success")) {
            return "redirect:/other/selling";
        } else {
            model.addAttribute("user", otherService.getClientByPrincipal(principal));
            model.addAttribute("other", other);

            Other orig = otherService.getAPartById(id);
            double price = orig.getPrice();
            String formattedPrice = Double.toString(price).replace(" ", "");
            model.addAttribute("formattedPrice", formattedPrice);

            model.addAttribute("errorMessage", st);
            return "autoparts/other/otherEditor";
        }
    }
}
