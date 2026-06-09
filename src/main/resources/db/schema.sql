-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS `taskhub_ai`
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE `taskhub_ai`;

-- 用户表
CREATE TABLE IF NOT EXISTS `sys_user` (
                                          `id` BIGINT NOT NULL COMMENT '用户ID（雪花算法）',
                                          `username` VARCHAR(64) NOT NULL COMMENT '用户名',
    `password` VARCHAR(128) NOT NULL COMMENT '加密密码',
    `nickname` VARCHAR(64) DEFAULT NULL COMMENT '昵称',
    `phone` VARCHAR(11) DEFAULT NULL COMMENT '手机号',
    `email` VARCHAR(128) DEFAULT NULL COMMENT '邮箱',
    `avatar` VARCHAR(512) DEFAULT NULL COMMENT '头像URL',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1正常，0禁用',
    `is_admin` TINYINT NOT NULL DEFAULT 0 COMMENT '是否超级管理员：1是，0否',
    `created_by` BIGINT DEFAULT NULL COMMENT '创建人ID',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by` BIGINT DEFAULT NULL COMMENT '更新人ID',
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除标记：0正常，1已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username_deleted` (`username`, `is_deleted`),
    UNIQUE KEY `uk_phone_deleted` (`phone`, `is_deleted`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- 项目表
CREATE TABLE IF NOT EXISTS `project` (
                                         `id` BIGINT NOT NULL COMMENT '项目ID',
                                         `name` VARCHAR(100) NOT NULL COMMENT '项目名称',
    `description` TEXT COMMENT '项目描述',
    `owner_id` BIGINT NOT NULL COMMENT '项目所有者ID',
    `created_by` BIGINT DEFAULT NULL,
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_by` BIGINT DEFAULT NULL,
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `is_deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    INDEX `idx_owner_id` (`owner_id`),
    INDEX `idx_is_deleted` (`is_deleted`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目表';

-- 任务表
CREATE TABLE IF NOT EXISTS `task` (
                                      `id` BIGINT NOT NULL COMMENT '任务ID',
                                      `title` VARCHAR(200) NOT NULL COMMENT '任务标题',
    `description` TEXT COMMENT '任务描述',
    `status` INT DEFAULT 0 COMMENT '状态：0待办，1进行中，2已完成',
    `project_id` BIGINT NOT NULL COMMENT '所属项目ID',
    `assignee_id` BIGINT DEFAULT NULL COMMENT '指派人用户ID',
    `created_by` BIGINT DEFAULT NULL,
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_by` BIGINT DEFAULT NULL,
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `is_deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    INDEX `idx_project_id` (`project_id`),
    INDEX `idx_assignee_id` (`assignee_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务表';

-- 缺陷表
CREATE TABLE IF NOT EXISTS `bug` (
                                     `id` BIGINT NOT NULL COMMENT '缺陷ID',
                                     `title` VARCHAR(200) NOT NULL COMMENT '缺陷标题',
    `severity` VARCHAR(10) DEFAULT 'P2' COMMENT '严重程度：P0/P1/P2/P3',
    `project_id` BIGINT NOT NULL COMMENT '所属项目ID',
    `created_by` BIGINT DEFAULT NULL,
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_by` BIGINT DEFAULT NULL,
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `is_deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    INDEX `idx_project_id` (`project_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='缺陷表';

-- 迭代表
CREATE TABLE IF NOT EXISTS `sprint` (
                                        `id` BIGINT NOT NULL COMMENT '迭代ID',
                                        `name` VARCHAR(100) NOT NULL COMMENT '迭代名称',
    `start_date` DATE DEFAULT NULL COMMENT '开始日期',
    `end_date` DATE DEFAULT NULL COMMENT '结束日期',
    `project_id` BIGINT NOT NULL COMMENT '所属项目ID',
    `created_by` BIGINT DEFAULT NULL,
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_by` BIGINT DEFAULT NULL,
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `is_deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    INDEX `idx_project_id` (`project_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='迭代表';

-- 项目成员表
CREATE TABLE IF NOT EXISTS `project_member` (
                                                `id` BIGINT NOT NULL COMMENT '成员关系ID',
                                                `user_id` BIGINT NOT NULL COMMENT '用户ID',
                                                `project_id` BIGINT NOT NULL COMMENT '项目ID',
                                                `role` VARCHAR(20) NOT NULL DEFAULT 'MEMBER' COMMENT '角色：ADMIN/MEMBER/VIEWER',
    `created_by` BIGINT DEFAULT NULL,
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_by` BIGINT DEFAULT NULL,
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `is_deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_project_user_deleted` (`project_id`, `user_id`, `is_deleted`),
    INDEX `idx_user_id` (`user_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目成员表';