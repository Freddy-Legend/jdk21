package com.example.jdk21.patternmatching;

public record Error(int code, String message) implements ApiResponse {
}