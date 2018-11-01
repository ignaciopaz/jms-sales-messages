package jpmorgan.salesmessaging.domain;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

public class ProductTest {

	private Product p;

	@Before public void before() {
		p = new Product("tape");
	}
	@Test public void oneSaleTotal() {
		p.addSale(new Sale(p, BigDecimal.TEN));
		assertEquals(BigDecimal.TEN, p.getTotal());
		assertEquals(Integer.valueOf(1), p.getQuantity());
	}
	@Test public void twoSalesTotal() {
		p.addSale(new Sale(p, BigDecimal.TEN));
		p.addSale(new Sale(p, BigDecimal.ONE));
		assertEquals(BigDecimal.valueOf(11), p.getTotal());
		assertEquals(Integer.valueOf(2), p.getQuantity());
	}
	
	@Test public void twoMultiSalesTotal() {
		p.addSale(new Sale(p, BigDecimal.TEN, 2));
		p.addSale(new Sale(p, BigDecimal.ONE, 4));
		assertEquals(BigDecimal.valueOf(24), p.getTotal());
		assertEquals(Integer.valueOf(6), p.getQuantity());
	}
	
	@Test public void twoMultiSalesAddTotal() {
		p.addSale(new Sale(p, BigDecimal.TEN, 2));
		p.addSale(new Sale(p, BigDecimal.ONE, 4)); 
		p.addAdjustment(new Adjustment(p, BigDecimal.valueOf(2), Operation.ADD));
		assertEquals(BigDecimal.valueOf(24+2*6), p.getTotal());
		assertEquals(Integer.valueOf(6), p.getQuantity());
	}
	
	@Test public void twoMultiSalesSubstractTotal() {
		p.addSale(new Sale(p, BigDecimal.TEN, 2));
		p.addSale(new Sale(p, BigDecimal.ONE, 4)); 
		p.addAdjustment(new Adjustment(p, BigDecimal.valueOf(2), Operation.SUBSTRACT));
		assertEquals(BigDecimal.valueOf(24-2*6), p.getTotal());
		assertEquals(Integer.valueOf(6), p.getQuantity());
	}
	@Test public void twoMultiSalesMultiplyBy1TotalNotChanged() {
		p.addSale(new Sale(p, BigDecimal.TEN, 2)); 
		p.addSale(new Sale(p, BigDecimal.ONE, 4));
		p.addAdjustment(new Adjustment(p, BigDecimal.valueOf(1), Operation.MULTIPLY));
		assertEquals(p.getTotal(), BigDecimal.valueOf(24));
		assertEquals(p.getQuantity(), Integer.valueOf(6));
	}
	@Test public void twoMultiSalesMultiplyTotal() {
		p.addSale(new Sale(p, BigDecimal.TEN, 2)); //20*2
		p.addSale(new Sale(p, BigDecimal.ONE, 4));// 2 * 4
		p.addAdjustment(new Adjustment(p, BigDecimal.valueOf(2), Operation.MULTIPLY));
		assertEquals(BigDecimal.valueOf(24*2), p.getTotal());
		assertEquals(Integer.valueOf(6), p.getQuantity());	
	}
	
	@Test public void fourMultiSalesMultiplyTotal() {
		p.addSale(new Sale(p, BigDecimal.TEN, 2));
		p.addSale(new Sale(p, BigDecimal.ONE, 4));
		p.addSale(new Sale(p, BigDecimal.valueOf(2), 3));
		p.addSale(new Sale(p, BigDecimal.ONE));
		p.addAdjustment(new Adjustment(p, BigDecimal.valueOf(4), Operation.MULTIPLY));
		assertEquals(BigDecimal.valueOf(31*4), p.getTotal());
		assertEquals(Integer.valueOf(10), p.getQuantity());	
	}
	
	@Test public void multiAdjustments() {
		p.addSale(new Sale(p, BigDecimal.TEN, 2));
		p.addSale(new Sale(p, BigDecimal.ONE, 4));
		p.addAdjustment(new Adjustment(p, BigDecimal.valueOf(2), Operation.MULTIPLY));
		assertEquals(BigDecimal.valueOf(48), p.getTotal());
		p.addAdjustment(new Adjustment(p, BigDecimal.valueOf(2), Operation.SUBSTRACT));
		assertEquals(BigDecimal.valueOf(48-2*6), p.getTotal());
		p.addSale(new Sale(p, BigDecimal.valueOf(2), 3));
		p.addAdjustment(new Adjustment(p, BigDecimal.valueOf(1), Operation.SUBSTRACT));
		assertEquals(BigDecimal.valueOf(48-12+6-9), p.getTotal());
		p.addSale(new Sale(p, BigDecimal.ONE));
		p.addAdjustment(new Adjustment(p, BigDecimal.valueOf(4), Operation.ADD));
		assertEquals(BigDecimal.valueOf(48-12+6-9+1+4*10), p.getTotal());
		assertEquals(Integer.valueOf(10), p.getQuantity());	
	}
	
	@Test(expected=IllegalArgumentException.class) public void productNameCannotBeNull() {
		Product p = new Product(null);
	}
	@Test(expected=IllegalArgumentException.class) public void productNameCannotBeEmpty() {
		Product p = new Product("");
	}
	
	@Test public void newProductQuantityTotalAreZero() {
		assertEquals(Integer.valueOf(0), p.getQuantity());
		assertEquals(BigDecimal.ZERO, p.getTotal());
	}
	
	@Test public void onlyAdjustments() {
		p.addAdjustment(new Adjustment(p, BigDecimal.valueOf(2), Operation.MULTIPLY));
		p.addAdjustment(new Adjustment(p, BigDecimal.valueOf(2), Operation.SUBSTRACT));
		p.addAdjustment(new Adjustment(p, BigDecimal.valueOf(1), Operation.SUBSTRACT));
		p.addAdjustment(new Adjustment(p, BigDecimal.valueOf(4), Operation.ADD));
		assertEquals(BigDecimal.ZERO, p.getTotal());
		assertEquals(Integer.valueOf(0), p.getQuantity());	
	}
}
