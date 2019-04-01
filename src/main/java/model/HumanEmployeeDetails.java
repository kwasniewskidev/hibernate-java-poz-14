package model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "humanEmployeeDetails")
@Setter
@Getter
public class HumanEmployeeDetails {

    @Id
    @GeneratedValue
    private Long id;

    private Long salary;

    @OneToOne(mappedBy = "details")

    private HumanEmployee employee;

    private Date birthday;

    private Date agreementExpiration;

    private String department;

}
