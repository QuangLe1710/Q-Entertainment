package com.app.Q_Entertainment.Repository;

import com.app.Q_Entertainment.Model.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Integer, User> {
    Optional<User> findByEmail(String email);
}
