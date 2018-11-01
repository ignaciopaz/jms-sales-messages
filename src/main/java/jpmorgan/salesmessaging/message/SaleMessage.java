package jpmorgan.salesmessaging.message;

import java.math.BigDecimal;

/**
 * @author ignacio paz
 * This class represent the message of sales that may have been designed by a third party
 * or external company as a kind of DTO.
 * It may be error prone and needs to be validated and processed by the receptor of the message.
 * There are some static factory methods to facilitate creating the instances to a customer
 * and reducing errors although a factory can be added too.
 */
public class SaleMessage {

	private String product;
	private BigDecimal price;
	private Integer quantity;

	public SaleMessage() {
		
	}
	
	public SaleMessage(String product, BigDecimal price) {
		this(product, price, 1);
	}
	
	public SaleMessage(String product, BigDecimal price, Integer quantity) {
		super();
		if (product == null || product.isEmpty()) throw new IllegalArgumentException("product cannot be null or empty");
		if (price == null || price.signum() < 0) throw new IllegalArgumentException("price cannot be null or negative");
		if (quantity == null || quantity < 1) throw new IllegalArgumentException("quantity cannot be null or negative");
		
		this.product = product;
		this.price = price;
		this.quantity = quantity;
	}

	public String getProduct() {
		return product;
	}


	public BigDecimal getPrice() {
		return price;
	}

	public Integer getQuantity() {
		return quantity;
	}

}
