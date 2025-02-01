package com.example.aparts.services.autoparts;


import com.example.aparts.models.autoparts.Engine;
import com.example.aparts.models.autoparts.Suspension;
import com.example.aparts.repositories.ClientRepository;
import com.example.aparts.repositories.ImageRepository;
import com.example.aparts.repositories.autoparts.SuspensionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class SuspensionService extends AbstractService<Suspension, SuspensionRepository> {
    @Autowired
    public SuspensionService(ClientRepository clientRepository, ImageRepository imageRepository, SuspensionRepository suspensionRepository) {
        super(clientRepository, imageRepository, suspensionRepository);
    }
    @Transactional(readOnly = true)
    public Page<Suspension> getChassisByName(String name, Pageable pageable){
        if (name!= null && !name.isEmpty()){
            return specializedRepository.findByNameLike("%" + name + "%", pageable);
        }
        else{
            return specializedRepository.findAll(pageable);
        }
    }
}
