package jpmorgan.salesmessaging.domain;

import java.math.BigDecimal;

public class Adjustment {
	private Product product;
	private BigDecimal amount;
	private Integer processedSales;
	private Operation operation;
	private BigDecimal totalBefore;
	private BigDecimal total;
	//We potentially could save a copy of the sales processed by this adjustment but it is not necessary
	//private Collection<Sale> processedSales;
	
	public Adjustment(Product product, BigDecimal amount, Operation operation) {
		if (product == null) throw new IllegalArgumentException("product must be not null");
		if (amount == null) throw new IllegalArgumentException("amount must be not null");
		if (operation == null) throw new IllegalArgumentException("operation must be not null");
		if (amount.signum() < 1) throw new IllegalArgumentException("amount must be positive");
		this.product = product;
		this.amount = amount;
		this.operation = operation;
		this.totalBefore = product.getTotal();
		//We potentially could save a copy of the sales processed by this adjustment but it is not necessary
		//this.processedSales = new ArrayList<Sale>(product.getSales());
		process();
	}

	public Operation getOperation() {
		return operation;
	}
	
	//this is in case we need to update all the sales and save with adjustments.
	//But we do not need to calculate the total for the product
	/*@Deprecated public void process() {
		Product product = product.getProduct();
		BigDecimal totalBefore = product.getTotal();
		
		setTotalBefore(totalBefore);
		BigDecimal total = BigDecimal.ZERO;
		int count = 0;
		for (Sale sale : product.getSales()) {			
			sale.adjustPrice(getPrice(), operation);
			total = total.add(sale.getTotal());
			count += sale.getQuantity();
		}
		setTotalAfter(total);
		this.processedSales = count;
	}*/

	public BigDecimal getTotalBefore() {
		return totalBefore;
	}

	private void process() {
		BigDecimal totalAmount = totalBefore;
		processedSales = product.getQuantity();
		//totalAmount = operation.apply(totalAmount, amount, processedSales);
		if (operation == Operation.ADD) {
			totalAmount = totalAmount.add(amount.multiply(BigDecimal.valueOf(processedSales)));
		} else if (operation == Operation.SUBSTRACT) {
			totalAmount = totalAmount.subtract(amount.multiply(BigDecimal.valueOf(processedSales)));
		} else if (operation == Operation.MULTIPLY) {
			totalAmount = totalAmount.multiply(amount);
		}
		total = totalAmount;	
	}

	public BigDecimal getTotal() {
		return total;
	}

	public Integer getProcessedSales() {
		return processedSales;
	}

	public BigDecimal getAmount() {
		return amount;
	}
}
