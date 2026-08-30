package com.healthcaremanagement.auth.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    public String full_name;
    public String email;
    public String phone_number;
    public String password;
    public Boolean agree_tcp;
}
