package jpmorgan.salesmessaging.domain;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

public class SaleTest {

	@Test(expected=IllegalArgumentException.class) public void singleSaleMustHaveAProduct() {
		Sale sale = new Sale(null, BigDecimal.ONE);
	}
	
	@Test(expected=IllegalArgumentException.class) public void multiSaleMustHaveAProduct() {
		Sale sale = new Sale(null, BigDecimal.ONE, 2);
	}
	
	@Test(expected=IllegalArgumentException.class) public void singleSaleMustHaveAPrice() {
		Sale sale = new Sale(new Product("A"), null);
	}
	
	@Test(expected=IllegalArgumentException.class) public void multiSalePriceCannotBeNegative() {
		Sale sale = new Sale(new Product("A"), BigDecimal.valueOf(-1), 2);
	}
	
	@Test(expected=IllegalArgumentException.class) public void singleSalePriceCannotBeNegative() {
		Sale sale = new Sale(new Product("A"), BigDecimal.valueOf(-1));
	}
	
	@Test(expected=IllegalArgumentException.class) public void saleQuantityMustBePositive() {
		Sale sale = new Sale(new Product("A"), BigDecimal.valueOf(1), -1);
	}
	@Test(expected=IllegalArgumentException.class) public void saleQuantityCannotBeZero() {
		Sale sale = new Sale(new Product("A"), BigDecimal.valueOf(1), 0);
	}
	
	@Test public void singleSaleTotal() {
		Sale sale = new Sale(new Product("A"), BigDecimal.valueOf(1));
		assertEquals(BigDecimal.ONE, sale.getTotal());
	}
	@Test public void singleSaleTotalDecimal() {
		Sale sale = new Sale(new Product("A"), BigDecimal.valueOf(12.3));
		assertEquals(BigDecimal.valueOf(12.3), sale.getTotal());
	}
	@Test public void multiSaleTotal2Sales() {
		Sale sale = new Sale(new Product("A"), BigDecimal.valueOf(12.3),2);
		assertEquals(BigDecimal.valueOf(24.6), sale.getTotal());
	}
	@Test public void multiSaleTotal5Sales() {
		Sale sale = new Sale(new Product("A"), BigDecimal.valueOf(32),5);
		assertEquals(BigDecimal.valueOf(32*5), sale.getTotal());
	}
}
