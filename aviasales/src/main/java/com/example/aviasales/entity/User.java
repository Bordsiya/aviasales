package com.example.aviasales.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.example.aviasales.util.enums.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    @SequenceGenerator(name = "users_id_seq", allocationSize = 1)
    @JsonView
    @Column(name = "id")
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
                "user-id=" + userId + "\n" +
                ", email=" + email + "\n" +
                ", password=" + password + "\n" +
                ", role=%s" + role.name() + "\n" +
                "]";
    }
}
