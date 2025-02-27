package com.example.aparts.models.autoparts;


import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public final class Fasteners extends AutoPart{
    @Override
    public String toControllerAutoPartType() {
        return "FASTENERS";
    }
}
