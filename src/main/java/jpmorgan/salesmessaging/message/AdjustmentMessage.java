package jpmorgan.salesmessaging.message;

import java.math.BigDecimal;

import jpmorgan.salesmessaging.domain.Operation;

/**
 * @author ignacio paz
 * This class represent the message of sales that may have been designed by a third party
 * or external company as a kind of DTO.
 * It may be error prone and needs to be validated and processed by the receptor of the message.
 */
public class AdjustmentMessage {

	private String product;
	private BigDecimal price;
	private Operation operation;
	public AdjustmentMessage() {
		
	}
	public AdjustmentMessage(String product, BigDecimal price, Operation operation) {
		super();
		if (product == null || product.isEmpty()) throw new IllegalArgumentException("product cannot be null or empty");
		if (price == null || price.signum() < 0) throw new IllegalArgumentException("price cannot be null or negative");
		if (operation == null ) throw new IllegalArgumentException("operation cannot be null");
		this.product = product;
		this.price = price;
		this.operation = operation;
	}

	public String getProduct() {
		return product;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public Operation getOperation() {
		return operation;
	}
}
