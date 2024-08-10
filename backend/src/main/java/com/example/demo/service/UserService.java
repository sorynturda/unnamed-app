package com.example.demo.service;


import com.example.demo.model.Match;
import com.example.demo.model.User;
import com.example.demo.model.UserDTO;
import com.example.demo.notification.Notification;
import com.example.demo.repo.MatchRepository;
import com.example.demo.repo.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepo;
    private final MatchRepository matchRepository;
    private final ValidatorImpl validator;

    public Notification<UserDTO> login(String username, String password) {
        Notification<UserDTO> userNotification = new Notification<>();
        Optional<User> user = userRepo.findById(username);
        if(user.isEmpty()){
            userNotification.setResult(null);

            userNotification.addError("User not found!");
            return userNotification;
        }

        if(!encodePassword(password).equals(user.get().getPassword())){
            userNotification.setResult(null);
            userNotification.addError("Incorrect password!");
            return userNotification;
        }
        userNotification.setSuccessMessage("da ma da");
        UserDTO userDTO = new UserDTO();
        userDTO.setName(user.get().getName());
        userDTO.setRole(user.get().getRole());
        userNotification.setResult(userDTO);
        return userNotification;
    }

    public Notification<List<Match>> getSchedule(String refereeName) {
        Notification<List<Match>> refereeNotification = new Notification<>();
        List<Match> matches = matchRepository.findAll();
        List<Match> refereeMatches = new ArrayList<>();
        for(Match match : matches) {
            if (match.getReferee()!=null && (match.getReferee().equals(refereeName))) {
                refereeMatches.add(match);

            }
        }
        refereeNotification.setResult(refereeMatches);
        return refereeNotification;
    }

    public Notification<List<Match>> getFiltered(String name) throws IOException {
        Notification<List<Match>> userNotification = new Notification<>();
        List<Match> match = new ArrayList<>(matchRepository.findAll());
        List<Match> filteredMatch = new ArrayList<>();
        System.out.println(name);
        for(Match m : match){
            if((m.getReferee()!=null && m.getReferee().toLowerCase().contains(name.toLowerCase())) || (m.getScore().contains(name)) || (m.getPlayer1()!= null && m.getPlayer1().toLowerCase().contains(name.toLowerCase())) || (m.getPlayer2()!= null && m.getPlayer2().toLowerCase().contains(name.toLowerCase()))){
                filteredMatch.add(m);
            }
        }
        if(filteredMatch.isEmpty()){
            userNotification.setResult(null);
            userNotification.addError("Match with that human not found!");
            return userNotification;
        }


        userNotification.setResult(filteredMatch);
        return userNotification;
    }

    public Notification<Match> setScore(String matchName, String score) {
        Notification<Match> scoreNotification = new Notification<>();
        Optional<Match> match = matchRepository.findById(matchName);
        if(match.isEmpty()){
            scoreNotification.setResult(null);
            scoreNotification.addError("Match not found!");
            return scoreNotification;
        }
        match.get().setScore(score);
        matchRepository.save(match.get());
        scoreNotification.setResult(match.get());
        return scoreNotification;
    }

    public Notification<User> register(User userToBeRegistered) {
        Notification<User> userNotification = new Notification<>();
        userNotification = validator.validateUser(userToBeRegistered);



        if(userNotification.hasErrors()){
            return userNotification;
        }
        userToBeRegistered.setPassword(encodePassword(userToBeRegistered.getPassword()));
        try{
            User registeredUser = userRepo.save(userToBeRegistered);
            userNotification.setResult(registeredUser);
        }
        catch (Exception e){
            userNotification = validator.validateUser(userToBeRegistered);
            userNotification.addError("The registration could not be performed");
            return userNotification;
        }

        return userNotification;
    }

    public String encodePassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


}
