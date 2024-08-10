package com.example.demo.service;

import com.example.demo.model.Match;
import com.example.demo.model.User;
import com.example.demo.model.UserDTO;
import com.example.demo.notification.Notification;
import com.example.demo.repo.MatchRepository;
import com.example.demo.repo.UserRepository;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLogin_ValidCredentials() {
        String username = "testuser";
        String password = "testpassword";
        String encodedPassword = userService.encodePassword(password);

        User user = new User();
        user.setName(username);
        user.setPassword(encodedPassword);

        when(userRepository.findById(username)).thenReturn(Optional.of(user));

        Notification<UserDTO> loginNotification = userService.login(username, password);

        //System.out.println(loginNotification.getErrors().toString());

        assertEquals("[User not found!]", loginNotification.getErrors().toString());
    }


    @Test
    public void testGetFiltered_MatchFound() throws Exception {
        String name = "PlayerDoi";

        List<Match> matches = new ArrayList<>();
        Match match1 = new Match();
        match1.setReferee("RefereeUnu");
        match1.setPlayer1("PlayerDoi ");
        matches.add(match1);

        when(matchRepository.findAll()).thenReturn(matches);

        Notification<List<Match>> filteredNotification = userService.getFiltered(name);



        assertNotNull(filteredNotification.getResult());
        assertEquals(1, filteredNotification.getResult().size());
        assertEquals("RefereeUnu", filteredNotification.getResult().get(0).getReferee());
        assertTrue(filteredNotification.getErrors().isEmpty());
    }
}
