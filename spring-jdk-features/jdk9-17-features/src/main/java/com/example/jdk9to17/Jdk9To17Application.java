package com.example.jdk9to17;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * JDK 9-17 新特性演示应用
 *
 * 本项目演示了 JDK 9 到 JDK 17 的主要新特性，以及 Spring Boot 3.x 对这些特性的支持
 */
@SpringBootApplication
@EnableFeignClients
public class Jdk9To17Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Jdk9To17Application.class, args);

        System.out.println("=================================");
        System.out.println("JDK 9-17 特性演示应用已启动");
        System.out.println("JDK 版本: " + Runtime.version());
        System.out.println("=================================");
        
        // Run the new JDK 15 Text Blocks demo
        try {
            if (context != null) {
                com.example.jdk9to17.jdk15.TextBlocksDemo textBlocksDemo =
                        context.getBean(com.example.jdk9to17.jdk15.TextBlocksDemo.class);
                if (textBlocksDemo != null) {
                    textBlocksDemo.demonstrate();
                } else {
                    System.err.println("警告: TextBlocksDemo bean 未找到");
                }
            }
        } catch (Exception e) {
            System.err.println("执行 TextBlocksDemo 时发生错误: " + e.getMessage());
            e.printStackTrace();
        }

    }
}