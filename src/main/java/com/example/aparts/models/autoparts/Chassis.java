package com.example.aparts.models.autoparts;


import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public final class Chassis extends AutoPart{
    @Override
    public String toControllerAutoPartType() {
        return "CHASSIS";
    }
}
