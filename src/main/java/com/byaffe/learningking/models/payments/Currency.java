/**
 *
 */
package com.byaffe.learningking.models.payments;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.byaffe.learningking.shared.models.BaseEntity;
import com.byaffe.learningking.shared.models.Country;


/**
 * Represents a currency in the system
 *
 * @author Mzee Sr.
 *
 */
@Entity
@Table(name = "currencies")
public class Currency extends BaseEntity {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String name;
    private String symbol;
    private Country country;
    private String pgwCode;

    /**
     * @return the name
     */
    @Column(name = "name")
    public String getName() {
        return name;
    }

    /**
     * @return the symbol
     */
    @Column(name = "symbol")
    public String getSymbol() {
        return symbol;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param symbol the symbol to set
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @OneToOne
    @JoinColumn(name = "country")
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Column(name = "pgw_code")
    public String getPgwCode() {
        return pgwCode;
    }

    public void setPgwCode(String pgwCode) {
        this.pgwCode = pgwCode;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", this.name, this.symbol);
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Currency && (super.getId() != null) ? super.getId().equals(((Currency) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }

   

}
