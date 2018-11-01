package jpmorgan.salesmessaging.processor;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import jpmorgan.salesmessaging.domain.Operation;
import jpmorgan.salesmessaging.domain.Product;
import jpmorgan.salesmessaging.message.AdjustmentMessage;
import jpmorgan.salesmessaging.message.SaleMessage;
import jpmorgan.salesmessaging.repository.ProductRepository;
import jpmorgan.salesmessaging.repository.ProductRepositoryMemImpl;

public class SalesMessagesProcessorTest {
	private SalesMessagesProcessor processor;
	private ProductRepository productRepository;
	private String p1Id, p2Id;
	
	@Before public void before() {
		productRepository = new ProductRepositoryMemImpl();
		processor = new SalesMessagesProcessor(productRepository);
		p1Id = "tape";
		p2Id = "vinyl";
	}
	
	@Test(expected=IllegalArgumentException.class) public void testEmptyAdjustmentMessage() {
		AdjustmentMessage message = new AdjustmentMessage(); 
		processor.process(message);
	}
	@Test(expected=IllegalArgumentException.class) public void testWrongAdjustmentMessage() {
		AdjustmentMessage message = new AdjustmentMessage(null, null, null); //should test more combinations
		processor.process(message);
	}
	@Test(expected=IllegalArgumentException.class) public void testWrongMultiSaleMessage() {
		SaleMessage message = new SaleMessage(null, BigDecimal.ONE, 2);
		processor.process(message);
	}
	@Test(expected=IllegalArgumentException.class) public void testWrongSaleMessage() {
		SaleMessage message = new SaleMessage(null, BigDecimal.ONE);
		processor.process(message);
	}
	@Test(expected=IllegalArgumentException.class) public void testEmptySaleMessage() {
		SaleMessage message = new SaleMessage();
		processor.process(message);
	}

	@Test public void test3ProductsWithSalesAndAdjustments() {
		addSale(p1Id, 10);
		addSale(p2Id, 10, 2);
		addSale(p2Id, 2.5,3);
		
		adjustAdd(p1Id, 1);
		adjustSubstract(p1Id, 0.5);
		adjustSubstract(p2Id, 0.1);
		adjustMultiply(p2Id, 2);
		adjustSubstract(p2Id, 0.2);
		addSale(p2Id,1,2);
		adjustAdd(p2Id, 1);
		
		Product p1 = productRepository.getOne(p1Id);
		Product p2 = productRepository.getOne(p2Id);
		assertEquals(Integer.valueOf(1), p1.getQuantity());
		assertEquals(BigDecimal.valueOf(10+1-0.5), p1.getTotal());
		assertEquals(1, p1.getSales().size());
		assertEquals(2, p1.getAdjustments().size());
		
		assertEquals(Integer.valueOf(7), p2.getQuantity());
		assertEquals(BigDecimal.valueOf(62.0), p2.getTotal());
		assertEquals(3, p2.getSales().size());
		assertEquals(4, p2.getAdjustments().size());
	}

	private void adjustAdd(String pId, double amount) {
		processor.process(new AdjustmentMessage(pId, BigDecimal.valueOf(amount), Operation.ADD));
	}
	private void adjustSubstract(String pId, double amount) {
		processor.process(new AdjustmentMessage(pId, BigDecimal.valueOf(amount), Operation.SUBSTRACT));
	}
	private void adjustMultiply(String pId, int amount) {
		processor.process(new AdjustmentMessage(pId, BigDecimal.valueOf(amount), Operation.MULTIPLY));
	}

	private void addSale(String pId, double price, int quantity) {
		processor.process(new SaleMessage(pId, BigDecimal.valueOf(price), quantity));	
	}
	
	private void addSale(String pId, double price) {
		processor.process(new SaleMessage(pId, BigDecimal.valueOf(price)));
	}
}
