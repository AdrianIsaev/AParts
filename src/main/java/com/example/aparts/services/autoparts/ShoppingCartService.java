package com.example.aparts.services.autoparts;


import com.example.aparts.exceptions.AutoPartException;
import com.example.aparts.models.Client;
import com.example.aparts.models.ShoppingCart;
import com.example.aparts.models.autoparts.AutoPart;
import com.example.aparts.models.enums.Status;
import com.example.aparts.repositories.ShoppingCartRepository;
import com.example.aparts.repositories.autoparts.AutoPartRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final AutoPartRepository autoPartRepository;

    @Autowired
    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, AutoPartRepository autoPartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.autoPartRepository = autoPartRepository;
    }

    public AutoPart getAutoPartById(Long id) throws AutoPartException {
        Optional<AutoPart> opt = autoPartRepository.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        }
        throw new AutoPartException("AutoPart not found");
    }

    public ShoppingCart getOrCreateShoppingCartByClient(Client client) {
        ShoppingCart shoppingCart = shoppingCartRepository.getShoppingCartByClientAndActive(client, true);
        if (shoppingCart == null) {
            shoppingCart = createDefaultShoppingCart(client);
            shoppingCartRepository.save(shoppingCart);
        }
        return shoppingCart;
    }

    private ShoppingCart createDefaultShoppingCart(Client client) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setClient(client);
        shoppingCart.setActive(true);
        shoppingCart.setCreationDate(LocalDateTime.now());
        return shoppingCart;
    }

    @Transactional
    public String addAutoPartToCart(Long id, Client client) throws AutoPartException {
        ShoppingCart shoppingCart = shoppingCartRepository.getShoppingCartByClientAndActive(client, true);
        if (shoppingCart == null) {
            shoppingCart = createDefaultShoppingCart(client);
        }

        AutoPart autoPart = getAutoPartById(id);

        if (shoppingCart.getItems().get(autoPart) != null && autoPart.getQuantity() > shoppingCart.getItems().get(autoPart))
            shoppingCart.addItem(getAutoPartById(id));

        else if (shoppingCart.getItems().get(autoPart) == null) shoppingCart.addItem(getAutoPartById(id));

        else return "Вы не можете добавить этого товара больше";

        shoppingCartRepository.save(shoppingCart);
        return "success";
    }

    public void removeProductFromCart(Client client, Long id) throws AutoPartException {

        ShoppingCart shoppingCart = shoppingCartRepository.getShoppingCartByClientAndActive(client, true);

        if (shoppingCart == null) shoppingCart = createDefaultShoppingCart(client);
        shoppingCart.removeItem(getAutoPartById(id));
        shoppingCartRepository.save(shoppingCart);
    }
    @Transactional
    public String checkoutShoppingCart(Client client, String address){
        ShoppingCart shoppingCart = getOrCreateShoppingCartByClient(client);
        for (Map.Entry<AutoPart, Integer> entry : shoppingCart.getItems().entrySet()){
            AutoPart autoPart = entry.getKey();
            Integer quantity = entry.getValue();

            if (autoPart.getQuantity() < quantity){
                return "К сожалению, Вы не можете оформить заказ.\n" +
                        "Товара " + autoPart.getAutoPartCategory() + " "
                        + autoPart.getName() + " в таком количестве больше нет.\n" +
                        "Осталось: " + autoPart.getQuantity();
            }
        }

        for (Map.Entry<AutoPart, Integer> entry : shoppingCart.getItems().entrySet()){
            AutoPart autoPart = entry.getKey();
            Integer quantity = entry.getValue();

            autoPart.setQuantity(autoPart.getQuantity() - 1);
            autoPartRepository.save(autoPart);
        }

        // Заказ совершен (прошлая корзина - совершенный заказ)
        shoppingCart.setActive(false);
        shoppingCart.setAddress(address);
        shoppingCart.setUpdatingDate(LocalDateTime.now());
        shoppingCart.setStatus(Status.CREATED);

        shoppingCart = new ShoppingCart();
        shoppingCart.setClient(client);
        shoppingCart.setActive(true);
        shoppingCartRepository.save(shoppingCart);

        return "Success";
    }
}
