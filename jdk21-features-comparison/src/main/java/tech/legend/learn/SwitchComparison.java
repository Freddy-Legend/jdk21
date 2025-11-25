package tech.legend.learn;

/**
 * Switch 表达式功能对比示例
 * Switch Expression Feature Comparison Example
 *
 * 本类演示 JDK 12/14 中引入的 Switch Expression 新特性，对比传统 Switch 语句的差异
 * This class demonstrates the Switch Expression feature introduced in JDK 12/14,
 * comparing traditional switch statements with new switch expressions
 *
 * 主要改进：
 * Key Improvements:
 * 1. Switch 作为表达式可以直接返回值，无需中间变量
 *    Switch as expression can return values directly without intermediate variables
 * 2. 使用箭头语法 (->) 替代冒号 (:)，无需 break 语句
 *    Uses arrow syntax (->) instead of colon (:), no break statement needed
 * 3. 支持多个 case 标签用逗号分隔
 *    Supports multiple case labels separated by commas
 * 4. 编译器强制穷尽性检查，更安全
 *    Compiler enforces exhaustiveness checking, safer
 *
 * 版本历史：
 * Version History:
 * - JDK 12: Switch 表达式预览 / Switch Expressions (Preview)
 * - JDK 13: Switch 表达式第二次预览 / Switch Expressions (Second Preview)
 * - JDK 14: Switch 表达式正式发布 / Switch Expressions (Final)
 */
public class SwitchComparison {

    /**
     * 星期枚举类型
     * Day enumeration type
     */
    enum Day {MONDAY, FRIDAY, SUNDAY}

    public static void main(String[] args) {
        System.out.println("=== Switch Comparison ===");

        // 测试数据：星期五
        // Test data: Friday
        Day day = Day.FRIDAY;

        // ============================================
        // JDK 8 及更早版本：传统的 Switch 语句
        // JDK 8 and earlier: Traditional Switch Statement
        // ============================================
        // 缺点：
        // Drawbacks:
        // 1. 必须先声明变量，Switch 不能直接返回值
        //    Must declare variable first, switch cannot return values directly
        // 2. 需要 break 语句防止穿透，容易遗漏导致 bug
        //    Requires break statements to prevent fall-through, easy to forget causing bugs
        // 3. 代码冗长，可读性差
        //    Verbose code, poor readability
        // 4. 每个 case 需要单独一行，不能合并
        //    Each case needs a separate line, cannot be combined
        String resultOld;
        switch (day) {
            case MONDAY:      // case 穿透 / fall-through
            case FRIDAY:
                resultOld = "Work";
                break;        // 必须使用 break / must use break
            case SUNDAY:
                resultOld = "Rest";
                break;
            default:
                resultOld = "Unknown";
        }
        System.out.println("JDK 8 Switch: " + resultOld);

        // ============================================
        // JDK 14+ (JDK 12 预览版): Switch 表达式
        // JDK 14+ (Preview in JDK 12): Switch Expression
        // ============================================
        // 优点：
        // Advantages:
        // 1. 直接返回值并赋值，无需中间变量
        //    Directly returns and assigns value, no intermediate variable needed
        // 2. 使用箭头 (->) 替代冒号和 break，代码更简洁
        //    Uses arrow (->) instead of colon and break, more concise code
        // 3. 支持逗号分隔多个 case 标签
        //    Supports comma-separated multiple case labels
        // 4. 编译器检查所有情况是否覆盖（穷尽性检查）
        //    Compiler checks if all cases are covered (exhaustiveness checking)
        // 5. 不会发生 case 穿透问题
        //    No fall-through issues
        String resultNew = switch (day) {
            case MONDAY, FRIDAY -> "Work";   // 多个 case 用逗号分隔 / Multiple cases separated by comma
            case SUNDAY -> "Rest";           // 箭头语法，无需 break / Arrow syntax, no break needed
        };  // 编译器检查穷尽性（所有枚举值已覆盖）/ Compiler checks exhaustiveness (all enum values covered)
        System.out.println("JDK 21 Switch Expression: " + resultNew);

        // 注意：Switch 表达式的穷尽性检查
        // Note: Exhaustiveness checking in switch expressions
        // - 对于枚举类型，必须覆盖所有枚举值或提供 default 分支
        //   For enum types, must cover all enum values or provide default branch
        // - 对于密封类，必须覆盖所有子类型
        //   For sealed classes, must cover all subtypes
        // - 对于其他类型，通常需要 default 分支
        //   For other types, usually requires default branch
    }
}
