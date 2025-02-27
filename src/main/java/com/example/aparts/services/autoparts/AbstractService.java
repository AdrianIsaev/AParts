package com.example.aparts.services.autoparts;


import com.example.aparts.exceptions.AutoPartException;
import com.example.aparts.exceptions.ChassisException;
import com.example.aparts.models.Client;
import com.example.aparts.models.Image;
import com.example.aparts.models.autoparts.AutoPart;
import com.example.aparts.models.enums.AutoPartCategory;
import com.example.aparts.repositories.ClientRepository;
import com.example.aparts.repositories.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractService<APart extends AutoPart, SpecializedRepository extends JpaRepository<APart, Long>>{

    protected final ClientRepository clientRepository;
    protected final ImageRepository imageRepository;
    protected final SpecializedRepository specializedRepository;
    @Transactional
    public Client getClientByPrincipal(Principal principal){
        if (principal == null) return new Client();
        return clientRepository.findByEmail(principal.getName());
    }

    public String validation(APart aPart){
        if (aPart.getPrice() != null && aPart.getPrice() > 0 &&
        aPart.getName() != null && !aPart.getName().isEmpty()){
            return "Success";
        }
        else{
            if (aPart.getPrice() == null) return "Укажите цену!";
            else if (aPart.getPrice()<0) return "Укажите корректную цену";
            else if (aPart.getName() == null || aPart.getName().isEmpty()) return "Напишите название запчасти";
        }
        return "Error";
    }

    @Transactional
    public String createAPart(Principal principal, APart aPart, MultipartFile file1) throws IOException{
        switch (aPart.getClass().getSimpleName()){
            case "Chassis":
                aPart.setAutoPartCategory(AutoPartCategory.CHASSIS);
                break;
            case "Engine":
                aPart.setAutoPartCategory(AutoPartCategory.ENGINE);
                break;
            case "Fasteners":
                aPart.setAutoPartCategory(AutoPartCategory.FASTENERS);
                break;
            case "Other":
                aPart.setAutoPartCategory(AutoPartCategory.OTHER);
                break;
            case "Suspension":
                aPart.setAutoPartCategory(AutoPartCategory.SUSPENSION);
                break;
            default:
                log.error("Неизвестный тип запчасти: {}", aPart.getClass().getName());
                return "Неизвестный тип запчасти!";
        }

        String validation = validation(aPart);
        if (validation.equals("Success")){
            Image image1;
            if (file1.getSize() != 0){
                image1 = toImageEntity(file1);
                image1.setPreviewImage(true);
                imageRepository.save(image1);
                aPart.setPreviewImageId(image1.getId());
                aPart.addImageToAutoPart(image1);
            }
            specializedRepository.save(aPart);
        }
        return validation;
    }
    @Transactional
    public Image toImageEntity(MultipartFile file) throws IOException{
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }

    @Transactional
    public APart getAPartById(Long id) throws AutoPartException {
        Optional<APart> aPart = specializedRepository.findById(id);
        if (aPart.isPresent()) return aPart.get();
        else throw new AutoPartException("AutoPart not found");
    }

    @Transactional
    public String updateAPart(Long id, APart aPart, MultipartFile file1) throws IOException{
        String validation = validation(aPart);
        if (validation.equals("Success")){
            APart originalPart = specializedRepository.findById(id).orElse(null);
            Image image1;
            if (file1.getSize()!= 0){
                if (originalPart!=null && originalPart.hasPreview()){
                    imageRepository.deleteById(originalPart.getPreviewImageId());
                }
                image1 = toImageEntity(file1);
                image1.setPreviewImage(true);
                imageRepository.save(image1);
                originalPart.setPreviewImageId(image1.getId());
                originalPart.addImageToAutoPart(image1);
            }
            originalPart.setName(aPart.getName());
            originalPart.setPrice(aPart.getPrice());
            originalPart.setQuantity(aPart.getQuantity());

            specializedRepository.save(originalPart);
        }
        return validation;
    }

    @Transactional
    public void deleteAPart(Long id){
        APart aPart = specializedRepository.findById(id).orElse(null);
        if (aPart!=null) {
            aPart.setQuantity(0);
            specializedRepository.save(aPart);
        }
        else {
            log.error("репозиторий вернул null объект");
        }
    }
    @Transactional
    public List<APart> getAllAParts(){
        return specializedRepository.findAll();
    }
}
