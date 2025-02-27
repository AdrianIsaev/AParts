package com.example.aparts.models.autoparts;


import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public final class Engine extends AutoPart{
    @Override
    public String toControllerAutoPartType() {
        return "ENGINE";
    }
}
