package br.com.wellington.cartaocredito.view;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import br.com.wellington.cartaocredito.model.entidade.Client;
import br.com.wellington.cartaocredito.model.entidade.Transaction;
import br.com.wellington.cartaocredito.model.util.PersistenceDao;
import br.com.wellington.cartaocredito.model.util.impl.DefaultPersistanceDao;

@ViewScoped
@Named
public class ReportBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -543479982706880935L;

	@NotNull
	private LocalDate startDate;

	@NotNull
	private LocalDate endDate;

	private List<SelectItem> customers;

	private PersistenceDao<Client> persistanceClientDao;

	private PersistenceDao<Transaction> persistanceTransactionDao;

	private Integer client;

	private String card;

	private BarChartModel dateModel;

	public ReportBean() {
		persistanceTransactionDao = new DefaultPersistanceDao<Transaction>();
		persistanceClientDao = new DefaultPersistanceDao<Client>();
		customers = createCustomerList();
		dateModel = new BarChartModel();
	}

	private List<SelectItem> createCustomerList() {

		return getPersistanceClientDao().findAll(Client.class).stream()
				.map(it -> new SelectItem(it.getId(), it.getName())).collect(Collectors.toList());
	}

	public void clean() {
		startDate = null;
		endDate = null;
		client = null;
		card = null;
	}

	public void save() {

		final boolean selectByVendor = this.card == null && this.client == null;

		if (endDate.isBefore(startDate)) {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Datas inválidas!", null));
		}

		final List<Transaction> transactions;

		if (selectByVendor) {
			transactions = getPersistanceTransactionDao().findAll(Transaction.class);
		} else {

			transactions = getPersistanceTransactionDao().findByReport(Transaction.class, client, "client", "id", card,
					"card", "number");
		}

		if (selectByVendor) {
			createDateModelByVendor(transactions);
		} else {
			createDateModel(transactions);
		}

	}

	private void createDateModelByVendor(List<Transaction> transactions) {
		BarChartModel model = new BarChartModel();

		ChartSeries vendorGra = new ChartSeries();
		vendorGra.setLabel("Vendedores");

		List<String> vendors = transactions.stream().map(Transaction::getStore).distinct().collect(Collectors.toList());

		for (String vendor : vendors) {
			vendorGra.set(vendor, transactions.stream().filter(it -> vendor.equals(it.getStore())).count());
		}

		model.addSeries(vendorGra);

		createModel(model, "Vendedores");
	}

	private void createDateModel(final List<Transaction> transactions) {
		BarChartModel model = new BarChartModel();

		ChartSeries vendorGra = new ChartSeries();
		vendorGra.setLabel("datas");

		LocalDate local = this.startDate;

		while (local.isBefore(endDate) || local.equals(endDate)) {
			final LocalDate date = local;

			vendorGra.set(local.format(DateTimeFormatter.ofPattern("dd/MM")),
					transactions.stream().filter(it -> date.equals(it.getDate().toLocalDate())).count());

			local = local.plusDays(1);
		}

		model.addSeries(vendorGra);

		createModel(model, "Datas");
	}

	private void createModel(final BarChartModel model, final String lineName) {

		dateModel = model;
		dateModel.setTitle("Bar Chart");
		dateModel.setLegendPosition("ne");

		Axis xAxis = dateModel.getAxis(AxisType.X);
		xAxis.setLabel(lineName);

		Axis yAxis = dateModel.getAxis(AxisType.Y);
		yAxis.setLabel("Transações");
		yAxis.setMin(0);
		yAxis.setMax(50);
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public List<SelectItem> getCustomers() {
		return customers;
	}

	public void setCustomers(List<SelectItem> customers) {
		this.customers = customers;
	}

	public PersistenceDao<Client> getPersistanceClientDao() {
		return persistanceClientDao;
	}

	public void setPersistanceClientDao(PersistenceDao<Client> persistanceClientDao) {
		this.persistanceClientDao = persistanceClientDao;
	}

	public PersistenceDao<Transaction> getPersistanceTransactionDao() {
		return persistanceTransactionDao;
	}

	public void setPersistanceTransactionDao(PersistenceDao<Transaction> persistanceTransactionDao) {
		this.persistanceTransactionDao = persistanceTransactionDao;
	}

	public Integer getClient() {
		return client;
	}

	public void setClient(Integer client) {
		this.client = client;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public BarChartModel getDateModel() {
		return dateModel;
	}

	public void setDateModel(BarChartModel dateModel) {
		this.dateModel = dateModel;
	}

}
