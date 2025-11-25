package tech.legend.learn;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

/**
 * Optional 增强功能对比示例
 * Optional Enhancements Comparison Example
 *
 * 本类演示 JDK 9 和 JDK 11 中 Optional 类的新增方法，对比 JDK 8 的功能
 * This class demonstrates the new methods added to the Optional class in JDK 9 and JDK 11,
 * comparing with the functionality in JDK 8
 *
 * 主要改进：
 * Key Improvements:
 * 1. 提供更多实用的方法来处理 Optional 值
 *    Provide more utility methods to handle Optional values
 * 2. 减少样板代码
 *    Reduce boilerplate code
 * 3. 提高代码可读性
 *    Improve code readability
 *
 * 版本历史：
 * Version History:
 * - JDK 8: 引入 Optional 类 / Introduced Optional class
 * - JDK 9: 添加 or() 和 ifPresentOrElse() 方法 / Added or() and ifPresentOrElse() methods
 * - JDK 11: 添加 isEmpty() 方法 / Added isEmpty() method
 */
public class OptionalEnhancementsComparison {

    public static void main(String[] args) {
        System.out.println("=== Optional Enhancements Comparison (JDK 8 vs JDK 9/11) ===");

        // ============================================
        // JDK 8: 基础 Optional 功能
        // JDK 8: Basic Optional Features
        // ============================================
        Optional<String> optionalJDK8 = Optional.of("Hello JDK 8");
        Optional<String> emptyOptionalJDK8 = Optional.empty();

        // 基本操作
        // Basic operations
        if (optionalJDK8.isPresent()) {
            System.out.println("JDK 8 Value: " + optionalJDK8.get());
        }

        // 处理空值的传统方式
        // Traditional way to handle empty values
        if (!emptyOptionalJDK8.isPresent()) {
            System.out.println("JDK 8: Empty Optional");
        }

        // 使用 orElse 提供默认值
        // Using orElse to provide default values
        String result = emptyOptionalJDK8.orElse("Default Value");
        System.out.println("JDK 8 orElse: " + result);

        // ============================================
        // JDK 9: 新增方法
        // JDK 9: New Methods
        // ============================================

        // or() 方法 - 提供备选 Optional
        // or() method - provide alternative Optional
        Optional<String> firstOptional = Optional.empty();
        Optional<String> secondOptional = Optional.of("Fallback Value");
        
        Optional<String> resultOr = firstOptional.or(() -> secondOptional);
        System.out.println("JDK 9 or(): " + resultOr.orElse("No value"));

        // ifPresentOrElse() 方法 - 分别处理存在和不存在的情况
        // ifPresentOrElse() method - handle present and absent cases separately
        Optional<String> presentOpt = Optional.of("Value exists");
        Optional<String> absentOpt = Optional.empty();

        presentOpt.ifPresentOrElse(
                value -> System.out.println("JDK 9 ifPresentOrElse (present): " + value),
                () -> System.out.println("JDK 9 ifPresentOrElse (absent): No value")
        );

        absentOpt.ifPresentOrElse(
                value -> System.out.println("JDK 9 ifPresentOrElse (present): " + value),
                () -> System.out.println("JDK 9 ifPresentOrElse (absent): No value")
        );

        // ============================================
        // JDK 11: 新增 isEmpty() 方法
        // JDK 11: New isEmpty() Method
        // ============================================

        Optional<String> filledOpt = Optional.of("Filled");
        Optional<String> emptyOpt = Optional.empty();

        // JDK 8 方式检查空值
        // JDK 8 way to check for emptiness
        if (!filledOpt.isPresent()) {
            System.out.println("JDK 8 Style: Optional is empty");
        }

        // JDK 11 方式检查空值（更直观）
        // JDK 11 way to check for emptiness (more intuitive)
        if (emptyOpt.isEmpty()) {
            System.out.println("JDK 11 isEmpty(): Optional is empty");
        }

        // ============================================
        // 实际应用示例
        // Practical Application Example
        // ============================================

        // 模拟用户查找场景
        // Simulate user lookup scenario
        UserService userService = new UserService();

        // JDK 8 方式
        // JDK 8 approach
        Optional<User> userJDK8 = userService.findUserById(1);
        if (userJDK8.isPresent()) {
            System.out.println("JDK 8 User Found: " + userJDK8.get().getName());
        } else {
            System.out.println("JDK 8 User Not Found");
        }

        // JDK 9 方式 - 使用 ifPresentOrElse
        // JDK 9 approach - using ifPresentOrElse
        userService.findUserById(2).ifPresentOrElse(
                user -> System.out.println("JDK 9 User Found: " + user.getName()),
                () -> System.out.println("JDK 9 User Not Found")
        );

        // JDK 9 方式 - 使用 or 提供备选方案
        // JDK 9 approach - using or to provide fallback
        Optional<User> primaryUser = userService.findUserById(999); // 不存在的用户
        Optional<User> backupUser = userService.findUserById(1);   // 备选用户
        
        User finalUser = primaryUser.or(() -> backupUser)
                .orElse(new User(0, "Default User"));
        System.out.println("JDK 9 Final User: " + finalUser.getName());
    }

    // 用户实体类
    // User entity class
    static class User {
        private int id;
        private String name;

        public User(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "User{id=" + id + ", name='" + name + "'}";
        }
    }

    // 用户服务类
    // User service class
    static class UserService {
        private List<User> users;

        public UserService() {
            users = new ArrayList<>();
            users.add(new User(1, "Alice"));
            users.add(new User(2, "Bob"));
            users.add(new User(3, "Charlie"));
        }

        public Optional<User> findUserById(int id) {
            return users.stream()
                    .filter(user -> user.getId() == id)
                    .findFirst();
        }
    }
}