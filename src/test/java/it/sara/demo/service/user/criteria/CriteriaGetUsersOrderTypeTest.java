package it.sara.demo.service.user.criteria;

import it.sara.demo.service.user.criteria.CriteriaGetUsers.OrderType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CriteriaGetUsersOrderTypeTest {

    @Test
    void byFirstName_shouldHaveCorrectDisplayName() {
        assertEquals("by firstName", OrderType.BY_FIRSTNAME.getDisplayName());
    }

    @Test
    void byFirstNameDesc_shouldHaveCorrectDisplayName() {
        assertEquals("by firstName desc", OrderType.BY_FIRSTNAME_DESC.getDisplayName());
    }

    @Test
    void byLastName_shouldHaveCorrectDisplayName() {
        assertEquals("by lastName", OrderType.BY_LASTNAME.getDisplayName());
    }

    @Test
    void byLastNameDesc_shouldHaveCorrectDisplayName() {
        assertEquals("by lastName desc", OrderType.BY_LASTNAME_DESC.getDisplayName());
    }

    @Test
    void allDisplayNames_shouldBeUnique() {
        long uniqueCount = Arrays.stream(OrderType.values())
                .map(OrderType::getDisplayName)
                .distinct()
                .count();
        assertEquals(OrderType.values().length, uniqueCount);
    }
}
