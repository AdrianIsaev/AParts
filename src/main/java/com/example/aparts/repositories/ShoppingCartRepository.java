package com.example.aparts.repositories;

import com.example.aparts.models.Client;
import com.example.aparts.models.ShoppingCart;
import com.example.aparts.models.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.*;
import java.util.List;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    ShoppingCart getShoppingCartByClientAndActive(Client client, Boolean active);
    List<ShoppingCart> findAllByStatus(Status status);
    List<ShoppingCart> findAllByActiveAndClient(boolean active, Client client);
    Page<ShoppingCart> findAllByAddressLikeAndActive(String address, boolean active, Pageable pageable);
    Page<ShoppingCart> findAllByActive(boolean active, Pageable pageable);
}
