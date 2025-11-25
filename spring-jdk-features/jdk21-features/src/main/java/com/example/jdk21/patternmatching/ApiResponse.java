package com.example.jdk21.patternmatching;

public sealed interface ApiResponse permits Error, Loading, Success {
}
