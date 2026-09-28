package com.app.Q_Entertainment.Model.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@Table(name = "user")
public class User extends BaseEntity{

    @Id
    private int id;

    private String username;

    private String email;

    private String passwordHash;

    private String fullName;

    private String avatarUrl;

    private boolean isActive;

    private boolean isLocked;

}
