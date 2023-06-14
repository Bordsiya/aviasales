package com.example.aviasales.entity;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.example.aviasales.util.enums.MailRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mail_request")
public class MailRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mail_request_id_seq")
    @SequenceGenerator(name = "mail_request_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private MailRequestStatus status;

    @Column(name = "payload")
    private String payload;

    @Column(name = "created_at")
    private Instant createdAt;
}
