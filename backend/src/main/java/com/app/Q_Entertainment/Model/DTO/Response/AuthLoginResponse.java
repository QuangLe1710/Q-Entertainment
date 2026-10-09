package com.app.Q_Entertainment.Model.DTO.Response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthLoginResponse {

    private String accessToken;

    private String refreshToken;

}
