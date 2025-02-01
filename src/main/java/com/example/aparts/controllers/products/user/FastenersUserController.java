package com.example.aparts.controllers.products.user;


import com.example.aparts.exceptions.AutoPartException;
import com.example.aparts.models.Client;
import com.example.aparts.models.autoparts.Engine;
import com.example.aparts.models.autoparts.Fasteners;
import com.example.aparts.models.enums.AutoPartCategory;
import com.example.aparts.services.autoparts.FastenersService;
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
@RequestMapping("/fasteners")
public class FastenersUserController {
    private final FastenersService fastenersService;

    @Autowired
    public FastenersUserController(FastenersService fastenersService){
        this.fastenersService = fastenersService;
    }

    @Operation(summary = "Show fasteners", description = "Endpoint for displaing all available fasteners")
    @GetMapping("/selling")
    public String showFasteners(Model model, Principal principal){
        List<Fasteners> fasteners = fastenersService.getAllAParts();
        model.addAttribute("fasteners", fasteners);
        model.addAttribute("user", fastenersService.getClientByPrincipal(principal));
        model.addAttribute("types", AutoPartCategory.values());
        return "/autoparts/fasteners/fasteners";
    }


    @Operation(summary = "Get fasteners page", description = "Endpoint for displaying a specific fasteners")
    @GetMapping("/{id}")
    public String getFastenersPage(
            @PathVariable("id") Long id,
            Principal principal, Model model) throws AutoPartException {
        Client client = fastenersService.getClientByPrincipal(principal);
        Fasteners fasteners = fastenersService.getAPartById(id);
        model.addAttribute("user", client);
        model.addAttribute("fasteners", fasteners);
        return "autoparts/fasteners/fastenersPage";
    }
}
