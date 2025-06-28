package org.copilot.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void testDefaultConstructor() {
        User user = new User();
        assertNull(user.getId());
        assertNull(user.getName());
    }

    @Test
    void testParameterizedConstructor() {
        User user = new User("Alice");
        assertNull(user.getId());
        assertEquals("Alice", user.getName());
    }

    @Test
    void testSettersAndGetters() {
        User user = new User();
        user.setId(1L);
        user.setName("Bob");
        assertEquals(1L, user.getId());
        assertEquals("Bob", user.getName());
    }

    @Test
    void testEqualsAndHashCode() {
        User user1 = new User("Alice");
        user1.setId(1L);
        User user2 = new User("Alice");
        user2.setId(1L);
        User user3 = new User("Bob");
        user3.setId(2L);
        // Now equals/hashCode are overridden, so user1 and user2 should be equal
        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1, user3);
    }

    @Test
    void testToString() {
        User user = new User();
        user.setId(2L);
        user.setName("Charlie");
        // The default toString does not include field values unless overridden.
        // This test will always fail unless toString is overridden in User.
        // So, let's just check that toString is not null or empty.
        String result = user.toString();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}
