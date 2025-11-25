package com.example.jdk21.recordpatterns;

import org.springframework.stereotype.Component;

/**
 * JDK 21: Record Patterns
 *
 * Record Patterns allow deconstruction of record values in instanceof and switch expressions
 */
@Component
public class RecordPatternsDemo {

    // Define some record types for demonstration
    record Point(int x, int y) {}
    record Rectangle(Point upperLeft, Point lowerRight) {}
    record ColoredRectangle(Rectangle rect, String color) {}
    record Person(String name, int age) {}
    record Employee(Person person, String department, double salary) {}

    // For Spring Boot usage examples
    sealed interface ApiResponse permits Success, Error, Loading {}
    record Success(int code, String data) implements ApiResponse {}
    record Error(int code, String message) implements ApiResponse {}
    record Loading() implements ApiResponse {}

    sealed interface DomainEvent permits UserCreated, UserUpdated, UserDeleted {}
    record UserCreated(String userId, String email) implements DomainEvent {}
    record UserUpdated(String userId, String field, String value) implements DomainEvent {}
    record UserDeleted(String userId) implements DomainEvent {}

    public void demonstrate() {
        System.out.println("\n=== JDK 21: Record Patterns ===");

        // 1. Simple record pattern matching
        demonstrateSimplePatterns();

        // 2. Nested record patterns
        demonstrateNestedPatterns();

        // 3. Record patterns in switch expressions
        demonstrateRecordPatternsInSwitch();

        // 4. Guarded patterns with record patterns
        demonstrateGuardedPatterns();
    }

    private void demonstrateSimplePatterns() {
        System.out.println("\n1. Simple Record Patterns:");

        Object obj = new Point(10, 20);

        // Before JDK 21 - traditional approach
        if (obj instanceof Point) {
            Point p = (Point) obj;
            System.out.println("  Before JDK 21 - Point: (" + p.x() + ", " + p.y() + ")");
        }

        // With record patterns (JDK 21)
        if (obj instanceof Point(int x, int y)) {
            System.out.println("  With Record Patterns - Point: (" + x + ", " + y + ")");
        }
    }

    private void demonstrateNestedPatterns() {
        System.out.println("\n2. Nested Record Patterns:");

        ColoredRectangle cr = new ColoredRectangle(
            new Rectangle(new Point(0, 0), new Point(10, 5)), 
            "blue"
        );

        // Extracting nested values before JDK 21 would be cumbersome
        // With record patterns (JDK 21):
        if (cr instanceof ColoredRectangle(Rectangle(Point(int x1, int y1), Point(int x2, int y2)), String color)) {
            System.out.println("  Nested pattern - Color: " + color + 
                             ", Coordinates: (" + x1 + "," + y1 + ") to (" + x2 + "," + y2 + ")");
        }
    }

    private void demonstrateRecordPatternsInSwitch() {
        System.out.println("\n3. Record Patterns in Switch Expressions:");

        Object[] objects = {
            new Point(5, 10),
            new Rectangle(new Point(0, 0), new Point(20, 15)),
            new Person("Alice", 30),
            "Just a string"
        };

        for (Object obj : objects) {
            String result = switch (obj) {
                case Point(int x, int y) -> 
                    "Point at (" + x + ", " + y + ")";
                case Rectangle(Point(int x1, int y1), Point(int x2, int y2)) -> 
                    "Rectangle from (" + x1 + "," + y1 + ") to (" + x2 + "," + y2 + ")";
                case Person(String name, int age) -> 
                    "Person: " + name + " (age: " + age + ")";
                default -> 
                    "Other: " + obj.toString();
            };
            
            System.out.println("  " + result);
        }
    }

    private void demonstrateGuardedPatterns() {
        System.out.println("\n4. Guarded Record Patterns:");

        Object[] employees = {
            new Employee(new Person("Alice", 25), "Engineering", 80000),
            new Employee(new Person("Bob", 35), "Engineering", 120000),
            new Employee(new Person("Charlie", 45), "Management", 150000),
            new Person("David", 30)
        };

        for (Object obj : employees) {
            String description = switch (obj) {
                case Employee(Person(String name, int age), String dept, double salary) 
                     when age < 30 && dept.equals("Engineering") -> 
                    name + " is a junior engineer (age: " + age + ", salary: " + salary + ")";
                case Employee(Person(String name, int age), String dept, double salary) 
                     when age >= 30 && dept.equals("Engineering") -> 
                    name + " is a senior engineer (age: " + age + ", salary: " + salary + ")";
                case Employee(Person(String name, int age), String dept, double salary) 
                     when dept.equals("Management") -> 
                    name + " is in management (age: " + age + ", salary: " + salary + ")";
                case Employee e -> 
                    "Employee: " + e.person().name() + " in " + e.department();
                case Person(String name, int age) -> 
                    "Person: " + name + " (age: " + age + ")";
                default -> 
                    "Other: " + obj.toString();
            };
            
            System.out.println("  " + description);
        }
    }

    /**
     * Spring Boot usage examples
     */
    public void demonstrateSpringUsage() {
        System.out.println("\n=== Record Patterns in Spring Boot ===");

        // API Response handling
        ApiResponse response = new Success(200, "User data retrieved");

        String result = switch (response) {
            case Success(int code, String data) -> 
                "Success: " + code + " - " + data;
            case Error(int code, String message) when code >= 500 -> 
                "Server Error: " + code + " - " + message;
            case Error(int code, String message) -> 
                "Client Error: " + code + " - " + message;
            case Loading() -> 
                "Loading...";
            default -> 
                "Unknown response type: " + response.getClass().getSimpleName();
        };

        System.out.println("  API Response: " + result);

        // Event processing
        DomainEvent event = new UserCreated("user-123", "user@example.com");

        String eventLog = switch (event) {
            case UserCreated(String userId, String email) -> 
                "User created: " + userId + " (" + email + ")";
            case UserUpdated(String userId, String field, String value) -> 
                "User updated: " + userId + "." + field + " = " + value;
            case UserDeleted(String userId) -> 
                "User deleted: " + userId;
            default -> 
                "Unknown event type: " + event.getClass().getSimpleName();
        };

        System.out.println("  Event processed: " + eventLog);
    }
}