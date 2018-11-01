package jpmorgan.salesmessaging.domain;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedList;

import jpmorgan.common.domain.Entity;

public class Product implements Entity<String> {
	private String name;
	private Collection<Sale> sales;
	private Collection<Adjustment> adjustments;
	private Integer quantity;
	private BigDecimal total;

	public Product(String name) {
		if (name == null || name.isEmpty()) throw new IllegalArgumentException("name cannot be null or empty");
		this.name = name;
		this.sales = new LinkedList<Sale>();
		this.adjustments = new LinkedList<Adjustment>();
		this.quantity = 0;
		this.total = BigDecimal.ZERO;
	}

	public String getName() {
		return name;
	}

	public void addSale(Sale sale) {
		quantity += sale.getNumberOfSales();
		total = total.add(sale.getTotal());
		sales.add(sale);		
	}
	
	public void addAdjustment(Adjustment adjustment) {
		//The total sale amount for a product can be calculated applying the adjustment to
		//the total accumulated for the product.
		//There is no need to iterate all the previous sales and update them.
		//If we would need to save adjusted sales, we can call: adjustment.process(this);
		adjustments.add(adjustment);
		total = adjustment.getTotal();	
	}
	
	public Integer getQuantity() {
		return quantity;
	}
	
	public BigDecimal getTotal() {
		return total;
	}

	public Collection<Sale> getSales() {
		return sales;
	}

	public String getId() {
		return getName();
	}

	public Collection<Adjustment> getAdjustments() {
		return adjustments;		
	}

	public Product getProduct() {
		return this;
	}

}
