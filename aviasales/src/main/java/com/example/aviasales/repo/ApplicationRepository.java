package com.example.aviasales.repo;

import com.example.aviasales.entity.Application;
import com.example.aviasales.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findAllByUser(User user);
}
