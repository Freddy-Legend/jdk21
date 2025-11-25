package com.example.jdk9to17.jdk14;

import org.springframework.stereotype.Component;

/**
 * JDK 14: Switch 表达式（标准化）
 *
 * Switch 从语句升级为表达式，支持返回值和箭头语法
 */
@Component
public class SwitchExpressionDemo {

    public void demonstrate() {
        System.out.println("\n=== JDK 14: Switch 表达式 ===");

        // 传统 switch 语句
        String day = "MONDAY";
        int numLetters;
        switch (day) {
            case "MONDAY":
            case "FRIDAY":
            case "SUNDAY":
                numLetters = 6;
                break;
            case "TUESDAY":
                numLetters = 7;
                break;
            default:
                numLetters = 0;
        }
        System.out.println("传统 switch: " + day + " has " + numLetters + " letters");

        // 新的 switch 表达式
        var result = switch (day) {
            case "MONDAY", "FRIDAY", "SUNDAY" -> 6;
            case "TUESDAY" -> 7;
            case "THURSDAY", "SATURDAY" -> 8;
            case "WEDNESDAY" -> 9;
            default -> 0;
        };
        System.out.println("新 switch 表达式: " + day + " has " + result + " letters");

        // 使用 yield 返回值（块语法）
        var seasonType = switch (getMonth()) {
            case 12, 1, 2 -> "冬季";
            case 3, 4, 5 -> "春季";
            case 6, 7, 8 -> "夏季";
            case 9, 10, 11 -> "秋季";
            default -> {
                System.out.println("无效的月份");
                yield "未知";
            }
        };
        System.out.println("当前季节: " + seasonType);

        // 在 Spring 服务中的实际应用
        demonstrateInSpringContext();
    }

    private int getMonth() {
        return java.time.LocalDate.now().getMonthValue();
    }

    /**
     * 在 Spring 上下文中使用 Switch 表达式
     */
    public void demonstrateInSpringContext() {
        System.out.println("\n在 Spring 服务中使用 Switch 表达式:");

        // 根据用户角色返回不同的权限
        String role = "ADMIN";
        var permissions = switch (role) {
            case "ADMIN" -> "ALL";
            case "USER" -> "READ, WRITE";
            case "GUEST" -> "READ";
            default -> "NONE";
        };
        System.out.println("Role: " + role + " -> Permissions: " + permissions);

        // 根据 HTTP 状态码返回消息
        int statusCode = 200;
        var message = switch (statusCode) {
            case 200 -> "OK";
            case 201 -> "Created";
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 404 -> "Not Found";
            case 500 -> "Internal Server Error";
            default -> "Unknown Status";
        };
        System.out.println("HTTP " + statusCode + ": " + message);
    }
}
