package it.sara.demo.service.result;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericPagedResult extends GenericResult {
    private long total;
    private int offset;
    private int limit;

}
