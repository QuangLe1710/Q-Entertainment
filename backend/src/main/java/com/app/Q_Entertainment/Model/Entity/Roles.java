package com.app.Q_Entertainment.Model.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
public class Roles extends BaseEntity{

    private int id;

    private String code;

    private String name;

    private String description;

}
