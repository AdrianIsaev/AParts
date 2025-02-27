package com.example.aparts.controllers.products.seller;

import com.example.aparts.exceptions.AutoPartException;
import com.example.aparts.models.autoparts.Chassis;
import com.example.aparts.models.autoparts.Engine;
import com.example.aparts.models.autoparts.Fasteners;
import com.example.aparts.services.autoparts.EngineService;
import com.example.aparts.services.autoparts.FastenersService;
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
@RequestMapping("/fasteners")
@RequiredArgsConstructor
public final class FastenersAdminController {

    private final FastenersService fastenersService;

    @Operation(summary = "Get fastebers panel", description = "Endpoint for displaying fasteners panel")
    @GetMapping("/panel")
    public String getFastenersPanel(
            @Parameter(description = "Fasteners name (optional)") @RequestParam(name = "name", required = false) String name,
            @PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable,
            Model model, Principal principal) {
        model.addAttribute("user", fastenersService.getClientByPrincipal(principal));
        model.addAttribute("name", name);

        Page<Fasteners> usersPage = fastenersService.getChassisByName(name, pageable);
        model.addAttribute("fasteners", usersPage.getContent());
        model.addAttribute("currentPage", usersPage.getNumber());
        model.addAttribute("totalPages", usersPage.getTotalPages());
        model.addAttribute("totalItems", usersPage.getTotalElements());

        return "admin/fastenersPanel";
    }

    @Operation(summary = "Create fasteners", description = "Endpoint for creating a new fasteners")
    @PostMapping("/create")
    public String createFasteners(
            @RequestParam("file1") MultipartFile file1,
            @Parameter(description = "Fasteners object to be created", required = true) Fasteners fasteners,
            Principal principal, Model model) throws IOException {
        String st = fastenersService.createAPart(principal, fasteners, file1);
        if (st.equals("Success")) {
            return "redirect:/fasteners/selling";
        } else {
            model.addAttribute("user", fastenersService.getClientByPrincipal(principal));
            model.addAttribute("errorMessage", st);
            return "autoparts/fasteners/fastenersCreator";
        }
    }
    @Operation(summary = "Create fasteners page", description = "Endpoint for displaying create fasteners page")
    @GetMapping("/create")
    public String createFastenersPage(Model model, Principal principal) {
        model.addAttribute("user", fastenersService.getClientByPrincipal(principal));
        return "autoparts/fasteners/fastenersCreator";
    }

    @Operation(summary = "Edit fasteners form", description = "Endpoint for displaying edit fasteners form")
    @GetMapping("/edit/{id}")
    public String editFastenersForm(
            @Parameter(description = "Fasteners ID", required = true) @PathVariable("id") Long id,
            Model model, Principal principal) throws AutoPartException {
        model.addAttribute("user", fastenersService.getClientByPrincipal(principal));
        Fasteners fasteners = fastenersService.getAPartById(id);
        model.addAttribute("fasteners", fasteners);

        double price = fasteners.getPrice();
        String formattedPrice = Double.toString(price).replace(" ", "");
        System.out.println(formattedPrice);
        model.addAttribute("formattedPrice", formattedPrice);

        return "autoparts/fasteners/fastenersEditor";
    }


    @Operation(summary = "Update fasteners", description = "Endpoint for updating an existing fasteners")
    @PostMapping("/edit/{id}")
    public String updateFasteners(
            @RequestParam("file1") MultipartFile file1,
            @Parameter(description = "Fasteners ID", required = true) @PathVariable("id") Long id,
            @Parameter(description = "Updated fasteners information", required = true) Fasteners fasteners,
            Model model, Principal principal) throws IOException, AutoPartException {
        String st = fastenersService.updateAPart(id, fasteners, file1);
        if (st.equals("Success")) {
            return "redirect:/fasteners/selling";
        } else {
            model.addAttribute("user", fastenersService.getClientByPrincipal(principal));
            model.addAttribute("fasteners", fasteners);

            Fasteners orig = fastenersService.getAPartById(id);
            double price = orig.getPrice();
            String formattedPrice = Double.toString(price).replace(" ", "");
            model.addAttribute("formattedPrice", formattedPrice);

            model.addAttribute("errorMessage", st);
            return "autoparts/fasteners/fastenersEditor";
        }
    }

}
