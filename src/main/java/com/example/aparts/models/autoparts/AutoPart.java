package com.example.aparts.models.autoparts;


import com.example.aparts.models.Image;
import com.example.aparts.models.enums.AutoPartCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "auto_parts")
public abstract class AutoPart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Double price;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "auto_part_category")
    @Enumerated(EnumType.STRING)
    private AutoPartCategory autoPartCategory;

    @Column(name = "images")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
    mappedBy = "autoPart")
    private List<Image> images = new ArrayList<>();

    @Column(name = "preview_image_id")
    private Long previewImageId;

    public void addImageToAutoPart(Image image){
        image.setAutoPart(this);
        images.add(image);
    }
    public boolean hasPreview(){
        return previewImageId != null;
    }
    public abstract String toControllerAutoPartType();
}
