package tech.legend.learn;

import java.util.Objects;

/**
 * Records 功能对比示例
 * Records Feature Comparison Example
 *
 * 本类演示 JDK 14/16 中引入的 Record 类型，对比传统 POJO 类的差异
 * This class demonstrates the Record type introduced in JDK 14/16,
 * comparing traditional POJO classes with Records
 *
 * 主要改进：
 * Key Improvements:
 * 1. 极简语法：一行代码替代几十行样板代码
 *    Minimal syntax: One line replaces dozens of boilerplate code
 * 2. 不可变性：自动创建不可变数据载体
 *    Immutability: Automatically creates immutable data carriers
 * 3. 自动生成：构造器、getter、equals()、hashCode()、toString()
 *    Auto-generated: Constructor, getters, equals(), hashCode(), toString()
 * 4. 语义清晰：明确表达数据载体的意图
 *    Clear semantics: Explicitly expresses data carrier intent
 *
 * 版本历史：
 * Version History:
 * - JDK 14: Record 预览 / Records (Preview)
 * - JDK 15: Record 第二次预览 / Records (Second Preview)
 * - JDK 16: Record 正式发布 / Records (Final)
 */
public class RecordsComparison {

    // ============================================
    // JDK 8 及更早版本：传统的 POJO 类
    // JDK 8 and earlier: Traditional POJO class
    // ============================================
    /**
     * 传统的不可变 POJO 类示例
     * Traditional immutable POJO class example
     *
     * 缺点：
     * Drawbacks:
     * 1. 需要手动编写大量样板代码（45+ 行）
     *    Requires manually writing lots of boilerplate code (45+ lines)
     * 2. 容易出错：忘记重写 equals/hashCode 导致 bug
     *    Error-prone: Forgetting to override equals/hashCode causes bugs
     * 3. 维护成本高：添加字段需要更新多处代码
     *    High maintenance cost: Adding fields requires updating multiple places
     * 4. 意图不明确：看起来像普通类
     *    Unclear intent: Looks like a regular class
     */
    public static class UserOld {
        // 私有 final 字段
        // Private final fields
        private final String name;
        private final int age;

        // 全参构造器
        // All-args constructor
        public UserOld(String name, int age) {
            this.name = name;
            this.age = age;
        }

        // Getter 方法（使用 get 前缀）
        // Getter methods (with 'get' prefix)
        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        // 必须手动重写 equals() 方法
        // Must manually override equals() method
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UserOld userOld = (UserOld) o;
            return age == userOld.age && Objects.equals(name, userOld.name);
        }

        // 必须手动重写 hashCode() 方法
        // Must manually override hashCode() method
        @Override
        public int hashCode() {
            return Objects.hash(name, age);
        }

        // 必须手动重写 toString() 方法
        // Must manually override toString() method
        @Override
        public String toString() {
            return "UserOld{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    // ============================================
    // JDK 16+ (JDK 14 预览版): Record 类型
    // JDK 16+ (Preview in JDK 14): Record type
    // ============================================
    /**
     * Record 类型示例 - 仅需一行代码！
     * Record type example - Only one line of code!
     *
     * 优点：
     * Advantages:
     * 1. 极简语法：一行代码完成所有功能
     *    Minimal syntax: One line for all functionality
     * 2. 自动生成以下内容：
     *    Automatically generates:
     *    - 私有 final 字段 / Private final fields
     *    - 全参构造器 / All-args constructor
     *    - 访问器方法（无 get 前缀）/ Accessor methods (no 'get' prefix)
     *    - equals() 方法（基于所有字段）/ equals() method (based on all fields)
     *    - hashCode() 方法（基于所有字段）/ hashCode() method (based on all fields)
     *    - toString() 方法（格式化输出）/ toString() method (formatted output)
     * 3. 不可变性保证：所有字段默认为 final
     *    Immutability guarantee: All fields are final by default
     * 4. 语义清晰：明确表达"数据载体"的意图
     *    Clear semantics: Explicitly expresses "data carrier" intent
     */
    public record UserNew(String name, int age) {
        // 这一行等价于上面 UserOld 类的所有代码！
        // This one line is equivalent to all the code in UserOld class above!

        // 可选：可以添加自定义方法或验证逻辑
        // Optional: Can add custom methods or validation logic
        // 示例：紧凑构造器用于参数验证
        // Example: Compact constructor for parameter validation
        /*
        public UserNew {
            if (age < 0) {
                throw new IllegalArgumentException("Age cannot be negative");
            }
        }
        */
    }

    public static void main(String[] args) {
        System.out.println("=== Records Comparison ===");

        // ============================================
        // 创建和使用传统 POJO
        // Creating and using traditional POJO
        // ============================================
        UserOld oldUser = new UserOld("Alice", 30);
        System.out.println("JDK 8 POJO: " + oldUser);
        // 输出 / Output: UserOld{name='Alice', age=30}

        // ============================================
        // 创建和使用 Record
        // Creating and using Record
        // ============================================
        UserNew newUser = new UserNew("Alice", 30);
        System.out.println("JDK 21 Record: " + newUser);
        // 输出 / Output: UserNew[name=Alice, age=30]

        // ============================================
        // 访问器方法对比
        // Accessor method comparison
        // ============================================
        // 传统 POJO：使用 get 前缀
        // Traditional POJO: Uses 'get' prefix
        System.out.println("name accessor (Old): " + oldUser.getName());

        // Record：直接使用字段名作为方法名（更简洁）
        // Record: Uses field name as method name directly (more concise)
        System.out.println("name accessor (New): " + newUser.name());

        // ============================================
        // 其他自动生成的功能演示
        // Demonstration of other auto-generated features
        // ============================================
        // equals() 方法自动实现
        // equals() method automatically implemented
        UserNew anotherUser = new UserNew("Alice", 30);
        System.out.println("Equality check: " + newUser.equals(anotherUser)); // true

        // hashCode() 方法自动实现
        // hashCode() method automatically implemented
        System.out.println("Hash code: " + newUser.hashCode());
    }
}
