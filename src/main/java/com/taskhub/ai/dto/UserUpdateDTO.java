package com.taskhub.ai.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateDTO {
    @Size(max = 64)
    private String nickname;
    @Email
    @Size(max = 128)
    private String email;
    @Size(max = 11)
    private String phone;
    private String avatar;
}