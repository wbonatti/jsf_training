package br.com.wellington.cartaocredito.view;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.com.wellington.cartaocredito.model.entidade.Card;
import br.com.wellington.cartaocredito.model.entidade.Client;
import br.com.wellington.cartaocredito.model.util.PersistenceDao;
import br.com.wellington.cartaocredito.model.util.impl.DefaultPersistanceDao;

@ViewScoped
@Named
public class CardBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -543479982706880935L;

	private Card card;

	private List<SelectItem> customers;

	private PersistenceDao<Card> persistanceCardDao;

	private PersistenceDao<Client> persistanceClientDao;

	private Integer client;

	private List<Card> cards;

	public CardBean() {
		card = new Card();
		persistanceCardDao = new DefaultPersistanceDao<Card>();
		persistanceClientDao = new DefaultPersistanceDao<Client>();
		customers = createCustomerList();
	}

	private List<SelectItem> createCustomerList() {

		return getPersistanceClientDao().findAll(Client.class).stream()
				.map(it -> new SelectItem(it.getId(), it.getName())).collect(Collectors.toList());
	}

	public void list() {
		cards = getPersistanceCardDao().findByExternalTable(client, Card.class, "client");
	}

	public void save() {
		card.setCreated(LocalDateTime.now());
		card.setCvv(generateCvv());
		card.setNumber(generateNumber());
		card.setValidDate(LocalDate.now().plusYears(5));
		getPersistanceCardDao().save(card);
		card = new Card();
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Cartão Salvo!", null));
	}

	private String generateNumber() {
		return String.join("-", String.valueOf(random(10000)), String.valueOf(random(10000)),
				String.valueOf(random(10000)), String.valueOf(random(10000)));
	}

	private Integer generateCvv() {
		return random(1000);
	}

	private Integer random(final int number) {
		final Random rand = new Random();
		Integer value = rand.nextInt(number);

		return formatNumber(value, number - 1);
	}

	private Integer formatNumber(final Integer value, final int number) {

		final String valueString = String.valueOf(value);
		final String paramNumber = String.valueOf(number);
		Integer result = value;

		if (valueString.length() < paramNumber.length()) {
			result = formatNumber(Integer.valueOf(valueString + "0"), number);
		}

		return result;
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public List<SelectItem> getCustomers() {
		return customers;
	}

	public void setCustomers(List<SelectItem> customers) {
		this.customers = customers;
	}

	public PersistenceDao<Card> getPersistanceCardDao() {
		return persistanceCardDao;
	}

	public void setPersistanceCardDao(PersistenceDao<Card> persistanceCardDao) {
		this.persistanceCardDao = persistanceCardDao;
	}

	public PersistenceDao<Client> getPersistanceClientDao() {
		return persistanceClientDao;
	}

	public void setPersistanceClientDao(PersistenceDao<Client> persistanceClientDao) {
		this.persistanceClientDao = persistanceClientDao;
	}

	public Integer getClient() {
		return client;
	}

	public void setClient(Integer client) {
		this.client = client;
	}

	public List<Card> getCards() {
		return cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}

}
