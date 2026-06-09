package com.taskhub.ai.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserVO {
    private String id;
    private String username;
    private String nickname;
    private String phone;
    private String email;
    private String avatar;
    private Integer status;
    private Integer isAdmin;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}