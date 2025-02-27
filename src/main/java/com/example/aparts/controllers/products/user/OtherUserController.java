package com.example.aparts.controllers.products.user;
import com.example.aparts.exceptions.AutoPartException;
import com.example.aparts.models.Client;
import com.example.aparts.models.autoparts.Fasteners;
import com.example.aparts.models.autoparts.Other;
import com.example.aparts.models.enums.AutoPartCategory;
import com.example.aparts.services.autoparts.OtherService;
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
@RequestMapping("/other")
public final class OtherUserController {
    private final OtherService otherService;
    @Autowired
    public OtherUserController(OtherService otherService){
        this.otherService = otherService;
    }
    @Operation(summary = "Show other", description = "Endpoint for displaing all available other")
    @GetMapping("/selling")
    public String showOther(Model model, Principal principal){
        List<Other> others = otherService.getAllAParts();
        model.addAttribute("others", others);
        model.addAttribute("user", otherService.getClientByPrincipal(principal));
        model.addAttribute("types", AutoPartCategory.values());
        return "/autoparts/other/other";
    }

    @Operation(summary = "Get other page", description = "Endpoint for displaying a specific other")
    @GetMapping("/{id}")
    public String getOtherPage(
            @PathVariable("id") Long id,
            Principal principal, Model model) throws AutoPartException {
        Client client = otherService.getClientByPrincipal(principal);
        Other other = otherService.getAPartById(id);
        model.addAttribute("user", client);
        model.addAttribute("other", other);
        return "autoparts/other/otherPage";
    }
}
