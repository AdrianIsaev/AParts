package com.example.aparts.services;

import com.example.aparts.exceptions.AutoPartException;
import com.example.aparts.exceptions.ClientException;
import com.example.aparts.exceptions.ShoppingCartException;
import com.example.aparts.models.Client;
import com.example.aparts.models.ShoppingCart;
import com.example.aparts.models.autoparts.AutoPart;
import com.example.aparts.models.enums.Role;
import com.example.aparts.models.enums.Status;
import com.example.aparts.repositories.ClientRepository;
import com.example.aparts.repositories.ShoppingCartRepository;
import com.example.aparts.repositories.autoparts.AutoPartRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.print.attribute.standard.PagesPerMinute;
import java.security.Principal;
import java.util.*;

@Service
@Slf4j
public class AdminService {
    private final ClientRepository clientRepository;
    private final AutoPartRepository autoPartRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    @Autowired
    public AdminService(ClientRepository clientRepository, AutoPartRepository autoPartRepository, ShoppingCartRepository shoppingCartRepository) {
        this.autoPartRepository = autoPartRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.clientRepository = clientRepository;
    }

    @Transactional(readOnly = true)
    public Client getClientByPrincipal(Principal principal) {
        if (principal == null) return new Client();
        return clientRepository.findByEmail(principal.getName());
    }

    @Transactional(readOnly = true)
    public Client getClientById(Long id) throws ClientException {
        return getClientFromOptionalOrThrowException(id);
    }

    @Transactional(readOnly = true)
    public List<Client> findAllClients(String email) {
        List<Client> clients = new ArrayList<>();
        if (email != null) {
            clients.add(clientRepository.findByEmail(email));
        } else {
            clients.addAll(clientRepository.findAll());
        }
        return clients;
    }

    @Transactional
    public void banUser(Long id) throws ClientException {
        Client client = getClientFromOptionalOrThrowException(id);
        if (client.isActive()) {
            client.setActive(false);
            log.info("Ban user with id = {}; email = {}", client.getId(), client.getEmail());
        } else {
            client.setActive(true);
            log.info("Unban user with id = {}; email = {}", client.getId(), client.getEmail());
        }
        clientRepository.save(client);
    }

    @Transactional
    public void changeUserRole(Client user, String role) {
        Set<Role> roles = new HashSet<>();
        if (role != null && !role.isEmpty()) {
            roles.add(Role.valueOf(role));
        }
        user.setRoles(roles);
        clientRepository.save(user);
    }

    @Transactional
    public void deleteAutoPart(Long id) throws AutoPartException {
        autoPartRepository.delete(getAutoPartFromOptionalOrThrowException(id));
    }

    @Transactional(readOnly = true)
    public List<AutoPart> getAllProducts() {
        return autoPartRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Client> getUsersByEmail(String email, Pageable pageable) {
        if (email != null && !email.isEmpty()) {
            return clientRepository.findByEmailLike("%" + email + "%", pageable);
        } else {
            return clientRepository.findAll(pageable);
        }
    }

    @Transactional(readOnly = true)
    public Page<ShoppingCart> getOrdersByAddress(String address, Pageable pageable) {
        if (address != null && address.isEmpty()) {
            return shoppingCartRepository.findAllByAddressLikeAndActive("%" + address + "%", false, pageable);
        } else {
            return shoppingCartRepository.findAllByActive(false, pageable);
        }
    }

    @Transactional(readOnly = true)
    public ShoppingCart getOrderById(Long id) throws ShoppingCartException {
        return getShoppingCartFromOptionalOrThrowException(id);
    }

    @Transactional
    public void changeOrderStatus(Long id, String status) throws ShoppingCartException {
        if (status != null && !status.isEmpty()) {
            ShoppingCart order = getShoppingCartFromOptionalOrThrowException(id);
            order.setStatus(Status.valueOf(status));
            shoppingCartRepository.save(order);
        }
    }

    @Transactional(readOnly = true)
    public List<ShoppingCart> getHistory(Client client) {
        return shoppingCartRepository.findAllByActiveAndClient(false, client);
    }

    private ShoppingCart getShoppingCartFromOptionalOrThrowException(Long id) throws ShoppingCartException {
        Optional<ShoppingCart> optional = shoppingCartRepository.findById(id);
        if (optional.isEmpty()) throw new ShoppingCartException("ShoppingCart not found");
        return optional.get();
    }

    private AutoPart getAutoPartFromOptionalOrThrowException(Long id) throws AutoPartException {
        Optional<AutoPart> optional = autoPartRepository.findById(id);
        if (optional.isEmpty()) throw new AutoPartException("AutoPart not found");
        return optional.get();
    }

    private Client getClientFromOptionalOrThrowException(Long id) throws ClientException {
        Optional<Client> optional = clientRepository.findById(id);
        if (optional.isEmpty()) throw new ClientException("Client not found");
        return optional.get();
    }
}
