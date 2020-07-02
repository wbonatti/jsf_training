package br.com.wellington.cartaocredito.model.entidade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
public class Card extends LocalEntity {

	private String number;

	private Integer cvv;

	private LocalDateTime created;

	private LocalDate validDate;

	@NotNull
	private BigDecimal moneyLimit;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "client_id")
	private Client client;

	@OneToMany(mappedBy = "card")
	private List<Transaction> transactions;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Integer getCvv() {
		return cvv;
	}

	public void setCvv(Integer cvv) {
		this.cvv = cvv;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDate getValidDate() {
		return validDate;
	}

	public void setValidDate(LocalDate validDate) {
		this.validDate = validDate;
	}

	public BigDecimal getMoneyLimit() {
		return moneyLimit;
	}

	public void setMoneyLimit(BigDecimal moneyLimit) {
		this.moneyLimit = moneyLimit;
	}

	public Client getClient() {
		if (client == null) {
			client = new Client();
		}
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	@Override
	public String toString() {
		return "Card [id=" + getId() + ", number=" + number + ", cvv=" + cvv + ", created=" + created + ", validDate="
				+ validDate + ", moneyLimit=" + moneyLimit + ", client=" + client + "]";
	}

}
