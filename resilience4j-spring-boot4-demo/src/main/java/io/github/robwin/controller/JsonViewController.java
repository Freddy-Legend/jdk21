package io.github.robwin.controller;

import com.fasterxml.jackson.annotation.JsonView;
import io.github.robwin.domain.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/jsonview")
class JsonViewController {
    private final User user = new User("eric", "7!jd#h23","123@gmail.com");

    @GetMapping("/user")
    @JsonView(User.WithoutPasswordView.class)
    public User getUser() {
        return user;
    }

    @GetMapping("/userAndPassWord")
    @JsonView(User.WithPasswordView.class)
    public User getUserAndPassWord() {
        return user;
    }
    @GetMapping("/none")
    public User none() {
        return user;
    }

}
