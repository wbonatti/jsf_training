package br.com.wellington.cartaocredito.model.util;

import java.util.List;

public interface PersistenceDao<T> {

	boolean save(final T object);

	boolean update(final T object);

	boolean delete(final Integer id, final Class<T> clazz);

	List<T> findAll(final Class<T> clazz);

	T findById(final Integer id, final Class<T> clazz);

	List<T> findByExternalTable(final Object id, final Class<T> clazz, final String column);

	List<T> findByColumn(final Object id, final Class<T> clazz, final String column);

	List<T> findByReport(final Class<T> clazz, final Integer id, final String join, final String field,
			final String number, final String join2, final String field2);

}
