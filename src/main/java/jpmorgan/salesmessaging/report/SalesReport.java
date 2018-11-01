package jpmorgan.salesmessaging.report;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;

import jpmorgan.salesmessaging.domain.Adjustment;
import jpmorgan.salesmessaging.domain.Product;

public class SalesReport {
	private NumberFormat format;
	
	public SalesReport() {
		this.format = DecimalFormat.getCurrencyInstance();
	}
	public void productsReport(Collection<Product> products) {
		System.out.println("#### Product Sales Report ####");			
		for (Product product : products) {
			System.out.println("Product: " +product.getName() + " - Total: " + format.format(product.getTotal()) + " - Total sales: " + product.getQuantity());
		}
	}
	
	public void adjustmentsReport(Collection<Product> products) {
		System.out.println("#### Adjustments Report ####");
		for (Product product : products) {
			Collection<Adjustment> adjustments = product.getAdjustments();
			for (Adjustment adjustment : adjustments) {
				System.out.println("Product: " +product.getName() + " - Amount: " + format.format(adjustment.getAmount()) + " - Operation: " + adjustment.getOperation() + " - Total Before: " + format.format(adjustment.getTotalBefore()) + " - Total After: " + format.format(adjustment.getTotal()) + " - Processed Sales: " + adjustment.getProcessedSales());
			}
			
		}
	}
}
