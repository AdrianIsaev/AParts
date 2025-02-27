package com.example.aparts.services;

import com.example.aparts.models.Client;
import com.example.aparts.models.ShoppingCart;
import com.example.aparts.models.autoparts.AutoPart;
import com.example.aparts.models.enums.Role;
import com.example.aparts.repositories.ClientRepository;
import com.example.aparts.repositories.ShoppingCartRepository;
import com.example.aparts.repositories.autoparts.AutoPartRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@Slf4j
public class ClientService{
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final AutoPartRepository autoPartRepository;
   private final ShoppingCartRepository shoppingCartRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository, PasswordEncoder passwordEncoder, AutoPartRepository autoPartRepository, ShoppingCartRepository shoppingCartRepository){
        this.clientRepository = clientRepository;
        this.shoppingCartRepository =shoppingCartRepository;
        this.autoPartRepository = autoPartRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional
    public boolean createClient(Client client){
        String email = client.getEmail();
        if (clientRepository.findByEmail(email) != null) return false;
        client.setActive(true);
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        client.getRoles().add(Role.ROLE_USER);
        log.info("Сохранен новый клиент с email: " + email);
        clientRepository.save(client);
        return true;
    }

    @Transactional
    public boolean changePass(Client client){
        Client currentClient = clientRepository.findByEmail(client.getEmail());
        currentClient.setPassword(passwordEncoder.encode(client.getPassword()));
        clientRepository.save(currentClient);
        return true;
    }
    @Transactional
    public Client getClientByPrincipal(Principal principal){
        if (principal == null) return new Client();
        return clientRepository.findByEmail(principal.getName());
    }
    @Transactional
    public List<AutoPart> getAllAutoParts(){
        return autoPartRepository.findAll();
    }

    @Transactional
    public List<ShoppingCart> getHistory(Client client) {
        return shoppingCartRepository.findAllByActiveAndClient(false, client);
    }

}
