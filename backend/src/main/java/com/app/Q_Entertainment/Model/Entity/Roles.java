package com.app.Q_Entertainment.Model.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import jakarta.persistence.Id;

@Entity
@Getter
@Setter
@Builder
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
public class Roles extends BaseEntity{

    @Id
    private int id;

    private String code;

    private String name;

    private String description;

}
