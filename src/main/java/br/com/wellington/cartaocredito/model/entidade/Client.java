package br.com.wellington.cartaocredito.model.entidade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

@Entity
public class Client extends LocalEntity {

	@NotNull
	@Size(min = 10)
	private String name;

	@NotNull
	@Past
	private LocalDate birthDate;

	@NotNull
	@Email
	private String email;

	@NotNull
	@Size(min = 11)
	@Column(unique = true)
	private String documentNumber;

	private String phone;

	private BigDecimal monthlyIncome;

	@OneToMany(mappedBy = "client")
	private List<Card> cards;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public BigDecimal getMonthlyIncome() {
		return monthlyIncome;
	}

	public void setMonthlyIncome(BigDecimal monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
	}

	public List<Card> getCards() {
		return cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}

	@Override
	public String toString() {
		return "Client [id=" + getId() + ", name=" + name + ", birthDate=" + birthDate + ", email=" + email
				+ ", documentNumber=" + documentNumber + ", phone=" + phone + ", monthlyIncome=" + monthlyIncome + "]";
	}

}
