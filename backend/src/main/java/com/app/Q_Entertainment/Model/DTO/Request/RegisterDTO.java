package com.app.Q_Entertainment.Model.DTO.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDTO {

    private String username;

    private String password;

    private String email;

    private String fullName;

    private String avatarUrl;

}
