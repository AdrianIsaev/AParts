package com.example.aparts.controllers;

import com.example.aparts.models.Image;
import com.example.aparts.repositories.ImageRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayInputStream;

@Controller
@RequestMapping("/images")
public class ImageController {
    private final ImageRepository imageRepository;
    @Autowired
    public ImageController(ImageRepository imageRepository){
        this.imageRepository = imageRepository;
    }

    @Operation(summary = "Get image by ID", description = "Endpoint for retrieving an image by its ID")
    @GetMapping("/{id}")
    private ResponseEntity<?> getImageById(
            @Parameter(description = "Image ID", required = true) @PathVariable Long id
    ){
        Image image = imageRepository.findById(id).orElse(null);
        if (image == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok()
                .header("fileName", image.getOriginalFileName())
                .contentType(MediaType.valueOf(image.getContentType()))
                .contentLength(image.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
    }
}
