package model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "country")
@Getter
@Setter
public class Country {

    @Id
    private String code;

    private String name;

    private String continent;

    private String surfaceArea;

    private String population;

}
