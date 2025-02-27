package com.example.aparts.controllers.products.delivery;

import com.example.aparts.exceptions.ShoppingCartException;
import com.example.aparts.models.Client;
import com.example.aparts.services.DeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@PreAuthorize("hasAuthority('ROLE_DELIVERYMAN')")
@RequestMapping("/delivery")
public class DeliveryController {
    private final DeliveryService deliveryService;
    @Autowired
    public DeliveryController(DeliveryService deliveryService){
        this.deliveryService = deliveryService;
    }

    @Operation(summary = "Show delivery panel", description = "Endpoint for displaying delivery panel")
    @GetMapping("/panel")
    public String showPanel(
            @Parameter(description = "Authenticated deliveryman", required = true) @AuthenticationPrincipal Client client,
            Model model) {
        model.addAttribute("user", client);
        model.addAttribute("orders", deliveryService.findAllAssembledOrders());
        return "delivery/deliveryPanel";
    }

    @Operation(summary = "Mark order as delivering", description = "Endpoint for marking an order as delivering")
    @GetMapping("/order/delivering/{id}")
    public String deliveringOrder(
            @Parameter(description = "Order ID", required = true) @PathVariable("id") Long id,
            @AuthenticationPrincipal Client client) throws ShoppingCartException {
        deliveryService.deliveringOrder(id, client);
        return "redirect:/delivery/panel";
    }

    @Operation(summary = "Mark order as complete", description = "Endpoint for marking an order as complete")
    @GetMapping("/order/complete/{id}")
    public String completeOrder(
            @Parameter(description = "Order ID", required = true) @PathVariable("id") Long id) throws ShoppingCartException {
        deliveryService.completeOrder(id);
        return "redirect:/delivery/panel";
    }

    @Operation(summary = "Cancel order", description = "Endpoint for cancelling an order")
    @GetMapping("/order/cancel/{id}")
    public String cancelOrder(
            @Parameter(description = "Order ID", required = true) @PathVariable("id") Long id) throws ShoppingCartException {
        deliveryService.cancelOrder(id);
        return "redirect:/delivery/panel";
    }
}
