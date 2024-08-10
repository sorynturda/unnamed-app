package com.example.demo.service;


import com.example.demo.model.Match;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.model.UserDTO;
import com.example.demo.notification.Notification;
import com.example.demo.repo.AdminRepository;
import com.example.demo.repo.MatchRepository;
import com.example.demo.repo.UserRepository;
import jdk.swing.interop.SwingInterOpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final MatchRepository matchRepository;

    public Notification<User> getUser(String username) {
        Notification<User> userNotification = new Notification<>();
        Optional<User> user = adminRepository.findById(username);
        if(user.isEmpty()){
            userNotification.setResult(null);
            userNotification.addError("User not found!");
            return userNotification;
        }
        userNotification.setSuccessMessage("DA");
        userNotification.setResult(user.get());
        return userNotification;
    }
    public Notification<List<UserDTO>> getAll() {
        Notification<List<UserDTO>> userNotification = new Notification<>();

        List<UserDTO> users = new ArrayList<>();
        List<User> user = new ArrayList<>(adminRepository.findAll());

        for(User u : user){
            UserDTO userDTO = new UserDTO();
            userDTO.setRole(u.getRole());
            userDTO.setName(u.getName());
            users.add(userDTO);
        }
        //System.out.println(user);

        userNotification.setResult(users);
        return userNotification;
    }

    public Notification<List<Match>> getFiltered(String name) throws IOException {
        Notification<List<Match>> userNotification = new Notification<>();
        List<Match> match = new ArrayList<>(matchRepository.findAll());
        List<Match> filteredMatch = new ArrayList<>();
        for(Match m : match){
            if((m.getReferee()!=null && m.getReferee().toLowerCase().contains(name.toLowerCase())) || (m.getPlayer1()!= null && m.getPlayer1().toLowerCase().contains(name.toLowerCase())) || (m.getPlayer2()!= null && m.getPlayer2().toLowerCase().contains(name.toLowerCase()))){
                filteredMatch.add(m);
            }
        }
        if(filteredMatch.isEmpty()){
            userNotification.setResult(null);
            userNotification.addError("Match with that human not found!");
            return userNotification;
        }

        String csvData = generateCSV(filteredMatch);
        String txtData = generateTXT(filteredMatch);

        saveToFile("C:\\AC_UTCN\\SD_Lab\\A1\\backend\\matches.csv", csvData);
        saveToFile("C:\\AC_UTCN\\SD_Lab\\A1\\backend\\matches.txt", txtData);

        userNotification.setResult(filteredMatch);
        return userNotification;
    }
    public static String generateCSV(List<Match> matches) {
        StringWriter writer = new StringWriter();

        // Write CSV header
        writer.append("Match,Date,Referee\n");

        // Write match information
        for (Match match : matches) {
            writer.append(formatCSVField(match.getName())).append(",");
            writer.append(formatCSVField(match.getDate().toString())).append(",");
            writer.append(formatCSVField(match.getReferee())).append(",");
        }

        return writer.toString();
    }

    private static String formatCSVField(String field) {
        if (field.contains(",") || field.contains("\"")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        } else {
            return field;
        }
    }

    public static String generateTXT(List<Match> matches) {
        StringBuilder builder = new StringBuilder();

        // Write match information
        for (Match match : matches) {
            builder.append("Name: ").append(match.getName()).append("\n");
            builder.append("Date: ").append(match.getDate()).append("\n");
            builder.append("Referee: ").append(match.getReferee()).append("\n\n");
        }

        return builder.toString();
    }

    public static void saveToFile(String fileName, String data) throws IOException {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(data);
        }
    }



    public Notification<User> updateUser(User userToBeUpdated) {
        Notification<User> userNotification = new Notification<>();
        Optional<User> myUser = adminRepository.findById(userToBeUpdated.getName());
        if(myUser.isEmpty()){
            userNotification.addError("User not found!");
            userNotification.setResult(null);
            return userNotification;
        }
        myUser.get().setPassword(encodePassword(myUser.get().getPassword()));
        myUser.get().setRole(userToBeUpdated.getRole());
        try{
            adminRepository.save(myUser.get());
            userNotification.setResult(myUser.get());
            userNotification.setSuccessMessage("Successfully updated!");
        }
        catch (Exception e){
            userNotification.setResult(null);
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

    public Notification<User> deleteUser(String deleteUserInfo) {
        Notification<User> userNotification = new Notification<>();
        Optional<User> myUser = adminRepository.findById(deleteUserInfo);
        if(myUser.isEmpty()){
            userNotification.addError("User not found!");
            userNotification.setResult(null);
            return userNotification;
        }
        User concreteUser = myUser.get();
        adminRepository.delete(concreteUser);
        userNotification.setResult(concreteUser);
        userNotification.setSuccessMessage("Successfully deleted!");
        return userNotification;
    }




}
