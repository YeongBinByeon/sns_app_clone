package com.example.sns.controller.response;

import com.example.sns.model.User;
import com.example.sns.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserLoginResponse {

    private String token;
}