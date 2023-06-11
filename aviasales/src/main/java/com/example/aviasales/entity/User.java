package com.example.aviasales.entity;

import javax.persistence.*;

import com.example.aviasales.util.enums.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private Set<Application> applications;

    @Override
    public String toString() {
        String ans = "User [" + "\n" +
                "user-id=" + userId + "\n" +
                ", email=" + email + "\n" +
                ", password=" + password + "\n" +
                ", role=" + role.name() + "\n" +
                ", applications=%s\n" +
                "]";
        return String.format(ans,
                Objects.requireNonNullElse(applications, "[]"));
    }
}
