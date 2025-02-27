package com.example.aparts.controllers.products.user;

import com.example.aparts.exceptions.AutoPartException;
import com.example.aparts.models.Client;
import com.example.aparts.models.autoparts.Chassis;
import com.example.aparts.models.enums.AutoPartCategory;
import com.example.aparts.services.autoparts.ChassisService;
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
@RequestMapping("/chassis")
public final class ChassisUserController {
    private final ChassisService chassisService;
    @Autowired
    public ChassisUserController(ChassisService chassisService){
        this.chassisService = chassisService;
    }

    @Operation(summary = "Show chassis", description = "Endpoint for displaying all available chassis")
    @GetMapping("/selling")
    public String showChassis(Model model, Principal principal){
        List<Chassis> chassis = chassisService.getAllAParts();
        model.addAttribute("chassis", chassis);
        model.addAttribute("user", chassisService.getClientByPrincipal(principal));
        model.addAttribute("types", AutoPartCategory.values());
        return "/autoparts/chassis/chassis";
    }

    @Operation(summary = "Get chassis page", description = "Endpoint for displaying a specific chassis")
    @GetMapping("/{id}")
    public String getChassisPage(
            @PathVariable("id") Long id,
            Principal principal, Model model) throws AutoPartException {
        Client client = chassisService.getClientByPrincipal(principal);
        Chassis chassis = chassisService.getAPartById(id);
        model.addAttribute("user", client);
        model.addAttribute("chassis", chassis);
        return "autoparts/chassis/chassisPage";
    }
}
