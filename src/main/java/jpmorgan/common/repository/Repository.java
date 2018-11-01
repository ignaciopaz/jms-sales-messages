package jpmorgan.common.repository;

import java.util.Collection;

public interface Repository<ID, T> {
	public T getOne(ID id);
	public void save(T t);
	public Collection<T> findAll();
}
