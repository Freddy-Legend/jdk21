package tech.legend.learn;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.net.URL;
import java.net.URLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * HTTP Client 功能对比示例 / HTTP Client Feature Comparison Example
 *
 * 本类演示 JDK 11 中引入的新的 HTTP Client API，对比传统的 URLConnection 方式
 * This class demonstrates the new HTTP Client API introduced in JDK 11,
 * comparing it with the traditional URLConnection approach
 *
 * 主要改进：
 * Key Improvements:
 * 1. 内置支持 HTTP/2 和 WebSocket
 *    Built-in support for HTTP/2 and WebSocket
 * 2. 同步和异步编程模型
 *    Synchronous and asynchronous programming models
 * 3. 更好的性能和易用性
 *    Better performance and usability
 * 4. 自动处理重定向
 *    Automatic redirect handling
 *
 * 版本历史：
 * Version History:
 * - JDK 11: HTTP Client 标准化 / HTTP Client (Standard)
 */
public class HttpClientComparison {

    public static void main(String[] args) {
        System.out.println("=== HTTP Client Comparison (JDK 8 URLConnection vs JDK 11 HttpClient) ===");

        // ============================================
        // JDK 8: 传统的 URLConnection 方式
        // JDK 8: Traditional URLConnection Approach
        // ============================================
        /*
         * 缺点：
         * Drawbacks:
         * 1. API 复杂且容易出错
         *    Complex and error-prone API
         * 2. 不支持 HTTP/2
         *    No HTTP/2 support
         * 3. 不支持异步操作
         *    No asynchronous operations
         * 4. 需要手动管理连接和流
         *    Manual connection and stream management required
         * 5. 不支持 WebSocket
         *    No WebSocket support
         */
        
        // 示例代码（注释掉避免实际网络请求）
        // Example code (commented out to avoid actual network requests)
        /*
        try {
            URL url = new URL("https://httpbin.org/get");
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("User-Agent", "JDK 8 Client");
            
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line).append("\n");
                }
                System.out.println("JDK 8 Response: " + response.toString().substring(0, 100) + "...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        // ============================================
        // JDK 11: 新的 HTTP Client API
        // JDK 11: New HTTP Client API
        // ============================================
        /*
         * 优点：
         * Advantages:
         * 1. 现代化的 API 设计
         *    Modern API design
         * 2. 内置 HTTP/2 支持
         *    Built-in HTTP/2 support
         * 3. 支持同步和异步操作
         *    Support for synchronous and asynchronous operations
         * 4. 自动处理重定向和认证
         *    Automatic redirect and authentication handling
         * 5. 内置 WebSocket 支持
         *    Built-in WebSocket support
         * 6. 更好的性能
         *    Better performance
         */

        // 同步请求示例（注释掉避免实际网络请求）
        // Synchronous request example (commented out to avoid actual network requests)
        /*
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://httpbin.org/get"))
                    .header("User-Agent", "JDK 11 Client")
                    .timeout(Duration.ofSeconds(30))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, 
                    HttpResponse.BodyHandlers.ofString());
            
            System.out.println("JDK 11 Status Code: " + response.statusCode());
            System.out.println("JDK 11 Response: " + response.body().substring(0, 100) + "...");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        */

        // 异步请求示例（注释掉避免实际网络请求）
        // Asynchronous request example (commented out to avoid actual network requests)
        /*
        HttpClient asyncClient = HttpClient.newHttpClient();
        HttpRequest asyncRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://httpbin.org/delay/1"))
                .build();

        asyncClient.sendAsync(asyncRequest, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(body -> System.out.println("JDK 11 Async Response: " + body.substring(0, 100) + "..."))
                .join();
        */

        System.out.println("HTTP Client 示例已展示 / HTTP Client examples shown");
        System.out.println("注意：为避免网络依赖，实际请求代码已被注释 / Note: Actual request code is commented out to avoid network dependency");
    }
}