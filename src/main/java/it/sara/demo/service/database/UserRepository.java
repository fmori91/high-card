package it.sara.demo.service.database;

import it.sara.demo.service.database.model.User;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class UserRepository {

    public boolean save(User user) {
        user.setGuid(java.util.UUID.randomUUID().toString());
        FakeDatabase.TABLE_USER.add(user);
        return true;
    }

    public Optional<User> getByGuid(String guid) {
        return FakeDatabase.TABLE_USER.stream().filter(u -> u.getGuid().equals(guid)).findFirst();
    }

    public List<User> getAll() {
        return FakeDatabase.TABLE_USER;
    }


    public long countUsersByQuery(String likePattern) {
        if (likePattern == null) {
            return FakeDatabase.TABLE_USER.size();
        }
        String q = likePattern.replace("%", "").toLowerCase();
        return FakeDatabase.TABLE_USER.stream()
                .filter(u -> contains(u.getFirstName(), q)
                        || contains(u.getLastName(), q)
                        || contains(u.getEmail(), q))
                .count();
    }

    public List<User> searchUsersByQuery(String likePattern,
                                         String sortColumn,
                                         boolean sortDesc,
                                         int offset,
                                         int limit) {
        String q = likePattern == null ? null : likePattern.replace("%", "").toLowerCase();

        // Filtro case-insensitive su firstName/lastName/email
        List<User> filtered = FakeDatabase.TABLE_USER.stream()
                .filter(u -> q == null
                        || contains(u.getFirstName(), q)
                        || contains(u.getLastName(), q)
                        || contains(u.getEmail(), q))
                .toList();

        // Ordinamento basato su OrderType (colonne whiteliste)
        Comparator<User> comp = null;
        if ("first_name".equalsIgnoreCase(sortColumn)) {
            comp = Comparator.comparing(u -> u.getFirstName() == null ? "" : u.getFirstName(),
                    String.CASE_INSENSITIVE_ORDER);
        } else if ("last_name".equalsIgnoreCase(sortColumn)) {
            comp = Comparator.comparing(u -> u.getLastName() == null ? "" : u.getLastName(),
                    String.CASE_INSENSITIVE_ORDER);
        }
        if (comp != null) {
            if (sortDesc) comp = comp.reversed();
            filtered = filtered.stream().sorted(comp).toList();
        }

        // Paginazione offset/limit
        int from = Math.max(0, offset);
        int to = Math.min(filtered.size(), from + Math.max(1, limit));
        if (from >= filtered.size()) return java.util.Collections.emptyList();
        return filtered.subList(from, to);
    }

    // helper per il filtro contains case-insensitive
    private boolean contains(String s, String q) {
         return s != null && q != null && !q.isEmpty()
            && s.toLowerCase().contains(q);
    }

}
