package jpmorgan.common.repository;

import java.util.Collection;
import java.util.Map;

import jpmorgan.common.domain.Entity;

public abstract class MapRepository<ID, T extends Entity<ID>>  implements Repository<ID, T> {
	
	private Map<ID, T> entities;

	public MapRepository(Map<ID,T> entities) {
		this.entities = entities;
	}
	
	public T getOne(String id) {
		return entities.get(id);
	}

	public void save(T t) {
		entities.put(t.getId(), t);	
	}

	public Collection<T> findAll() {
		return entities.values();
	}
}
