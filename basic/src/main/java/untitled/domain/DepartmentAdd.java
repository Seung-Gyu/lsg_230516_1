package untitled.domain;

import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

@Data
@ToString
public class DepartmentAdd extends AbstractEvent {

    private Long id;
    private String name;

    public DepartmentAdd(Department aggregate) {
        super(aggregate);
    }

    public DepartmentAdd() {
        super();
    }
}
