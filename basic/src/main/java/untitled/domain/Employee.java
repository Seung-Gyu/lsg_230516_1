package untitled.domain;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import untitled.BasicApplication;
import untitled.domain.EmployeeAdd;

@Entity
@Table(name = "Employee_table")
@Data
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String userId;

    @Embedded
    private RankId rankId;

    @Embedded
    private DepartmentId departmentId;

    @ElementCollection
    private List<EmployeeUser> employeeUser;

    @PostPersist
    public void onPostPersist() {
        EmployeeAdd employeeAdd = new EmployeeAdd(this);
        employeeAdd.publishAfterCommit();
    }

    public static EmployeeRepository repository() {
        EmployeeRepository employeeRepository = BasicApplication.applicationContext.getBean(
            EmployeeRepository.class
        );
        return employeeRepository;
    }
}
