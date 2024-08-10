package com.example.demo.service;



import com.example.demo.model.User;
import com.example.demo.notification.Notification;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor
@Service
public class ValidatorImpl implements Validator {
    private Notification<User> userNotification;

    private boolean isName(String name) {
        String regex = "^[a-zA-Z]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    private boolean isGoodPassword(String password) {
        boolean hasEightCharacters = password.length() >= 8;
        if(!hasEightCharacters) userNotification.addError("The password has less than 8 characters!");

        boolean containsUppercase = !password.equals(password.toLowerCase());
        if(!containsUppercase) userNotification.addError("The password has no uppercase letter!");

        String symbols = "!@#$%^&*()-_+=<>,.?/\\|";
        boolean containsSymbol = false;
        for (char c : password.toCharArray()) {
            if (symbols.contains(String.valueOf(c))) {
                containsSymbol = true;
                break;
            }
        }
        if(!containsSymbol) userNotification.addError("The password has no symbol!");

        return hasEightCharacters && containsUppercase && containsSymbol;
    }

    @Override
    public Notification<User> validateUser(User user) {
        userNotification = new Notification<>();
        if(!isName(user.getName())) userNotification.addError("The username is not valid!");
        isGoodPassword(user.getPassword());
        return userNotification;
    }

    private boolean isNumber(String numberString){
        return numberString.matches("[0-9]+");
    }

}
