package com.example.jdk21.patternmatching;

import org.springframework.stereotype.Component;

/**
 * JDK 21: Pattern Matching for switch 和 Record Patterns
 *
 * 增强的模式匹配，支持更复杂的类型检查和解构
 */
@Component
public class PatternMatchingDemo {


    public void demonstrate() {
        System.out.println("\n=== JDK 21: Pattern Matching ===");

        // 1. Switch 模式匹配
        demonstrateSwitchPatterns();

        // 2. Record Patterns (解构)
        demonstrateRecordPatterns();

        // 3. Guard Patterns
        demonstrateGuardPatterns();

        // 4. 实际应用场景
        demonstrateRealWorldUsage();
    }

    /**
     * Switch 模式匹配
     */
    private void demonstrateSwitchPatterns() {
        System.out.println("\n1. Switch 模式匹配:");

        Object obj = "Hello";
        String result = switch (obj) {
            case String s -> "字符串: " + s;
            case Integer i -> "整数: " + i;
            case Long l -> "长整数: " + l;
            case null -> "空值";
            default -> "其他类型: " + obj.getClass().getSimpleName();
        };
        System.out.println("  " + result);

        // 带有条件的模式匹配
        obj = 42;
        String description = switch (obj) {
            case String s when s.length() > 10 -> "长字符串";
            case String s -> "短字符串";
            case Integer i when i > 100 -> "大整数: " + i;
            case Integer i when i > 0 -> "正整数: " + i;
            case Integer i -> "非正整数: " + i;
            default -> "其他";
        };
        System.out.println("  " + description);
    }

    /**
     * Record Patterns (记录模式/解构)
     */
    private void demonstrateRecordPatterns() {
        System.out.println("\n2. Record Patterns (解构):");

        Point point = new Point(10, 20);

        // 解构 Record
        if (point instanceof Point(int x, int y)) {
            System.out.println("  Point 解构: x=" + x + ", y=" + y);
        }

        // 嵌套解构
        Circle circle = new Circle(new Point(5, 5), 10);
        if (circle instanceof Circle(Point(int x, int y), int r)) {
            System.out.println("  Circle 解构: center=(" + x + ", " + y + "), radius=" + r);
        }

        // 在 switch 中使用
        Shape shape = new Triangle(
            new Point(0, 0),
            new Point(3, 0),
            new Point(0, 4)
        );

        String shapeInfo = switch (shape) {
            case Circle(Point(int x, int y), int r) ->
                "圆形: 圆心(" + x + ", " + y + "), 半径=" + r;
            case Rectangle(Point(int x1, int y1), Point(int x2, int y2)) ->
                "矩形: (" + x1 + ", " + y1 + ") 到 (" + x2 + ", " + y2 + ")";
            case Triangle(Point p1, Point p2, Point p3) ->
                "三角形: 3个顶点";
        };
        System.out.println("  " + shapeInfo);
    }

    /**
     * Guard Patterns (守卫模式)
     */
    private void demonstrateGuardPatterns() {
        System.out.println("\n3. Guard Patterns:");

        record Employee(String name, int age, String department) {}

        Employee emp = new Employee("张三", 28, "技术部");

        // 使用 when 子句
        String category = switch (emp) {
            case Employee(String name, int age, String dept)
                when age < 30 && dept.equals("技术部") ->
                "年轻技术人员: " + name;
            case Employee(String name, int age, String dept)
                when age >= 30 && dept.equals("技术部") ->
                "资深技术人员: " + name;
            case Employee(String name, int age, String dept) ->
                name + " (" + dept + ")";
        };
        System.out.println("  " + category);
    }

    /**
     * 实际应用场景
     */
    private void demonstrateRealWorldUsage() {
        System.out.println("\n4. 实际应用场景:");

        ApiResponse response = new Success(200, "用户数据");

        String result = switch (response) {
            case Success(int code, String data) ->
                "成功 [" + code + "]: " + data;
            case Error(int code, String msg) when code == 404 ->
                "未找到: " + msg;
            case Error(int code, String msg) when code >= 500 ->
                "服务器错误: " + msg;
            case Error(int code, String msg) ->
                "客户端错误 [" + code + "]: " + msg;
            case Loading() ->
                "加载中...";
        };
        System.out.println("  API 响应: " + result);

        // 处理 JSON 数据结构
        record JsonObject(String type, Object value) {}

        JsonObject json = new JsonObject("user", "张三");

        String jsonResult = switch (json.type) {
            case "user" ->
                "用户" ;
            case "number" ->
                "数字";
            default ->
                "其他类型 [" +json.type+"]";
        };
        System.out.println("  JSON 处理: " + jsonResult);
    }

    /**
     * Spring 中的应用示例
     */
    public void demonstrateSpringUsage() {
        System.out.println("\n=== Spring 中使用 Pattern Matching ===");

        ValidationResult validation = new Invalid("email", "邮箱格式不正确");

        String message = switch (validation) {
            case Valid(Object data) ->
                "验证通过: " + data;
            case Invalid(String field, String msg) ->
                "验证失败 - " + field + ": " + msg;
        };
        System.out.println("  " + message);


        DomainEvent event = new UserCreated("user123", "user@example.com");

        String eventLog = switch (event) {
            case UserCreated(String id, String email) ->
                "用户创建: " + id + " - " + email;
            case UserUpdated(String id, String field, String val) ->
                "用户更新: " + id + "." + field + " = " + val;
            case UserDeleted(String id) ->
                "用户删除: " + id;
        };
        System.out.println("  " + eventLog);
    }
}


