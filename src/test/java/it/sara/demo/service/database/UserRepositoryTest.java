package it.sara.demo.service.database;

import it.sara.demo.service.database.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserRepositoryTest {

    private final UserRepository repository = new UserRepository();
    private List<User> snapshot;

    @BeforeEach
    void takeSnapshot() {
        snapshot = new ArrayList<>(FakeDatabase.TABLE_USER);
    }

    @AfterEach
    void restoreSnapshot() {
        FakeDatabase.TABLE_USER.clear();
        FakeDatabase.TABLE_USER.addAll(snapshot);
    }

    private User buildUser(String first, String last) {
        User user = new User();
        user.setFirstName(first);
        user.setLastName(last);
        user.setEmail(first.toLowerCase() + "@example.com");
        user.setPhoneNumber("+393330000000");
        return user;
    }

    @Test
    void save_shouldAssignGuidAndAppendToTable() {
        int before = FakeDatabase.TABLE_USER.size();
        User user = buildUser("Anna", "Bianchi");

        boolean saved = repository.save(user);

        assertTrue(saved);
        assertNotNull(user.getGuid());
        assertFalse(user.getGuid().isEmpty());
        assertEquals(before + 1, FakeDatabase.TABLE_USER.size());
    }

    @Test
    void save_multipleCalls_shouldAssignDistinctGuids() {
        User a = buildUser("Anna", "Bianchi");
        User b = buildUser("Beatrice", "Neri");

        repository.save(a);
        repository.save(b);

        assertNotNull(a.getGuid());
        assertNotNull(b.getGuid());
        assertFalse(a.getGuid().equals(b.getGuid()));
    }

    @Test
    void getByGuid_whenPresent_shouldReturnUser() {
        User user = buildUser("Carlo", "Verdi");
        repository.save(user);

        Optional<User> found = repository.getByGuid(user.getGuid());

        assertTrue(found.isPresent());
        assertEquals(user.getGuid(), found.get().getGuid());
        assertEquals("Carlo", found.get().getFirstName());
    }

    @Test
    void getByGuid_whenMissing_shouldReturnEmpty() {
        Optional<User> found = repository.getByGuid("non-existing-guid");
        assertFalse(found.isPresent());
    }

    @Test
    void getAll_shouldReturnUnderlyingTable() {
        List<User> all = repository.getAll();
        assertNotNull(all);
        assertEquals(FakeDatabase.TABLE_USER.size(), all.size());
    }

    @Test
    void getAll_afterSave_shouldContainNewUser() {
        int before = repository.getAll().size();
        User user = buildUser("Dario", "Gialli");

        repository.save(user);

        assertEquals(before + 1, repository.getAll().size());
    }
}
