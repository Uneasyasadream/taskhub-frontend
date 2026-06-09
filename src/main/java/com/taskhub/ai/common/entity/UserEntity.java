package com.taskhub.ai.common.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(
        name = "sys_user",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_username_deleted", columnNames = {"username", "is_deleted"}),
                @UniqueConstraint(name = "uk_phone_deleted", columnNames = {"phone", "is_deleted"})
        }
)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class UserEntity extends BaseEntity {

    @Column(name = "username", nullable = false, length = 64, unique = true)
    private String username;

    @Column(name = "password", nullable = false, length = 128)
    private String password;

    @Column(name = "nickname", length = 64)
    private String nickname;

    @Column(name = "phone", length = 11)
    private String phone;

    @Column(name = "email", length = 128)
    private String email;

    @Column(name = "avatar", length = 512)
    private String avatar;

    @Column(name = "status", nullable = false)
    private Integer status = 1;

    @Column(name = "is_admin", nullable = false)
    private Integer isAdmin = 0;
}