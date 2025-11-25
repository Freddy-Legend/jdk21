package com.example.jdk9to17.jdk15;

import org.springframework.stereotype.Component;

/**
 * JDK 15: Text Blocks
 *
 * Text Blocks allow multi-line string literals without escape sequences
 */
@Component
public class TextBlocksDemo {

    public void demonstrate() {
        System.out.println("\n=== JDK 15: Text Blocks ===");

        // Traditional string concatenation
        String jsonOld = "{\n" +
                "  \"name\": \"Spring Boot\",\n" +
                "  \"version\": \"3.2.0\",\n" +
                "  \"features\": [\"JDK 21\", \"Virtual Threads\"]\n" +
                "}";

        // Using text blocks
        String jsonNew = """
                {
                  "name": "Spring Boot",
                  "version": "3.2.0",
                  "features": ["JDK 21", "Virtual Threads"]
                }
                """;

        System.out.println("Traditional JSON:\n" + jsonOld);
        System.out.println("\nText Block JSON:\n" + jsonNew);

        // SQL query example
        String sql = """
                SELECT u.name, u.email, p.title
                FROM users u
                JOIN posts p ON u.id = p.user_id
                WHERE u.active = true
                ORDER BY p.created_at DESC
                LIMIT 10
                """;

        System.out.println("\nSQL Query:\n" + sql);

        // HTML example
        String html = """
                <html>
                    <head>
                        <title>Spring JDK Features</title>
                    </head>
                    <body>
                        <h1>JDK Features Demo</h1>
                        <p>This is a demo of JDK features with Spring Boot</p>
                    </body>
                </html>
                """;

        System.out.println("\nHTML Template:\n" + html);

        // Using escape sequences in text blocks
        String code = """
                public class HelloWorld {
                    public static void main(String[] args) {
                        System.out.println("Hello, World!");
                    }
                }
                """;

        System.out.println("\nJava Code:\n" + code);
    }
}