package model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "city")
@Getter
@Setter
public class City {

    @Id
    private Long id;

    private String name;

    private String district;

    private String population;

    private String countryCode;

}
