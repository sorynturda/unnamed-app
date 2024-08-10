package com.example.demo.controller;


import com.example.demo.model.Match;
import com.example.demo.model.User;
import com.example.demo.model.UserDTO;
import com.example.demo.notification.Notification;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static com.example.demo.constant.UrlMapping.USERS;


@RestController
@RequiredArgsConstructor
@RequestMapping(USERS)
public class UserController {
    private final UserService userService;
    // @CrossOrigin(origins = "http://localhost:40404")
    @CrossOrigin(origins = "*")
    @GetMapping("/login")
    public ResponseEntity<Notification<UserDTO>> login(String username, String password){
        System.out.println(username);
        System.out.println(password);
        Notification<UserDTO> user = userService.login(username, password);

        if (user.getResult() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(user);
        }

        return ResponseEntity.ok(user);
    }
    @CrossOrigin(origins = "*")
    @GetMapping("/getSchedule")
    public ResponseEntity<Notification<List<Match>>> getSchedule(String refereeName){
        Notification<List<Match>> match = userService.getSchedule(refereeName);

        if (match.getResult() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(match);
        }

        return ResponseEntity.ok(match);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getFiltered")
    public ResponseEntity<Notification<List<Match>>> getFiltered(String name) throws IOException {

        Notification<List<Match>> match = userService.getFiltered(name);

        if (match.getResult() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(match);
        }

        return ResponseEntity.ok(match);
    }

    @CrossOrigin(origins = "*")
    @PutMapping("/setScore")
    public ResponseEntity<Notification<Match>> setScore(String matchName,String score){
        Notification<Match> match = userService.setScore(matchName,score);

        if (match.getResult() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(match);
        }

        return ResponseEntity.ok(match);
    }


    @CrossOrigin(origins = "*")
    @PostMapping("/register")
    public ResponseEntity<Notification<User>> register(@RequestBody User registerInfo){
            User userToBeRegistered = User.builder()
                .name(registerInfo.getName())
                .password(registerInfo.getPassword())
                .role(registerInfo.getRole())
                .email(registerInfo.getEmail())
                .build();
        System.out.println(registerInfo.getName());
        Notification<User> user = userService.register(userToBeRegistered);

        if (user.getResult() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(user);
        }

        return ResponseEntity.ok(user);
    }

}
