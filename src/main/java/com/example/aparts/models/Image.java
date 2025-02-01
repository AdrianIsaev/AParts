package com.example.aparts.models;

import com.example.aparts.models.autoparts.AutoPart;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "original_file_name")
    private String originalFileName;

    @Column(name = "size")
    private Long size;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "is_preview_image")
    private boolean isPreviewImage;

    @Lob
    @Column(name = "bytes", columnDefinition = "BYTEA")
    private byte[] bytes;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private AutoPart autoPart;

}
