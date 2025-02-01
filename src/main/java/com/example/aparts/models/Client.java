package com.example.aparts.models;


import com.example.aparts.models.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "clients")
public class Client implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    @NotBlank(message = "Почта не может быть пустой")
    @Email(message = "Некорректный email")
    private String email;

    @Column(name = "phone_number")
    @NotNull(message = "Номер телефона не может быть пустым")
    @NotBlank
    @NotEmpty
    @Pattern(regexp = "(^$|[0-9]{11})", message = "Некорректный номер телефона")
    private String phoneNumber;

    @Column(name = "name")
    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 2, max = 30, message = "Имя должно содержать не менее 2, не более 30 символов")
    private String name;

    @Column(name = "is_active")
    private boolean isActive;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "image_id")
    private Image avatar;

    @Column(name = "password")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
            message = "Пароль должен содержать минимум одну цифру," +
                    " одну строчную и одну прописную букву, специальный" +
                    " символ и быть не менее 8 символов")
    // regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
    // минимум:
    // одну цифру,
    // одну строчную и одну прописную букву,
    // один специальный символ (@, #, $, %, ^, &, +, =, !)
    // быть не менее 8 символов
    private String password;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "updating_date")
    private LocalDateTime updatingDate;

    @PrePersist
    private void init() {
        creationDate = LocalDateTime.now();
    }

    @PreUpdate
    private void update() {
        updatingDate = LocalDateTime.now();
    }

    public boolean isAdmin() {
        return roles.contains(Role.ROLE_ADMIN);
    }

    public boolean isEmployee() {
        return roles.contains(Role.ROLE_EMPLOYEE);
    }

    public boolean isDeliveryman() {
        return roles.contains(Role.ROLE_DELIVERYMAN);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
