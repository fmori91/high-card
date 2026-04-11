package it.sara.demo.service.user.criteria;

import it.sara.demo.service.criteria.GenericCriteria;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriteriaGetUsers extends GenericCriteria {

    private String query;
    private int offset;
    private int limit;
    private OrderType order;

    @Getter
    public enum OrderType {
        BY_FIRSTNAME("by firstName"),
        BY_FIRSTNAME_DESC("by firstName desc"),
        BY_LASTNAME("by lastName"),
        BY_LASTNAME_DESC("by lastName desc");
        private final String displayName;

        OrderType(String displayName) {
            this.displayName = displayName;
        }
    }

}
