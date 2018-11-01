package jpmorgan.salesmessaging.repository;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import jpmorgan.common.repository.MapRepository;
import jpmorgan.salesmessaging.domain.Product;

public class ProductRepositoryMemImpl extends MapRepository<String, Product> implements ProductRepository {
	private Comparator<Product> bySalesComparator = (Product p1, Product p2) -> {
		return p2.getQuantity().compareTo(p1.getQuantity());
	};
	
	public ProductRepositoryMemImpl() {
		super(new LinkedHashMap<String, Product>());
	}

	@Override
	public Collection<Product> findAllSortedBySales() {
		List<Product> list = new LinkedList<Product>(findAll());
		Collections.sort(list, bySalesComparator);
		return list;
	}

}
