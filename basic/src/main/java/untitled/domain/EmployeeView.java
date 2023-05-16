package untitled.domain;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "EmployeeView_table")
@Data
public class EmployeeView {

    @Id
    //@GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
}
