package com.example.aparts.controllers.products.employee;

import com.example.aparts.exceptions.ShoppingCartException;
import com.example.aparts.models.Client;
import com.example.aparts.services.EmployeeService;
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
@RequestMapping("/employee")
@PreAuthorize("hasAuthority('ROLE_EMPLOYEE')")
public final class EmployeeController {
    private final EmployeeService employeeService;
    @Autowired
    public EmployeeController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

    @Operation(summary = "Show employee panel", description = "Endpoint for displaying employee panel")
    @GetMapping("/panel")
    public String showPanel(
            @Parameter(description = "Authenticated employee", required = true) @AuthenticationPrincipal Client client,
            Model model) {
        model.addAttribute("user", client);
        model.addAttribute("orders", employeeService.findAllCreatedOrders());
        return "employee/employeePanel";
    }

    @Operation(summary = "Accept order", description = "Endpoint for accepting an order")
    @GetMapping("/order/accept/{id}")
    public String acceptOrder(
            @Parameter(description = "Order ID", required = true) @PathVariable("id") Long id,
            @AuthenticationPrincipal Client client) throws ShoppingCartException {
        employeeService.acceptOrder(id, client);
        return "redirect:/employee/panel";
    }

    @Operation(summary = "Mark order as cooked", description = "Endpoint for marking an order as cooked")
    @GetMapping("/order/assembled/{id}")
    public String cookedOrder(
            @Parameter(description = "Order ID", required = true) @PathVariable("id") Long id) throws ShoppingCartException {
        employeeService.assembledOrder(id);
        return "redirect:/employee/panel";
    }

    @Operation(summary = "Cancel order", description = "Endpoint for cancelling an order")
    @GetMapping("/order/cancel/{id}")
    public String cancelOrder(
            @Parameter(description = "Order ID", required = true) @PathVariable("id") Long id) throws ShoppingCartException {
        employeeService.cancelOrder(id);
        return "redirect:/employee/panel";
    }
}
