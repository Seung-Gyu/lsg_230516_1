package untitled.domain;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "DepartmentView_table")
@Data
public class DepartmentView {

    @Id
    //@GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
}
