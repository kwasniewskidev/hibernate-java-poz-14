package model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "humanEmployee")
@Setter
public class HumanEmployee {

    @Id
    @GeneratedValue
    private Long id;

    private String firstname;

    @OneToOne
    @JoinColumn(name = "employee_details_id")
    private HumanEmployeeDetails details;

    private String lastname;
}
