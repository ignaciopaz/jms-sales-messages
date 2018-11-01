package jpmorgan.salesmessaging.domain;

import java.math.BigDecimal;

public class Sale {
	private Product product;
	private BigDecimal price;
	private Integer numberOfSales;
	
	public Sale(Product product, BigDecimal price, Integer numberOfSales) {
		if (product == null) throw new IllegalArgumentException("product must be not null");
		if (price == null) throw new IllegalArgumentException("price must be not null");
		if (price.signum() < 0) throw new IllegalArgumentException("price cannot be less than 0");
		if (numberOfSales <= 0) throw new IllegalArgumentException("numberOfSales must be positive");
		this.product = product;
		this.price = price;
		this.numberOfSales = numberOfSales;
	}
	
	public Sale(Product product, BigDecimal price) {
		this(product, price, 1);
	}
	public BigDecimal getPrice() {
		return price;
	}
	
	public Product getProduct() {
		return product;		
	}
	public Integer getNumberOfSales() {
		return numberOfSales;
	}

	public BigDecimal getTotal() {
		return getPrice().multiply(BigDecimal.valueOf(numberOfSales));
	}
	
	//this is in case we need to update all the sales and save with adjustments.
	//But we do not need to calculate the total for the product
	/*@Deprecated public void adjustPrice(BigDecimal amount, Operation operation) {
		if (operation == Operation.ADD) {
			price = price.add(amount);
		} else if (operation == Operation.SUBSTRACT) {
			price = price.subtract(amount);
		} else if (operation == Operation.MULTIPLY) {
			price = price.multiply(amount);
		}	
	}*/

}
