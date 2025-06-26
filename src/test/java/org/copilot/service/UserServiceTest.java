package org.copilot.service;

import org.copilot.model.User;
import org.copilot.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
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
    }

    @Test
    void testGetAllUserNames() {
        List<User> users = Arrays.asList(new User("Alice"), new User("Bob"));
        when(userRepository.findAll()).thenReturn(users);
        List<String> names = userService.getAllUserNames();
        assertEquals(2, names.size());
        assertEquals("Alice", names.get(0));
        assertEquals("Bob", names.get(1));
    }

    @Test
    void testSaveUser() {
        User user = new User("Charlie");
        when(userRepository.save(any(User.class))).thenReturn(user);
        userService.saveUser("Charlie");
        verify(userRepository, times(1)).save(any(User.class));
    }
}

