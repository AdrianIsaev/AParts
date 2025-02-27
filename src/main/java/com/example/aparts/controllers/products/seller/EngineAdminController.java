package com.example.aparts.controllers.products.seller;

import com.example.aparts.exceptions.AutoPartException;
import com.example.aparts.models.autoparts.Chassis;
import com.example.aparts.models.autoparts.Engine;
import com.example.aparts.services.autoparts.ChassisService;
import com.example.aparts.services.autoparts.EngineService;
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
@RequestMapping("/engine")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@RequiredArgsConstructor
public final class EngineAdminController {

    private final EngineService engineService;

    @Operation(summary = "Get engine panel", description = "Endpoint for displaying engine panel")
    @GetMapping("/panel")
    public String getEnginePanel(
            @Parameter(description = "Engine name (optional)") @RequestParam(name = "name", required = false) String name,
            @PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable,
            Model model, Principal principal) {
        model.addAttribute("user", engineService.getClientByPrincipal(principal));
        model.addAttribute("name", name);

        Page<Engine> usersPage = engineService.getChassisByName(name, pageable);
        model.addAttribute("engines", usersPage.getContent());
        model.addAttribute("currentPage", usersPage.getNumber());
        model.addAttribute("totalPages", usersPage.getTotalPages());
        model.addAttribute("totalItems", usersPage.getTotalElements());

        return "admin/enginePanel";
    }

    @Operation(summary = "Create engine", description = "Endpoint for creating a new engine")
    @PostMapping("/create")
    public String createEngine(
            @RequestParam("file1") MultipartFile file1,
            @Parameter(description = "Engine object to be created", required = true) Engine engine,
            Principal principal, Model model) throws IOException {
        String st = engineService.createAPart(principal, engine, file1);
        if (st.equals("Success")) {
            return "redirect:/engine/selling";
        } else {
            model.addAttribute("user", engineService.getClientByPrincipal(principal));
            model.addAttribute("errorMessage", st);
            return "autoparts/chassis/chassisCreator";
        }
    }
    @Operation(summary = "Create engine page", description = "Endpoint for displaying create engine page")
    @GetMapping("/create")
    public String createEnginePage(Model model, Principal principal) {
        model.addAttribute("user", engineService.getClientByPrincipal(principal));
        return "autoparts/engine/engineCreator";
    }

    @Operation(summary = "Edit engine form", description = "Endpoint for displaying edit engine form")
    @GetMapping("/edit/{id}")
    public String editEngineForm(
            @Parameter(description = "Engine ID", required = true) @PathVariable("id") Long id,
            Model model, Principal principal) throws AutoPartException {
        model.addAttribute("user", engineService.getClientByPrincipal(principal));
        Engine engine = engineService.getAPartById(id);
        model.addAttribute("engine", engine);

        double price = engine.getPrice();
        String formattedPrice = Double.toString(price).replace(" ", "");
        System.out.println(formattedPrice);
        model.addAttribute("formattedPrice", formattedPrice);

        return "autoparts/engine/engineEditor";
    }

    @Operation(summary = "Update engine", description = "Endpoint for updating an existing engine")
    @PostMapping("/edit/{id}")
    public String updateEngine(
            @RequestParam("file1") MultipartFile file1,
            @Parameter(description = "Engine ID", required = true) @PathVariable("id") Long id,
            @Parameter(description = "Updated engine information", required = true) Engine engine,
            Model model, Principal principal) throws IOException, AutoPartException {
        String st = engineService.updateAPart(id, engine, file1);
        if (st.equals("Success")) {
            return "redirect:/engine/selling";
        } else {
            model.addAttribute("user", engineService.getClientByPrincipal(principal));
            model.addAttribute("engine", engine);

            Engine orig = engineService.getAPartById(id);
            double price = orig.getPrice();
            String formattedPrice = Double.toString(price).replace(" ", "");
            model.addAttribute("formattedPrice", formattedPrice);

            model.addAttribute("errorMessage", st);
            return "autoparts/engine/engineEditor";
        }
    }
}
