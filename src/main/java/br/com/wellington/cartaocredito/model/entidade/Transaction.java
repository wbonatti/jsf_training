package br.com.wellington.cartaocredito.model.entidade;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Transaction extends LocalEntity {

	@NotNull
	@ManyToOne
	@JoinColumn(name = "card_id")
	private Card card;

	@NotNull
	private Integer cvv;

	@NotNull
	private String transactionType;

	private BigDecimal amount;

	@NotNull
	@Size(min = 10)
	private String store;

	private LocalDateTime date;

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public Integer getCvv() {
		return cvv;
	}

	public void setCvv(Integer cvv) {
		this.cvv = cvv;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Transaction [id=" + getId() + ", card=" + card + ", cvv=" + cvv + ", transactionType=" + transactionType
				+ ", amount=" + amount + ", store=" + store + ", date=" + date + "]";
	}

}
