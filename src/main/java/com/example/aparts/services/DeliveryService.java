package com.example.aparts.services;

import com.example.aparts.exceptions.ShoppingCartException;
import com.example.aparts.models.Client;
import com.example.aparts.models.ShoppingCart;
import com.example.aparts.models.enums.Status;
import com.example.aparts.repositories.ShoppingCartRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class DeliveryService {
    private final ShoppingCartRepository shoppingCartRepository;

    @Autowired
    public DeliveryService(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    public List<ShoppingCart> findAllAssembledOrders() {
        List<ShoppingCart> orders = new ArrayList<>();
        orders.addAll(shoppingCartRepository.findAllByStatus(Status.ASSEMBLED));
        orders.addAll(shoppingCartRepository.findAllByStatus(Status.DELIVERING));
        return orders;
    }

    @Transactional
    public void deliveringOrder(Long id, Client carrier) throws ShoppingCartException {
        ShoppingCart order = getOrderFromOptionalOrThrowException(id);
        if (order.getCarrierId() == null || order.getCarrierId() == 0) {
            order.setCarrierId(carrier.getId());
            order.setStatus(Status.DELIVERING);
            shoppingCartRepository.save(order);
        }
    }

    @Transactional
    public void completeOrder(Long id) throws ShoppingCartException {
        ShoppingCart order = getOrderFromOptionalOrThrowException(id);
        order.setStatus(Status.COMPLETED);
        shoppingCartRepository.save(order);
    }

    @Transactional
    public void cancelOrder(Long id) throws ShoppingCartException {
        ShoppingCart order = getOrderFromOptionalOrThrowException(id);
        order.setCarrierId(null);
        order.setStatus(Status.ASSEMBLED);
        shoppingCartRepository.save(order);
    }


    private ShoppingCart getOrderFromOptionalOrThrowException(Long id) throws ShoppingCartException {
        Optional<ShoppingCart> shoppingCartOpt = shoppingCartRepository.findById(id);
        if (shoppingCartOpt.isEmpty()) throw new ShoppingCartException("ShoppingCart not found");
        return shoppingCartOpt.get();
    }
}
