package com.example.jdk21.patternmatching;

public sealed interface DomainEvent permits UserCreated, UserDeleted, UserUpdated {
}
