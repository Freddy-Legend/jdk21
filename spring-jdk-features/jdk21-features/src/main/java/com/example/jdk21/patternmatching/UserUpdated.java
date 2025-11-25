package com.example.jdk21.patternmatching;

public record UserUpdated(String userId, String field, String value) implements DomainEvent {
}
