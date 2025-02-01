package com.example.aparts.services;

import com.example.aparts.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final ClientRepository clientRepository;
    @Autowired
    public CustomUserDetailsService(ClientRepository clientRepository)
    {
        this.clientRepository = clientRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return clientRepository.findByEmail(username);
    }
}
