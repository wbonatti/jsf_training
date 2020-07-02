package br.com.wellington.cartaocredito.view;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;

import br.com.wellington.cartaocredito.model.entidade.Card;
import br.com.wellington.cartaocredito.model.entidade.Client;
import br.com.wellington.cartaocredito.model.util.PersistenceDao;
import br.com.wellington.cartaocredito.model.util.impl.DefaultPersistanceDao;

@RequestScoped
@Named
public class ClientBean {

	private Client client;

	private List<Client> clients;

	private PersistenceDao<Client> persistanceService;

	private PersistenceDao<Card> persistanceCardService;

	public ClientBean() {
		client = new Client();
		persistanceService = new DefaultPersistanceDao<Client>();
		persistanceCardService = new DefaultPersistanceDao<Card>();
		list();
	}

	public void save() {
		try {
			getPersistanceService().save(this.client);
			client = new Client();
			list();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário cadastrado!", null));
		} catch (final Exception error) {
			if (error instanceof RollbackException || error instanceof PersistenceException) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Erro!!! Já existe um usuário com o CPF: " + this.client.getDocumentNumber(), null));
			}
		}
	}

	public void list() {
		clients = getPersistanceService().findAll(Client.class);
	}

	public void delete(final Integer id) {

		final List<Card> cards = getPersistanceCardService().findByColumn(id, Card.class, "client");
		if (cards != null && !cards.isEmpty()) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Erro!!! Não pode remover cliente que possui cartão", null));
		} else {
			getPersistanceService().delete(id, Client.class);
			list();
		}

	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public PersistenceDao<Client> getPersistanceService() {
		return persistanceService;
	}

	public void setPersistanceService(PersistenceDao<Client> persistanceService) {
		this.persistanceService = persistanceService;
	}

	public List<Client> getClients() {
		return clients;
	}

	public void setClients(List<Client> clients) {
		this.clients = clients;
	}

	public PersistenceDao<Card> getPersistanceCardService() {
		return persistanceCardService;
	}

	public void setPersistanceCardService(PersistenceDao<Card> persistanceCardService) {
		this.persistanceCardService = persistanceCardService;
	}

}
