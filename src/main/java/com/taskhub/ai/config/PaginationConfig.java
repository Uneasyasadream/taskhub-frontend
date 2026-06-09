// TaskhubAiApplication.java 或新建一个 PaginationConfig.java
package com.taskhub.ai.config;

import com.taskhub.ai.TaskhubAiApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO) // 关键配置
public class PaginationConfig {
    public static void main(String[] args) {
        SpringApplication.run(TaskhubAiApplication.class, args);
    }
}