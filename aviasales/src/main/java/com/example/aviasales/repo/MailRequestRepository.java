package com.example.aviasales.repo;

import java.time.Instant;
import java.util.List;

import com.example.aviasales.entity.MailRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailRequestRepository extends JpaRepository<MailRequest, Long> {
    List<MailRequest> findAllByCreatedAtGreaterThan(Instant createdAt);
}
