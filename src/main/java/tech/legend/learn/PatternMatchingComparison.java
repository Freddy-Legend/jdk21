package tech.legend.learn;

/**
 * Pattern Matching 模式匹配功能对比示例
 * Pattern Matching Feature Comparison Example
 *
 * 本类演示 JDK 14/16/21 中引入的 Pattern Matching 新特性，对比传统方式与新方式的差异
 * This class demonstrates the Pattern Matching features introduced in JDK 14/16/21,
 * comparing traditional approaches with new methods
 *
 * 主要改进：
 * Key Improvements:
 * 1. instanceof 模式匹配：无需显式类型转换
 *    Pattern Matching for instanceof: No explicit casting needed
 * 2. Switch 模式匹配：支持类型匹配和模式变量
 *    Pattern Matching for switch: Supports type matching and pattern variables
 * 3. 代码更简洁、更安全、减少样板代码
 *    More concise, safer code with less boilerplate
 *
 * 版本历史：
 * Version History:
 * - JDK 14: instanceof 模式匹配预览 / Pattern Matching for instanceof (Preview)
 * - JDK 16: instanceof 模式匹配正式发布 / Pattern Matching for instanceof (Final)
 * - JDK 17: Switch 模式匹配预览 / Pattern Matching for switch (Preview)
 * - JDK 21: Switch 模式匹配正式发布 / Pattern Matching for switch (Final)
 */
public class PatternMatchingComparison {

    public static void main(String[] args) {
        System.out.println("=== Pattern Matching Comparison ===");

        // 测试对象：字符串类型
        // Test object: String type
        Object obj = "Hello World";

        // ============================================
        // Part 1: instanceof 模式匹配对比
        // Part 1: instanceof Pattern Matching Comparison
        // ============================================

        // JDK 8 及更早版本：传统的 instanceof + 强制类型转换
        // JDK 8 and earlier: Traditional instanceof + explicit casting
        // 缺点：
        // Drawbacks:
        // 1. 需要先检查类型，再进行强制类型转换，代码冗余
        //    Need to check type first, then cast explicitly - redundant code
        // 2. 变量声明与类型检查分离，可读性差
        //    Variable declaration separated from type checking - less readable
        // 3. 可能出现类型转换错误
        //    Potential casting errors
        if (obj instanceof String) {
            String s = (String) obj;  // 显式强制类型转换 / Explicit casting
            System.out.println("JDK 8: " + s.toUpperCase());
        }

        // JDK 16+ (JDK 14 预览版): instanceof 的模式匹配
        // JDK 16+ (Preview in JDK 14): Pattern Matching for instanceof
        // 优点：
        // Advantages:
        // 1. 类型检查和变量声明合二为一
        //    Type checking and variable declaration combined
        // 2. 自动类型转换，无需显式 cast
        //    Automatic type conversion, no explicit cast needed
        // 3. 作用域限定在 if 块内，更安全
        //    Scope limited to if block, safer
        if (obj instanceof String s) {  // s 自动被声明和转换 / s is automatically declared and cast
            System.out.println("JDK 21: " + s.toUpperCase());
        }

        // ============================================
        // Part 2: Switch 模式匹配
        // Part 2: Pattern Matching for Switch
        // ============================================

        // JDK 21: Switch 表达式的模式匹配
        // JDK 21: Pattern Matching for switch expressions
        // 新特性：
        // New features:
        // 1. 支持类型模式匹配，可以直接匹配对象类型
        //    Supports type pattern matching, can match object types directly
        // 2. 使用箭头语法 (->) 简化代码
        //    Uses arrow syntax (->) to simplify code
        // 3. Switch 作为表达式可以直接返回值
        //    Switch as expression can return values directly
        // 4. 编译器保证穷尽性检查（需要 default 或覆盖所有情况）
        //    Compiler ensures exhaustiveness (requires default or covers all cases)
        String result = switch (obj) {
            case String s -> "It's a string: " + s;      // 匹配 String 类型 / Match String type
            case Integer i -> "It's an integer: " + i;   // 匹配 Integer 类型 / Match Integer type
            default -> "Unknown type";                   // 默认分支（保证穷尽性）/ Default case (ensures exhaustiveness)
        };
        System.out.println("JDK 21 Switch Pattern: " + result);

        // 注意：在 JDK 8 中实现相同功能需要多个 if-else 和强制类型转换
        // Note: In JDK 8, achieving the same functionality requires multiple if-else and explicit casting
        // 例如：
        // Example:
        // String result;
        // if (obj instanceof String) {
        //     result = "It's a string: " + (String) obj;
        // } else if (obj instanceof Integer) {
        //     result = "It's an integer: " + (Integer) obj;
        // } else {
        //     result = "Unknown type";
        // }
    }
}
