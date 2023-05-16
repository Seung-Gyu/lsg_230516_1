package untitled.domain;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "RankView_table")
@Data
public class RankView {

    @Id
    //@GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
}
