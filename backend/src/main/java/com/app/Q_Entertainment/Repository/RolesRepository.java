package com.app.Q_Entertainment.Repository;

import com.app.Q_Entertainment.Model.Entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository extends CrudRepository<Roles, Integer>, JpaRepository<Roles, Integer> {

    Optional<Roles> findByCode(String code);
}
