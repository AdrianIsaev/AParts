package com.example.aparts.controllers.products.user;


import com.example.aparts.exceptions.AutoPartException;
import com.example.aparts.models.Client;
import com.example.aparts.models.ShoppingCart;
import com.example.aparts.models.autoparts.AutoPart;
import com.example.aparts.services.autoparts.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shopping")
public final class SCartUserController {
    private final ShoppingCartService shoppingCartService;
    @Autowired
    public SCartUserController(ShoppingCartService shoppingCartService){
        this.shoppingCartService = shoppingCartService;
    }

    @Operation(summary = "Show shopping cart", description = "Endpoint for displaying the shopping cart")
    @GetMapping("/shoppingCart")
    public String showSoppingCart(@AuthenticationPrincipal Client client, Model model){
        ShoppingCart cart = shoppingCartService.getOrCreateShoppingCartByClient(client);
        Map<AutoPart, Integer> items = cart.getItems();
        List<Map.Entry<AutoPart, Integer>> sortedItems = new ArrayList<>(items.entrySet());
        sortedItems.sort(Comparator.comparing(entry -> entry.getKey().getPrice()));

        model.addAttribute("user", client);
        model.addAttribute("items", sortedItems);
        model.addAttribute("cart", cart);

        return "user/shoppingCart";
    }
    @Operation(summary = "Add item to shopping cart", description = "Endpoint for adding an item to the shopping cart")
    @GetMapping("/shoppingCart/addItem/{id}")
    public String addItemToShoppingCart(@PathVariable Long id, @AuthenticationPrincipal Client client, Model model) throws AutoPartException {
        String st = shoppingCartService.addAutoPartToCart(id, client);
        if (!st.equals("Success")) model.addAttribute("errorMessage", st);
        ShoppingCart cart = shoppingCartService.getOrCreateShoppingCartByClient(client);
        Map<AutoPart, Integer> items = cart.getItems();
        List<Map.Entry<AutoPart, Integer>> sortedItems = new ArrayList<>(items.entrySet());
        sortedItems.sort(Comparator.comparing(entry -> entry.getKey().getPrice()));

        model.addAttribute("user", client);
        model.addAttribute("items", sortedItems);
        model.addAttribute("cart", cart);
        return "user/shoppingCart";
    }

    @Operation(summary = "Remove item from shopping cart", description = "Endpoint for removing an item from the shopping cart")
    @GetMapping("/shoppingCart/removeItem/{id}")
    public String removeItemFromShoppingCart(@PathVariable Long id, @AuthenticationPrincipal Client client, Model model) throws AutoPartException {
        shoppingCartService.removeProductFromCart(client, id);
        return "redirect:/shopping/shoppingCart";
    }


    @Operation(summary = "Checkout shopping cart", description = "Endpoint for checking out the shopping cart")
    @PostMapping("/shoppingCart/checkout")
    public String checkoutShoppingCart(@RequestParam(name = "address", required = true) String address,
                                       @AuthenticationPrincipal Client client, Model model) {
        try {
            String st = shoppingCartService.checkoutShoppingCart(client, address);
            if (!st.equals("Success")) {
                model.addAttribute("errorMessage", st);
                ShoppingCart cart = shoppingCartService.getOrCreateShoppingCartByClient(client);
                Map<AutoPart, Integer> items = cart.getItems();
                List<Map.Entry<AutoPart, Integer>> sortedItems = new ArrayList<>(items.entrySet());
                sortedItems.sort(Comparator.comparing(entry -> entry.getKey().getPrice()));
                model.addAttribute("user", client);
                model.addAttribute("items", sortedItems);
                model.addAttribute("cart", cart);
                return "user/shoppingCart";
            } else {
                model.addAttribute("successLabel", "Вы успешно оформили заказ!");
                model.addAttribute("user", client);
                return "user/orderPage";
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Произошла ошибка при оформлении заказа.");
            ShoppingCart cart = shoppingCartService.getOrCreateShoppingCartByClient(client);
            Map<AutoPart, Integer> items = cart.getItems();
            List<Map.Entry<AutoPart, Integer>> sortedItems = new ArrayList<>(items.entrySet());
            sortedItems.sort(Comparator.comparing(entry -> entry.getKey().getPrice()));
            model.addAttribute("user", client);
            model.addAttribute("items", sortedItems);
            model.addAttribute("cart", cart);
            return "user/shoppingCart";
        }
    }

}
