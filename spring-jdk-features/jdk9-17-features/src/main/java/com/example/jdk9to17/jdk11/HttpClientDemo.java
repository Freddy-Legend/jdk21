package com.example.jdk9to17.jdk11;

import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * JDK 11: 新的 HTTP Client API
 *
 * 标准化的 HTTP 客户端，支持 HTTP/2 和 WebSocket
 */
@Component
public class HttpClientDemo {

    public void demonstrate() {
        System.out.println("\n=== JDK 11: HTTP Client API ===");

        try {
            // 创建 HttpClient
            var client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

            // 创建 GET 请求
            var request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.github.com/repos/spring-projects/spring-boot"))
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/json")
                .GET()
                .build();

            // 同步发送请求
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Status Code: " + response.statusCode());
            System.out.println("Response (first 200 chars): " +
                response.body().substring(0, Math.min(200, response.body().length())) + "...");

            // 异步请求示例
            System.out.println("\n异步请求示例:");
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(body -> System.out.println("Async response received, length: " + body.length()))
                .join(); // 等待完成（演示用）

        } catch (Exception e) {
            System.err.println("HTTP 请求失败: " + e.getMessage());
        }
    }

    /**
     * JDK 11: String 新方法
     */
    public void demonstrateStringMethods() {
        System.out.println("\n=== JDK 11: String 新方法 ===");

        var text = "  Hello Spring Boot  ";
        System.out.println("原始字符串: '" + text + "'");
        System.out.println("strip(): '" + text.strip() + "'");
        System.out.println("isBlank(): " + "   ".isBlank());
        System.out.println("lines().count(): " + "line1\nline2\nline3".lines().count());
        System.out.println("repeat(3): " + "Java".repeat(3));
    }
}
