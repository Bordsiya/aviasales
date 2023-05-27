package com.example.aviasales.entity;

import com.example.aviasales.util.enums.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonView
    private Long userId;
    @Column(name = "email", nullable = false)
    @JsonView
    private String email;
    @Column(name = "password")
    @JsonView
    private String password;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    @JsonView
    private RoleType role;

    @Override
    public String toString() {
        return "User [" + "\n" +
                "user-id=" + userId+ "\n" +
                ", email=" + email + "\n" +
                ", password=" + password + "\n" +
                ", role=%s" + role.name() + "\n" +
                "]";
    }
}
