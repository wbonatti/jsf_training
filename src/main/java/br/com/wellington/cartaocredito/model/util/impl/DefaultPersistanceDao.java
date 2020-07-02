package br.com.wellington.cartaocredito.model.util.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.wellington.cartaocredito.model.util.ConnectionFactory;
import br.com.wellington.cartaocredito.model.util.PersistenceDao;

public class DefaultPersistanceDao<T> implements PersistenceDao<T> {

	public boolean save(final T object) {

		final EntityManager em = ConnectionFactory.getInstance();
		try {
			em.getTransaction().begin();
			em.persist(object);
			em.getTransaction().commit();
		} catch (Exception error) {
			em.getTransaction().rollback();

			throw error;
		} finally {
			em.close();
		}

		return true;
	}

	public boolean update(final T object) {

		final EntityManager em = ConnectionFactory.getInstance();
		try {
			em.getTransaction().begin();
			em.merge(object);
			em.getTransaction().commit();
		} catch (Exception error) {
			em.getTransaction().rollback();

			throw error;
		} finally {
			em.close();
		}

		return true;
	}

	public boolean delete(final Integer id, final Class<T> clazz) {

		final EntityManager em = ConnectionFactory.getInstance();
		try {
			em.getTransaction().begin();
			em.remove(em.find(clazz, id));
			em.getTransaction().commit();
		} catch (Exception error) {

			em.getTransaction().rollback();
			throw error;
		} finally {
			em.close();
		}

		return true;
	}

	public List<T> findAll(final Class<T> clazz) {

		final EntityManager em = ConnectionFactory.getInstance();
		final List<T> result;

		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<T> cq = cb.createQuery(clazz);
			Root<T> rootEntry = cq.from(clazz);
			CriteriaQuery<T> all = cq.select(rootEntry);
			TypedQuery<T> allQuery = em.createQuery(all);

			result = allQuery.getResultList();

		} finally {
			em.close();
		}

		return result;
	}

	public T findById(final Integer id, final Class<T> clazz) {

		final T result;
		final EntityManager em = ConnectionFactory.getInstance();

		try {
			result = (T) em.find(clazz, id);
		} catch (Exception error) {
			throw error;
		} finally {
			em.close();
		}
		return result;
	}

	@Override
	public List<T> findByExternalTable(Object id, Class<T> clazz, String column) {

		final EntityManager em = ConnectionFactory.getInstance();
		final List<T> result;

		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<T> cq = cb.createQuery(clazz);
			Root<T> rootEntry = cq.from(clazz);
			CriteriaQuery<T> all = cq.select(rootEntry);
			all.where(cb.equal(rootEntry.join(column).get("id"), id));
			TypedQuery<T> allQuery = em.createQuery(all);

			result = allQuery.getResultList();

		} finally {
			em.close();
		}

		return result;
	}

	@Override
	public List<T> findByColumn(Object id, Class<T> clazz, String column) {
		final EntityManager em = ConnectionFactory.getInstance();
		final List<T> result;

		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<T> cq = cb.createQuery(clazz);
			Root<T> rootEntry = cq.from(clazz);
			CriteriaQuery<T> all = cq.select(rootEntry);
			all.where(cb.equal(rootEntry.get(column), id));
			TypedQuery<T> allQuery = em.createQuery(all);

			result = allQuery.getResultList();

		} finally {
			em.close();
		}

		return result;
	}

	@Override
	public List<T> findByReport(final Class<T> clazz, final Integer id, final String join, final String field,
			final String number, final String join2, final String field2) {

		final EntityManager em = ConnectionFactory.getInstance();
		final List<T> result;

		try {

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<T> cq = cb.createQuery(clazz);
			Root<T> rootEntry = cq.from(clazz);
			CriteriaQuery<T> all = cq.select(rootEntry);
			Predicate client = cb.conjunction();
			Predicate card = cb.conjunction();

			if (id != null) {
				client = cb.equal(rootEntry.join(join2).join(join).get(field), id);
			}

			if (number != null) {
				card = cb.equal(rootEntry.join(join2).get(field2), number);
			}

			all.where(client, card);
			TypedQuery<T> allQuery = em.createQuery(all);

			result = allQuery.getResultList();

		} finally {
			em.close();
		}

		return result;
	}

}
