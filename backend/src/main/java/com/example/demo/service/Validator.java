package com.example.demo.service;


import com.example.demo.model.User;
import com.example.demo.notification.Notification;

public interface Validator {

    Notification<User> validateUser(User user);

}
