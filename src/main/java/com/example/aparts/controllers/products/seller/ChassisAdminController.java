package com.example.aparts.controllers.products.seller;

import com.example.aparts.exceptions.AutoPartException;
import com.example.aparts.models.autoparts.Chassis;
import com.example.aparts.services.autoparts.ChassisService;
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
@RequestMapping("/chassis")
@RequiredArgsConstructor
public final class ChassisAdminController {
    private final ChassisService chassisService;

    @Operation(summary = "Get chassis panel", description = "Endpoint for displaying chassis panel")
    @GetMapping("/panel")
    public String getChassisPanel(
            @Parameter(description = "Chassis name (optional)") @RequestParam(name = "name", required = false) String name,
            @PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable,
            Model model, Principal principal) {
        model.addAttribute("user", chassisService.getClientByPrincipal(principal));
        model.addAttribute("name", name);

        Page<Chassis> usersPage = chassisService.getChassisByName(name, pageable);
        model.addAttribute("chassis", usersPage.getContent());
        model.addAttribute("currentPage", usersPage.getNumber());
        model.addAttribute("totalPages", usersPage.getTotalPages());
        model.addAttribute("totalItems", usersPage.getTotalElements());

        return "admin/chassisPanel";
    }

    @Operation(summary = "Create chassis", description = "Endpoint for creating a new chassis")
    @PostMapping("/create")
    public String createChassis(
            @RequestParam("file1") MultipartFile file1,
            @Parameter(description = "Chassis object to be created", required = true) Chassis chassis,
            Principal principal, Model model) throws IOException {
        String st = chassisService.createAPart(principal, chassis, file1);
        if (st.equals("Success")) {
            return "redirect:/chassis/selling";
        } else {
            model.addAttribute("user", chassisService.getClientByPrincipal(principal));
            model.addAttribute("errorMessage", st);
            return "autoparts/chassis/chassisCreator";
        }
    }

    @Operation(summary = "Create chassis page", description = "Endpoint for displaying create chassis page")
    @GetMapping("/create")
    public String createChassisPage(Model model, Principal principal) {
        model.addAttribute("user", chassisService.getClientByPrincipal(principal));
        return "autoparts/chassis/chassisCreator";
    }


    @Operation(summary = "Edit chassis form", description = "Endpoint for displaying edit chassis form")
    @GetMapping("/edit/{id}")
    public String editChassisForm(
            @Parameter(description = "Chassis ID", required = true) @PathVariable("id") Long id,
            Model model, Principal principal) throws AutoPartException {
        model.addAttribute("user", chassisService.getClientByPrincipal(principal));
        Chassis chassis = chassisService.getAPartById(id);
        model.addAttribute("chassis", chassis);

        double price = chassis.getPrice();
        String formattedPrice = Double.toString(price).replace(" ", "");
        System.out.println(formattedPrice);
        model.addAttribute("formattedPrice", formattedPrice);

        return "autoparts/chassis/chassisEditor";
    }



    @Operation(summary = "Update chassis", description = "Endpoint for updating an existing chassis")
    @PostMapping("/edit/{id}")
    public String updateChassis(
            @RequestParam("file1") MultipartFile file1,
            @Parameter(description = "Chassis ID", required = true) @PathVariable("id") Long id,
            @Parameter(description = "Updated chassis information", required = true) Chassis chassis,
            Model model, Principal principal) throws IOException, AutoPartException {
        String st = chassisService.updateAPart(id, chassis, file1);
        if (st.equals("Success")) {
            return "redirect:/chassis/selling";
        } else {
            model.addAttribute("user", chassisService.getClientByPrincipal(principal));
            model.addAttribute("chassis", chassis);

            Chassis orig = chassisService.getAPartById(id);
            double price = orig.getPrice();
            String formattedPrice = Double.toString(price).replace(" ", "");
            model.addAttribute("formattedPrice", formattedPrice);

            model.addAttribute("errorMessage", st);
            return "autoparts/chassis/chassisEditor";
        }
    }
}
