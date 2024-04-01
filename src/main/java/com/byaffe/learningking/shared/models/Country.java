package com.byaffe.learningking.shared.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@Table(name = "countries")
@Inheritance(strategy = InheritanceType.JOINED)
public class  Country extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Column(name = "postal_code", length = 5, nullable = false)
    private String postalCode;
    @Column(name = "currency_code",length = 10,  nullable = true, unique = true)
    private String currencyCode;
    @Column(name = "currency_symbol",length = 5,  nullable = true, unique = true)
     private String currencySymbol;


    public Country(String name, String postalCode, String currencyCode, String currencySymbol) {
        this.name = name;
        this.postalCode = postalCode;
        this.currencyCode = currencyCode;
        this.currencySymbol = currencySymbol;
    }

    public Country(String name, String postalCode) {
        this.name = name;
        this.postalCode = postalCode;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Country && (super.getId() != 0) ? super.getId()==((Country) object).getId()
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != 0 ? (int) (this.getClass().hashCode() + super.getId()) : super.hashCode();
    }

}
