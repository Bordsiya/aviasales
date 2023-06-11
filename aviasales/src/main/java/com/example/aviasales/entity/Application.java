package com.example.aviasales.entity;

import com.example.aviasales.util.enums.ApplicationStatus;
import com.example.aviasales.util.enums.ApplicationType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "applications")
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "applications_id_seq")
    @SequenceGenerator(name = "applications_id_seq", allocationSize = 1)
    @Column(name = "id")
    @JsonView
    private Long applicationId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JsonView
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "application_type", nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonView
    private ApplicationType applicationType;

    @Column(name = "payload", nullable = false)
    @JsonView
    @Type(type = "text")
    private String payload;

    @Column(name = "application_status", nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonView
    private ApplicationStatus applicationStatus;

    @Column(name = "publish_date", nullable = false)
    @JsonView
    private LocalDate publishDate;

    @Column(name = "is_archived", nullable = false)
    @JsonView
    private Boolean isArchived;
}
