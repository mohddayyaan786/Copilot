package org.copilot.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void testNoArgsConstructorAndSetters() {
        User user = new User();
        user.setId(1L);
        user.setName("Alice");
        assertEquals(1L, user.getId());
        assertEquals("Alice", user.getName());
    }

    @Test
    void testAllArgsConstructor() {
        User user = new User("Bob");
        assertNull(user.getId()); // id should be null before persistence
        assertEquals("Bob", user.getName());
    }

    @Test
    void testEqualsAndHashCode() {
        User user1 = new User("Charlie");
        user1.setId(2L);
        User user2 = new User("Charlie");
        user2.setId(2L);
        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void testToString() {
        User user = new User("David");
        user.setId(3L);
        String str = user.toString();
        assertTrue(str.contains("David"));
        assertTrue(str.contains("3"));
    }
}

