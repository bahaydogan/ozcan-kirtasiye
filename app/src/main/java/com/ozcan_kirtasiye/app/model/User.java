package com.ozcan_kirtasiye.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ozcan_kirtasiye.app.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table(name = "Users", uniqueConstraints = @UniqueConstraint (columnNames = {"email"}))
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long  id;

    @Column
    @NotBlank
    private String name;

    @Column
    @Email
    @NotBlank
    private String email;

    @Column
    @NotBlank
    private String password;

    @Column(nullable = false)
    @JsonIgnore
    private LocalDateTime createTime;

    @JsonIgnore
    boolean active = false;

    @JsonIgnore
    String activationToken;

    @Column
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;  // Default

    @Column
    String address;

}
