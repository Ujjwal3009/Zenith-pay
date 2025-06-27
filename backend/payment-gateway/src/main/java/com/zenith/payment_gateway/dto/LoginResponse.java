package com.zenith.payment_gateway.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.zenith.payment_gateway.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponse {
    private Integer statusCode;
    private String message;
    private String token;
    private UserEntity users;
    private String refreshToken;
    private String expirationTime;

    private List<UserEntity> usersList;
}






