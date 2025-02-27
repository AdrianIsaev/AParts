package com.example.aparts.models.autoparts;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public final class Other extends AutoPart{
    @Override
    public String toControllerAutoPartType() {
        return "OTHER";
    }
}
