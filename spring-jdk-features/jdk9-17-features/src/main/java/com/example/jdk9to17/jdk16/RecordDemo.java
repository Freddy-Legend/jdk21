package com.example.jdk9to17.jdk16;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * JDK 16: Record 类（标准化）
 * <p>
 * Record 是不可变数据类的简洁语法，自动生成构造器、getter、equals、hashCode、toString
 */
@Component
public class RecordDemo {

    // 定义 Record 类
    public record User(Long id, String name, String email) {
        // 可以添加自定义方法
        public String displayName() {
            return name + " (" + email + ")";
        }

        // 紧凑构造器（验证）
        public User {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("Name cannot be blank");
            }
        }
    }

    public record Product(String id, String name, double price) {
        // 静态工厂方法
        public static Product of(String id, String name, double price) {
            return new Product(id, name, price);
        }
    }

    public record ApiResponse<T>(int code, String message, T data) {
    }

    public void demonstrate() {
        System.out.println("\n=== JDK 16: Record 类 ===");

        // 创建 Record 实例
        var user = new User(1L, "张三", "zhangsan@example.com");
        System.out.println("User Record: " + user);
        System.out.println("User ID: " + user.id());
        System.out.println("User Name: " + user.name());
        System.out.println("Display Name: " + user.displayName());

        // Record 的不可变性
        System.out.println("\nRecord 是不可变的:");
        var user2 = new User(1L, "张三", "zhangsan@example.com");
        System.out.println("user.equals(user2): " + user.equals(user2));
        System.out.println("user.hashCode(): " + user.hashCode());

        // 在集合中使用
        var users = List.of(
            new User(1L, "张三", "zhangsan@example.com"),
            new User(2L, "李四", "lisi@example.com"),
            new User(3L, "王五", "wangwu@example.com")
        );

        System.out.println("\n用户列表:");
        users.forEach(u -> System.out.println("  - " + u.displayName()));

        // 泛型 Record
        var response = new ApiResponse<>(200, "Success", users);
        System.out.println("\nAPI Response: " + response.code() + " - " + response.message());
        System.out.println("Data count: " + response.data().size());

        // Product Record
        var product = Product.of("P001", "Spring Boot 实战", 89.9);
        System.out.println("\nProduct: " + product);
    }

    /**
     * Record 在 Spring 中的实际应用
     */
    public void demonstrateInSpring() {
        System.out.println("\n=== Record 在 Spring 中的应用 ===");

        // DTO (Data Transfer Object)
        record UserDTO(String name, String email) {}

        // Request/Response 对象
        record CreateUserRequest(String name, String email, String password) {}
        record CreateUserResponse(Long id, String name, String createdAt) {}

        // 配置类
        record DatabaseConfig(String url, String username, int maxConnections) {}

        var dto = new UserDTO("张三", "zhangsan@example.com");
        System.out.println("DTO: " + dto);

        var request = new CreateUserRequest("李四", "lisi@example.com", "password123");
        System.out.println("Request: " + request);

        var config = new DatabaseConfig("jdbc:mysql://localhost:3306/db", "root", 10);
        System.out.println("Config: " + config);
    }
}
