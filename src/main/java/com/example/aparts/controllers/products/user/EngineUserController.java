package com.example.aparts.controllers.products.user;

import com.example.aparts.exceptions.AutoPartException;
import com.example.aparts.models.Client;
import com.example.aparts.models.autoparts.Chassis;
import com.example.aparts.models.autoparts.Engine;
import com.example.aparts.models.enums.AutoPartCategory;
import com.example.aparts.services.autoparts.EngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/engine")
public final class EngineUserController {

    private final EngineService engineService;

    @Autowired
    public EngineUserController(EngineService engineService){
        this.engineService = engineService;
    }

    @Operation(summary = "Show engines", description = "Endpoint for displaying all available engines")
    @GetMapping("/selling")
    public String showEngines(Model model, Principal principal){
        List<Engine> engines = engineService.getAllAParts();
        model.addAttribute("user", engineService.getClientByPrincipal(principal));
        model.addAttribute("engines", engines);
        model.addAttribute("types", AutoPartCategory.values());
        return "/autoparts/engine/engine";
    }

    @Operation(summary = "Get engine page", description = "Endpoint for displaying a specific engine")
    @GetMapping("/{id}")
    public String getEnginePage(
            @PathVariable("id") Long id,
            Principal principal, Model model) throws AutoPartException {
        Client client = engineService.getClientByPrincipal(principal);
        Engine engine = engineService.getAPartById(id);
        model.addAttribute("user", client);
        model.addAttribute("engine", engine);
        return "autoparts/engine/enginePage";
    }
}
