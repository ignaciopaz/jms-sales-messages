package jpmorgan.salesmessaging.repository;

import java.util.Collection;

import jpmorgan.common.repository.Repository;
import jpmorgan.salesmessaging.domain.Product;

public interface ProductRepository extends Repository<String, Product> {
	public Collection<Product> findAllSortedBySales();
}
