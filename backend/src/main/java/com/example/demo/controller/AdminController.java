package com.example.demo.controller;


import com.example.demo.model.Match;
import com.example.demo.model.User;
import com.example.demo.model.UserDTO;
import com.example.demo.notification.Notification;
import com.example.demo.service.AdminService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static com.example.demo.constant.UrlMapping.ADMINS;


@RestController
@RequiredArgsConstructor
@RequestMapping(ADMINS)
public class AdminController {
    private final UserService userService;
    private final AdminService adminService;
    @CrossOrigin(origins = "*")
    @GetMapping("/getInfo")
    public ResponseEntity<Notification<User>> getUser(String username){

        Notification<User> user = adminService.getUser(username);

        if (user.getResult() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(user);
        }

        return ResponseEntity.ok(user);
    }
    @CrossOrigin(origins = "*")
    @GetMapping("/getAll")
    public ResponseEntity<Notification<List<UserDTO>>> getAll(){

        Notification<List<UserDTO>> user = adminService.getAll();

        if (user.getResult() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(user);
        }

        return ResponseEntity.ok(user);
    }
    @CrossOrigin(origins = "*")
    @GetMapping("/getFiltered")
    public ResponseEntity<Notification<List<Match>>> getFiltered(String name) throws IOException {

        Notification<List<Match>> match = adminService.getFiltered(name);

        if (match.getResult() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(match);
        }

        return ResponseEntity.ok(match);
    }
    @CrossOrigin(origins = "*")
    @PutMapping("/updateUser")
    public ResponseEntity<Notification<User>> updateUser(@RequestBody User updateInfo){

        Notification<User> user = adminService.updateUser(updateInfo);

        if (user.getResult() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(user);
        }

        return ResponseEntity.ok(user);
    }
    @CrossOrigin(origins = "*")
    @PutMapping("/deleteUser")
    public ResponseEntity<Notification<User>> deleteUser(String deleteUserInfo){

        Notification<User> user = adminService.deleteUser(deleteUserInfo);

        if (user.getResult() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(user);
        }

        return ResponseEntity.ok(user);
    }

}
