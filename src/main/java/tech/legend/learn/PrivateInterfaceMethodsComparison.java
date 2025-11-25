package tech.legend.learn;

/**
 * 接口私有方法功能对比示例 / Private Interface Methods Comparison Example
 *
 * 本类演示 JDK 9 中引入的接口私有方法特性，对比 JDK 8 中的限制
 * This class demonstrates the private interface methods feature introduced in JDK 9,
 * comparing it with the limitations in JDK 8
 *
 * 主要改进：
 * Key Improvements:
 * 1. 允许在接口中定义私有方法
 *    Allow private methods in interfaces
 * 2. 提高代码复用性
 *    Improve code reusability
 * 3. 更好的封装性
 *    Better encapsulation
 *
 * 版本历史：
 * Version History:
 * - JDK 8: 接口只能包含 public abstract 方法、static 方法和 default 方法
 *          Interfaces could only contain public abstract methods, static methods, and default methods
 * - JDK 9: 引入接口私有方法 / Introduced private interface methods
 */
public class PrivateInterfaceMethodsComparison {

    public static void main(String[] args) {
        System.out.println("=== Private Interface Methods Comparison (JDK 8 vs JDK 9) ===");

        Calculator calc = new CalculatorImpl();
        System.out.println("Addition Result: " + calc.add(5, 3));
        System.out.println("Multiplication Result: " + calc.multiply(5, 3));
    }

    // ============================================
    // JDK 8: 接口限制
    // JDK 8: Interface Limitations
    // ============================================
    /**
     * JDK 8 接口示例
     * JDK 8 Interface Example
     *
     * 缺点：
     * Drawbacks:
     * 1. 无法在接口中共享代码实现
     *    Cannot share code implementation in interfaces
     * 2. default 方法可能暴露不必要的公共API
     *    default methods may expose unnecessary public APIs
     * 3. 代码重复问题
     *    Code duplication issues
     */
    interface CalculatorJDK8 {
        // 抽象方法
        // Abstract methods
        int add(int a, int b);
        int multiply(int a, int b);

        // 默认方法实现
        // Default method implementation
        default int square(int a) {
            // 如果有复杂逻辑需要复用，JDK 8 中很难优雅地实现
            // If there's complex logic that needs reuse, it's hard to implement elegantly in JDK 8
            return a * a;
        }

        // 静态方法
        // Static method
        static int cube(int a) {
            return a * a * a;
        }
    }

    // ============================================
    // JDK 9: 接口私有方法
    // JDK 9: Private Interface Methods
    // ============================================
    /**
     * JDK 9 接口示例（包含私有方法）
     * JDK 9 Interface Example (with private methods)
     *
     * 优点：
     * Advantages:
     * 1. 可以在接口中定义私有方法来共享代码
     *    Can define private methods in interfaces to share code
     * 2. 提高代码组织性和可维护性
     *    Improve code organization and maintainability
     * 3. 避免暴露内部实现细节
     *    Avoid exposing internal implementation details
     * 4. 减少代码重复
     *    Reduce code duplication
     */
    interface Calculator {
        int add(int a, int b);
        int multiply(int a, int b);

        // 公共默认方法
        // Public default method
        default int power(int base, int exponent) {
            // 复用私有方法进行验证
            // Reuse private method for validation
            validateInputs(base, exponent);
            
            if (exponent == 0) return 1;
            if (exponent < 0) throw new IllegalArgumentException("Negative exponents not supported");
            
            int result = 1;
            for (int i = 0; i < exponent; i++) {
                result = multiply(result, base);
            }
            return result;
        }

        // 私有方法 - 只能在接口内部使用
        // Private method - only usable within the interface
        private void validateInputs(int a, int b) {
            if (a < 0 || b < 0) {
                throw new IllegalArgumentException("Negative inputs not supported");
            }
        }

        // 私有静态方法 - 可以在接口内部的静态和实例方法中使用
        // Private static method - can be used in static and instance methods within the interface
        private static void logOperation(String operation) {
            System.out.println("Performing operation: " + operation);
        }

        // 公共静态方法也可以使用私有静态方法
        // Public static methods can also use private static methods
        static Calculator getInstance() {
            logOperation("Creating calculator instance");
            return new CalculatorImpl();
        }
    }

    // 实现类
    // Implementation class
    static class CalculatorImpl implements Calculator {
        @Override
        public int add(int a, int b) {
            // 实现类中无法访问接口的私有方法
            // Implementation classes cannot access private methods of interfaces
            return a + b;
        }

        @Override
        public int multiply(int a, int b) {
            return a * b;
        }
    }
}