package org.copilot.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.copilot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/names")
    public List<String> getUserNames() {
        return userService.getAllUserNames();
    }
}
