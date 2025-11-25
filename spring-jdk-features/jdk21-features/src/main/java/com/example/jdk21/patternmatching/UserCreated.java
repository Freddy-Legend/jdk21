package com.example.jdk21.patternmatching;

public record UserCreated(String userId, String email) implements DomainEvent {
}
