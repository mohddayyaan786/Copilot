package org.copilot.service;

import org.copilot.model.User;
import org.copilot.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository);
    }

    @Test
    void getAllUserNames_returnsNames_whenUsersExist() {
        List<User> users = Arrays.asList(new User("Alice"), new User("Bob"));
        when(userRepository.findAll()).thenReturn(users);
        List<String> names = userService.getAllUserNames();
        assertEquals(Arrays.asList("Alice", "Bob"), names);
    }

    @Test
    void getAllUserNames_returnsEmptyList_whenNoUsers() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        List<String> names = userService.getAllUserNames();
        assertTrue(names.isEmpty());
    }

    @Test
    void saveUser_savesUserWithCorrectName() {
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        userService.saveUser("Charlie");
        verify(userRepository, times(1)).save(userCaptor.capture());
        assertEquals("Charlie", userCaptor.getValue().getName());
    }
}
