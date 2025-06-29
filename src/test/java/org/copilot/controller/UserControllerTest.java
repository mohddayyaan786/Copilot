package org.copilot.controller;

import org.copilot.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;
    private AutoCloseable mocks;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @AfterEach
    void tearDown() throws Exception {
        if (mocks != null) mocks.close();
    }

    @Test
    void getUserNames_shouldReturnListOfNames() throws Exception {
        List<String> names = Arrays.asList("Alice", "Bob");
        when(userService.getAllUserNames()).thenReturn(names);
        mockMvc.perform(get("/users/names").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Alice"))
                .andExpect(jsonPath("$[1]").value("Bob"));
        verify(userService, times(1)).getAllUserNames();
    }

    @Test
    void getUserNames_shouldReturnEmptyList() throws Exception {
        when(userService.getAllUserNames()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/users/names").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(userService, times(1)).getAllUserNames();
    }

    @Test
    void getUserNames_shouldHandleNullResponse() throws Exception {
        when(userService.getAllUserNames()).thenReturn(null);
        mockMvc.perform(get("/users/names").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("")); // Expect empty string for null response
        verify(userService, times(1)).getAllUserNames();
    }
}
