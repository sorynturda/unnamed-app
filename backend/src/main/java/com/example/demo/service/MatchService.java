package com.example.demo.service;


import com.example.demo.model.Confirm;
import com.example.demo.model.Match;
import com.example.demo.model.MatchConfirm;
import com.example.demo.model.User;
import com.example.demo.notification.Notification;
import com.example.demo.repo.AdminRepository;
import com.example.demo.repo.ConfirmRepo;
import com.example.demo.repo.MatchRepository;
import com.example.demo.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepository matchRepository;
    private final ConfirmRepo confirmRepo;
    private final UserRepository userRepository;
    private final JavaMailSender emailSender;

    public Notification<Match> getMatch(String name) {
        Notification<Match> matchNotification = new Notification<>();
        Optional<Match> match = matchRepository.findById(name);
        if(match.isEmpty()){
            matchNotification.setResult(null);
            matchNotification.addError("User not found!");
            return matchNotification;
        }

        matchNotification.setResult(match.get());
        return matchNotification;
    }

    public Notification<List<Match>> getAll() {
        Notification<List<Match>> matchNotification = new Notification<>();

        List<Match> match = new ArrayList<>(matchRepository.findAll());
        //System.out.println(user);

        matchNotification.setResult(match);
        return matchNotification;
    }

    public Notification<List<MatchConfirm>> getRequests() {
        Notification<List<MatchConfirm>> matchNotification = new Notification<>();

        List<MatchConfirm> match = new ArrayList<>(confirmRepo.findAll());
        //System.out.println(user);

        matchNotification.setResult(match);
        return matchNotification;


    }

    public Notification<Match> participate(String name,String match) {
        Notification<Match> matchNotification = new Notification<>();
        Optional<Match> myMatch = matchRepository.findById(match);

        if(myMatch.isEmpty()){
            matchNotification.addError("Match not found!");
            matchNotification.setResult(null);
            return matchNotification;
        }
        System.out.println(name);
        if(myMatch.get().getPlayer1() == null || myMatch.get().getPlayer1().isEmpty()){
            myMatch.get().setPlayer1(name);
            matchNotification.setResult(myMatch.get());
        }
        else if(myMatch.get().getPlayer2() == null ||myMatch.get().getPlayer2().isEmpty()){
            myMatch.get().setPlayer2(name);
            matchNotification.setResult(myMatch.get());
        }
        else{

            matchNotification.setResult(null);
            return matchNotification;
        }

        matchRepository.save(myMatch.get());

        return matchNotification;
    }

    public Notification<MatchConfirm> matchRequest(String name,String match) {
        Notification<MatchConfirm> matchNotification = new Notification<>();

        MatchConfirm matchConfirm = new MatchConfirm();
        matchConfirm.setMatchId(match);
        matchConfirm.setPlayerName(name);

        sendEmailNotification(name,match);

        try {
            confirmRepo.save(matchConfirm);
            matchNotification.setResult(matchConfirm);
        }catch (Exception e){
            matchNotification.setResult(null);
            matchNotification.addError("Error saving to db");
            return matchNotification;
        }


        return matchNotification;
    }

    public Notification<Match> matchResponse(Confirm confirm, String match, String player) {
        Notification<Match> matchNotification = new Notification<>();

        if(confirm == Confirm.YES){
            matchNotification = participate(player,match);
        }
        else{
            matchNotification.addError("you were denied");
            matchNotification.setResult(null);

        }
        List<MatchConfirm> matchConfirm = new ArrayList<>();
        matchConfirm = confirmRepo.findAll();
        MatchConfirm matchToDelete = new MatchConfirm();
        for(MatchConfirm mc : matchConfirm){
            if(mc.getMatchId().equals(match) && mc.getPlayerName().equals(player)){

                matchToDelete.setMatchId(mc.getMatchId());
                matchToDelete.setPlayerName(mc.getPlayerName());
                matchToDelete.setId(mc.getId());

            }
        }
        confirmRepo.delete(matchToDelete);
        return matchNotification;

    }

    public Notification<Match> register(Match matchToRegister) {
        Notification<Match> matchNotification = new Notification<>();
        if(matchRepository.findById(matchToRegister.getName()).isPresent()){
            matchNotification.addError("A match with this name already exists!");
            matchNotification.setResult(null);
            return matchNotification;
        }

        try{
            Match registeredMatch = matchRepository.save(matchToRegister);
            matchNotification.setResult(registeredMatch);
        }
        catch (Exception e){
            matchNotification.setResult(null);
            matchNotification.addError("The registration could not be performed");
            return matchNotification;
        }

        return matchNotification;
    }

    private void sendEmailNotification(String username, String matchId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("senderformail1234@gmail.com");
        message.setSubject("New Participation Request");
        message.setText("A new participation request has been received from user: " + username + " for match ID: " + matchId);
        message.setFrom("senderformail1234@gmail.com");

        try {
            emailSender.send(message);
        } catch (MailException e) {
            // Log the error or handle it accordingly
            e.printStackTrace();
        }
    }

}
