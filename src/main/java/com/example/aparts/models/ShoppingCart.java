package com.example.aparts.models;


import com.example.aparts.models.autoparts.AutoPart;
import com.example.aparts.models.enums.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Table
@Entity
@Data
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "client_id", unique = true)
    private Client client;

    @Column(name = "is_active")
    private boolean active;

    @ElementCollection
    @CollectionTable(name = "cart_items", joinColumns = @JoinColumn(name = "cart_id"))
    private Map<AutoPart, Integer> items = new HashMap<>();

    @Column(name = "address")
    private String address;

    @Column(name = "employee_id")
    private Long employeeId = (long) 0;

    @Column(name = "carrier_id")
    private Long carrierId = (long) 0;

    @ElementCollection(targetClass = Status.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "order_status",
            joinColumns = @JoinColumn(name = "order_id"))
    @Enumerated(EnumType.STRING)
    private Set<Status> status = new HashSet<>();

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "updating_date")
    private LocalDateTime updatingDate;

    public void setStatus(Status status) {
        this.status.clear();
        this.status.add(status);
    }

    public boolean isCreated() {
        return status.contains(Status.CREATED);
    }

    public boolean isAccepted() {
        return status.contains(Status.ACCEPTED);
    }

    public boolean isAssembled() {
        return status.contains(Status.ASSEMBLED);
    }

    public boolean isDelivering() {
        return status.contains(Status.DELIVERING);
    }

    public boolean isCompleted() {
        return status.contains(Status.COMPLETED);
    }

    public boolean isCanceled() {
        return status.contains(Status.CANCELED);
    }

    public void addItem(AutoPart autoPart) {
        if (items.containsKey(autoPart)) {
            items.put(autoPart, items.get(autoPart) + 1);
        } else {
            items.put(autoPart, 1);
        }
    }
    public void removeItem(AutoPart autoPart) {
        int currentQuantity = items.getOrDefault(autoPart, 0);
        if (currentQuantity > 1) {
            items.put(autoPart, currentQuantity - 1);
        } else {
            items.remove(autoPart);
        }
    }

    public void deleteItem(AutoPart autoPart) {
        items.remove(autoPart);
    }

    public Double calculateTotal() {
        Double total = 0.0;
        for (Map.Entry<AutoPart, Integer> entry : items.entrySet()) {
            AutoPart autoPart = entry.getKey();
            Integer quantity = entry.getValue();
            total += autoPart.getPrice() * quantity;
        }
        return total;
    }

}
