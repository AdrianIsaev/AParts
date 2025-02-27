package com.example.aparts.services;


import com.example.aparts.exceptions.ShoppingCartException;
import com.example.aparts.models.Client;
import com.example.aparts.models.ShoppingCart;
import com.example.aparts.models.enums.Status;
import com.example.aparts.repositories.ClientRepository;
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
public class EmployeeService {
    private final ClientRepository clientRepository;

    private final ShoppingCartRepository shoppingCartRepository;

    @Autowired
    public EmployeeService(ClientRepository clientRepository, ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.clientRepository = clientRepository;
    }
    @Transactional
    public List<ShoppingCart> findAllCreatedOrders(){
        List<ShoppingCart> orders = new ArrayList<>();
        orders.addAll(shoppingCartRepository.findAllByStatus(Status.ACCEPTED));
        orders.addAll(shoppingCartRepository.findAllByStatus(Status.CREATED));
        return orders;
    }

    @Transactional
    public void acceptOrder(Long id, Client employee) throws ShoppingCartException {
        ShoppingCart order = getOrderFromOptionalOrThrowException(id);
        if (order.getEmployeeId() == null || order.getEmployeeId() == 0) {
            order.setEmployeeId(employee.getId());
            order.setStatus(Status.ACCEPTED);
            shoppingCartRepository.save(order);
        }
    }
    @Transactional
    public void cancelOrder(Long id) throws ShoppingCartException{
        ShoppingCart order = getOrderFromOptionalOrThrowException(id);
        order.setEmployeeId(null);
        order.setStatus(Status.CANCELED);
        shoppingCartRepository.save(order);
    }
    @Transactional
    public void assembledOrder(Long id) throws ShoppingCartException{
        ShoppingCart order = getOrderFromOptionalOrThrowException(id);
        order.setStatus(Status.ASSEMBLED);
        shoppingCartRepository.save(order);
    }
    @Transactional
    private ShoppingCart getOrderFromOptionalOrThrowException(Long id) throws ShoppingCartException {
        Optional<ShoppingCart> shoppingCartOpt = shoppingCartRepository.findById(id);
        if (shoppingCartOpt.isEmpty()) throw new ShoppingCartException("ShoppingCart not found");
        return shoppingCartOpt.get();
    }
}
