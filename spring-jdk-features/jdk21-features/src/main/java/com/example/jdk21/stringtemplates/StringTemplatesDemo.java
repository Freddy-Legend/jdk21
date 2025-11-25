package com.example.jdk21.stringtemplates;

import org.springframework.stereotype.Component;

/**
 * JDK 21: String Templates (Preview Feature)
 * <p>
 * String templates allow embedding expressions within string literals
 * Note: This is a preview feature and requires enabling preview features
 */
@Component
public class StringTemplatesDemo {

    public void demonstrate() {
        System.out.println("\n=== JDK 21: String Templates (Preview) ===");

        // Note: String templates are a preview feature in JDK 21
        // They require enabling preview features with --enable-preview flag

        String name = "Spring Boot";
        int version = 3;
        double score = 9.5;

        // Traditional string formatting
        String traditional = String.format("Framework: %s, Version: %d, Score: %.1f", name, version, score);
        System.out.println("Traditional formatting: " + traditional);

        // Using text blocks with interpolation would be something like:
        // String template = STR."Framework: \{name}, Version: \{version}, Score: \{score}";
        // But this syntax is not yet supported in standard Java

        System.out.println("""
                String templates are a preview feature in JDK 21.
                They allow embedding expressions directly in string literals like:
                
                String name = "Spring Boot";
                int version = 3;
                String message = STR."Welcome to \\{name} version \\{version}!";
                
                This feature simplifies string interpolation compared to traditional approaches.
                """);

        // Show what the feature will enable when available
        demonstrateFutureUsage();
    }

    private void demonstrateFutureUsage() {
        System.out.println("\nFuture usage example (conceptual):");
        System.out.println("""
                // This is how String Templates will work (preview feature):
                String name = "Spring Boot";
                int version = 3;
                LocalDate releaseDate = LocalDate.now();
                
                // Instead of:
                String message1 = String.format(
                    "Welcome to %s version %d, released on %s!", 
                    name, version, releaseDate.toString());
                
                // We'll be able to write:
                // String message2 = STR."Welcome to \\{name} version \\{version}, released on \\{releaseDate}!";
                
                // Or with multi-line text blocks:
                // String json = STR.\"\"\"
                //     {
                //         "name": "\\{name}",
                //         "version": \\{version},
                //         "releaseDate": "\\{releaseDate}"
                //     }
                //     \"\"\";
                """);
    }
}