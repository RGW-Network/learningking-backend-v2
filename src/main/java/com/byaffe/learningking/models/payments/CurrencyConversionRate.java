/**
 * 
 */
package com.byaffe.learningking.models.payments;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.byaffe.learningking.shared.models.BaseEntity;

/**
 * {@link CurrencyConversionRate} are not editable once activated. The only
 * allowable acton is deactivation. Once deactivated, the instance isnt editable
 * at all.
 * 
 * @author Mzee Sr.
 *
 */
@Entity
@Table(name = "currency_conversion_rates")
public class CurrencyConversionRate extends BaseEntity {

	
	private static final long serialVersionUID = -3922131731859625812L;
	private Currency fromCurency;
	private float toBaseCurrency;
	private Date dateActivated;
	private Date dateDeactivated;
	private CurrencyConversionRateStatus status = CurrencyConversionRateStatus.New;

        
	/**
	 * @return the fromCurency
	 */
	@ManyToOne
	@JoinColumn(name = "from_currency")
	public Currency getFromCurency() {
		return fromCurency;
	}

	/**
	 * @return the toBaseCurrency
	 */
	@Column(name = "to_base_currency")
	public float getToBaseCurrency() {
		return toBaseCurrency;
	}

	/**
	 * @return the status
	 */
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "status")
	public CurrencyConversionRateStatus getStatus() {
		return status;
	}

	/**
	 * @return the dateActivated
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_activated")
	public Date getDateActivated() {
		return dateActivated;
	}

	/**
	 * @param dateActivated the dateActivated to set
	 */
	public void setDateActivated(Date dateActivated) {
		this.dateActivated = dateActivated;
	}

	/**
	 * @return the dateDeactivated
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_deactivated")
	public Date getDateDeactivated() {
		return dateDeactivated;
	}

	/**
	 * @param dateDeactivated the dateDeactivated to set
	 */
	public void setDateDeactivated(Date dateDeactivated) {
		this.dateDeactivated = dateDeactivated;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(CurrencyConversionRateStatus status) {
		this.status = status;
	}

	/**
	 * @param fromCurency the fromCurency to set
	 */
	public void setFromCurency(Currency fromCurency) {
		this.fromCurency = fromCurency;
	}

	/**
	 * @param toBaseCurrency the toBaseCurrency to set
	 */
	public void setToBaseCurrency(float toBaseCurrency) {
		this.toBaseCurrency = toBaseCurrency;
	}

	@Override
	public String toString() {
		return String.format("% to Base = %s", this.fromCurency.toString(), String.valueOf(this.toBaseCurrency));
	}

	@Override
	public boolean equals(Object object) {
		return object instanceof CurrencyConversionRate && (super.getId() != null)
				? super.getId().equals(((CurrencyConversionRate) object).getId())
				: (object == this);
	}

	@Override
	public int hashCode() {
		return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
	}
}