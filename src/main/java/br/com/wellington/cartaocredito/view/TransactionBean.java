package br.com.wellington.cartaocredito.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import br.com.wellington.cartaocredito.model.entidade.Card;
import br.com.wellington.cartaocredito.model.entidade.Transaction;
import br.com.wellington.cartaocredito.model.util.PersistenceDao;
import br.com.wellington.cartaocredito.model.util.impl.DefaultPersistanceDao;

@ViewScoped
@Named
public class TransactionBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4826839629548282948L;
	private static final String DEBIT = "Débito";

	private Transaction transaction;

	@NotNull
	private String cardNumber;

	private List<Transaction> transactionList;

	private PersistenceDao<Transaction> persistenceTransactionDao;

	private PersistenceDao<Card> persistenceCardDao;

	public TransactionBean() {
		transaction = new Transaction();
		persistenceCardDao = new DefaultPersistanceDao<Card>();
		persistenceTransactionDao = new DefaultPersistanceDao<Transaction>();
	}

	public void save() {

		final List<Card> cards = getPersistenceCardDao().findByColumn(String.valueOf(cardNumber), Card.class, "number");
		
		if(cards == null || cards.isEmpty()) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!!! Cartão inválido", null));
		}else {
			
			final Card card = cards.get(0);

			if (!card.getCvv().equals(this.transaction.getCvv())) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!!! Número do cartão ou CVV inválido", null));
			} else {

				final BigDecimal finalValue;
				if (DEBIT.equals(this.transaction.getTransactionType())) {
					finalValue = card.getMoneyLimit().subtract(this.transaction.getAmount());
				} else {
					finalValue = card.getMoneyLimit().add(this.transaction.getAmount());
				}

				if (finalValue.compareTo(BigDecimal.ZERO) < 0) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_WARN, "Cartão sem limite", null));
				} else {

					this.transaction.setCard(card);
					this.transaction.setDate(LocalDateTime.now());
					getPersistenceTransactionDao().save(transaction);
					card.setMoneyLimit(finalValue);
					getPersistenceCardDao().update(card);

					transaction = new Transaction();
					this.cardNumber = null;
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "Transação Gerada!, saldo restante R$ " + finalValue, null));
				}
			}
			
		}
		

	}

	public void list() {

		List<Card> cards = getPersistenceCardDao().findByColumn(String.valueOf(cardNumber), Card.class, "number");
		if (cards != null && !cards.isEmpty()) {

			transactionList = getPersistenceTransactionDao().findByExternalTable(String.valueOf(cards.get(0).getId()),
					Transaction.class, "card");
		}
		setCardNumber(null);
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public PersistenceDao<Transaction> getPersistenceTransactionDao() {
		return persistenceTransactionDao;
	}

	public void setPersistenceTransactionDao(PersistenceDao<Transaction> persistenceTransactionDao) {
		this.persistenceTransactionDao = persistenceTransactionDao;
	}

	public PersistenceDao<Card> getPersistenceCardDao() {
		return persistenceCardDao;
	}

	public void setPersistenceCardDao(PersistenceDao<Card> persistenceCardDao) {
		this.persistenceCardDao = persistenceCardDao;
	}

	public List<Transaction> getTransactionList() {
		return transactionList;
	}

	public void setTransactionList(List<Transaction> transactionList) {
		this.transactionList = transactionList;
	}

}
