package io.github.robwin.domain;

import com.fasterxml.jackson.annotation.JsonView;

public class User {

    public String getEmail() {
        return email;
    }

    public interface WithoutPasswordView {
    }

    ;

    public interface WithPasswordView extends WithoutPasswordView {
    }

    ;

    private String username;
    private String password;
    private String email;
    public User() {
    }

    public User(String username, String password,String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    @JsonView(WithoutPasswordView.class)
    public String getUsername() {
        return this.username;
    }

    @JsonView(WithPasswordView.class)
    public String getPassword() {
        return this.password;
    }
}
