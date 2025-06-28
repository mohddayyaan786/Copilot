package org.copilot.repository;

import org.copilot.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {
    private InMemoryUserRepository repo;

    @BeforeEach
    void setUp() {
        repo = new InMemoryUserRepository();
    }

    @Test
    void testSaveAndFindUser() {
        User user = new User("TestUser");
        user.setId(1L);
        repo.save(user);

        Optional<User> found = repo.findById(1L);
        assertTrue(found.isPresent());
        assertEquals("TestUser", found.get().getName());
    }

    @Test
    void testExistsById() {
        User user = new User("Exists");
        user.setId(2L);
        repo.save(user);
        assertTrue(repo.existsById(2L));
        assertFalse(repo.existsById(3L));
    }

    @Test
    void testFindAll() {
        User user1 = new User("A");
        user1.setId(1L);
        User user2 = new User("B");
        user2.setId(2L);
        repo.save(user1);
        repo.save(user2);
        List<User> all = repo.findAll();
        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(u -> u.getName().equals("A")));
        assertTrue(all.stream().anyMatch(u -> u.getName().equals("B")));
    }

    @Test
    void testFindAllById() {
        User user1 = new User("A");
        user1.setId(1L);
        User user2 = new User("B");
        user2.setId(2L);
        repo.save(user1);
        repo.save(user2);
        List<User> found = repo.findAllById(Arrays.asList(1L, 2L, 3L));
        assertEquals(2, found.size());
    }

    @Test
    void testCount() {
        assertEquals(0, repo.count());
        repo.save(new User("A") {{
            setId(1L);
        }});
        assertEquals(1, repo.count());
    }

    @Test
    void testDeleteById() {
        User user = new User("Del");
        user.setId(1L);
        repo.save(user);
        repo.deleteById(1L);
        assertFalse(repo.existsById(1L));
    }

    @Test
    void testDeleteUser() {
        User user = new User("Del");
        user.setId(1L);
        repo.save(user);
        repo.delete(user);
        assertFalse(repo.existsById(1L));
    }

    @Test
    void testDeleteAllById() {
        User user1 = new User("A");
        user1.setId(1L);
        User user2 = new User("B");
        user2.setId(2L);
        repo.save(user1);
        repo.save(user2);
        repo.deleteAllById(Arrays.asList(1L, 2L));
        assertEquals(0, repo.count());
    }

    @Test
    void testDeleteAllIterable() {
        User user1 = new User("A");
        user1.setId(1L);
        User user2 = new User("B");
        user2.setId(2L);
        repo.save(user1);
        repo.save(user2);
        repo.deleteAll(Arrays.asList(user1, user2));
        assertEquals(0, repo.count());
    }

    @Test
    void testDeleteAll() {
        repo.save(new User("A") {{
            setId(1L);
        }});
        repo.save(new User("B") {{
            setId(2L);
        }});
        repo.deleteAll();
        assertEquals(0, repo.count());
    }

    @Test
    void testSaveAll() {
        User user1 = new User("A");
        user1.setId(1L);
        User user2 = new User("B");
        user2.setId(2L);
        List<User> saved = repo.saveAll(Arrays.asList(user1, user2));
        assertEquals(2, saved.size());
        assertTrue(repo.existsById(1L));
        assertTrue(repo.existsById(2L));
    }

    // Minimal UserRepository interface for testing
    interface UserRepository {
        <S extends User> S save(S entity);

        <S extends User> List<S> saveAll(Iterable<S> entities);

        Optional<User> findById(Long id);

        boolean existsById(Long id);

        List<User> findAll();

        List<User> findAllById(Iterable<Long> ids);

        long count();

        void deleteById(Long id);

        void delete(User entity);

        void deleteAllById(Iterable<? extends Long> ids);

        void deleteAll(Iterable<? extends User> entities);

        void deleteAll();
    }

    // Simple in-memory implementation for testing
    static class InMemoryUserRepository implements UserRepository {
        private final Map<Long, User> store = new HashMap<>();

        @Override
        public <S extends User> S save(S entity) {
            store.put(entity.getId(), entity);
            return entity;
        }

        @Override
        public <S extends User> List<S> saveAll(Iterable<S> entities) {
            List<S> result = new ArrayList<>();
            for (S entity : entities) {
                save(entity);
                result.add(entity);
            }
            return result;
        }

        @Override
        public Optional<User> findById(Long id) {
            return Optional.ofNullable(store.get(id));
        }

        @Override
        public boolean existsById(Long id) {
            return store.containsKey(id);
        }

        @Override
        public List<User> findAll() {
            return new ArrayList<>(store.values());
        }

        @Override
        public List<User> findAllById(Iterable<Long> ids) {
            List<User> result = new ArrayList<>();
            for (Long id : ids) {
                if (store.containsKey(id)) {
                    result.add(store.get(id));
                }
            }
            return result;
        }

        @Override
        public long count() {
            return store.size();
        }

        @Override
        public void deleteById(Long id) {
            store.remove(id);
        }

        @Override
        public void delete(User entity) {
            store.remove(entity.getId());
        }

        @Override
        public void deleteAllById(Iterable<? extends Long> ids) {
            for (Long id : ids) {
                store.remove(id);
            }
        }

        @Override
        public void deleteAll(Iterable<? extends User> entities) {
            for (User entity : entities) {
                store.remove(entity.getId());
            }
        }

        @Override
        public void deleteAll() {
            store.clear();
        }
    }
}
