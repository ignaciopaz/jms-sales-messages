package jpmorgan.salesmessaging.processor;

import jpmorgan.salesmessaging.domain.Adjustment;
import jpmorgan.salesmessaging.domain.Product;
import jpmorgan.salesmessaging.domain.Sale;
import jpmorgan.salesmessaging.message.AdjustmentMessage;
import jpmorgan.salesmessaging.message.SaleMessage;
import jpmorgan.salesmessaging.repository.ProductRepository;

public class SalesMessagesProcessor {

	private ProductRepository productRepository;
	
	public SalesMessagesProcessor(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public void process(SaleMessage message) {
		Product product = getOrCreate(message.getProduct());
		product.addSale(new Sale(product, message.getPrice(), message.getQuantity()));
	}
	
	public void process(AdjustmentMessage message) {
		Product product = getOrCreate(message.getProduct());
		product.addAdjustment(new Adjustment(product, message.getPrice(), message.getOperation()));
	}

	private Product getOrCreate(String productName) {
		Product product = productRepository.getOne(productName);
		if (product == null) {
			product = new Product(productName);
			productRepository.save(product);
		}
		return product;
	}	
	
}
