package com.example.demo.controller;

import com.example.demo.model.Confirm;
import com.example.demo.model.Match;
import com.example.demo.model.MatchConfirm;
import com.example.demo.model.User;
import com.example.demo.notification.Notification;
import com.example.demo.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.constant.UrlMapping.MATCHES;


@RestController
@RequiredArgsConstructor
@RequestMapping(MATCHES)
public class MatchController {
    private final MatchService matchService;

    @CrossOrigin(origins = "*")
    @PostMapping("/register")
    public ResponseEntity<Notification<Match>> register(@RequestBody Match registerInfo){
        Match matchToRegister = Match.builder()
                .name(registerInfo.getName())
                .date(registerInfo.getDate())
                .player1(registerInfo.getPlayer1())
                .player2(registerInfo.getPlayer2())
                .referee(registerInfo.getReferee())
                .score(registerInfo.getScore())
                .build();
        //System.out.println(registerInfo);
        Notification<Match> match = matchService.register(matchToRegister);

        if (match.getResult() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(match);
        }

        return ResponseEntity.ok(match);
    }
    @CrossOrigin(origins = "*")
    @PostMapping("/participate")
    public ResponseEntity<Notification<Match>> participate(String playerName, String matchName){


        Notification<Match> match = matchService.participate(playerName,matchName);

        if (match.getResult() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(match);
        }
        return ResponseEntity.ok(match);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/matchRequest")
    public ResponseEntity<Notification<MatchConfirm>> matchRequest(String playerName, String matchName){
        System.out.println(playerName);
        System.out.println(matchName);
        Notification<MatchConfirm> match = matchService.matchRequest(playerName,matchName);

        if (match.getResult() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(match);
        }
        return ResponseEntity.ok(match);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getRequests")
    public ResponseEntity<Notification<List<MatchConfirm>>> getRequests(){

        Notification<List<MatchConfirm>> match = matchService.getRequests();

        if (match.getResult() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(match);
        }
        return ResponseEntity.ok(match);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/matchResponse")
    public ResponseEntity<Notification<Match>> matchResponse(Confirm confirm, String matchId, String player){

        Notification<Match> match = matchService.matchResponse(confirm,matchId,player);

        if (match.getResult() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(match);
        }
        return ResponseEntity.ok(match);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getAll")
    public ResponseEntity<Notification<List<Match>>> getAll(){

        Notification<List<Match>> match = matchService.getAll();

        if (match.getResult() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(match);
        }

        return ResponseEntity.ok(match);
    }
    @CrossOrigin(origins = "*")
    @GetMapping("/getMatch")
    public ResponseEntity<Notification<Match>> getMatch(String matchName){

        Notification<Match> match = matchService.getMatch(matchName);

        if (match.getResult() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(match);
        }

        return ResponseEntity.ok(match);
    }
}
