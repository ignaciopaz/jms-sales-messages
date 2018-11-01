package jpmorgan.salesmessaging.domain;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

public class AdjustmentTest {

	private Product p;
	@Before public void before() {
		p = new Product("A");
	}
	
	@Test(expected=IllegalArgumentException.class) public void adjustmentMustHaveAProduct() {
		Adjustment a = new Adjustment(null, BigDecimal.ONE, Operation.ADD);
	}
	@Test(expected=IllegalArgumentException.class) public void adjustmentMustHaveAmount() {
		Adjustment a = new Adjustment(p, null, Operation.ADD);
	}
	@Test(expected=IllegalArgumentException.class) public void adjustmentMustHaveOperaiont() {
		Adjustment a = new Adjustment(p, BigDecimal.ONE, null);
	}	
	@Test(expected=IllegalArgumentException.class) public void adjustmentAmountMustBePositive() {
		Adjustment a = new Adjustment(p, BigDecimal.valueOf(-2), Operation.ADD);
	}
	@Test(expected=IllegalArgumentException.class) public void adjustmentAmountCannotBeZero() {
		Adjustment a = new Adjustment(p, BigDecimal.ZERO, Operation.ADD);
	}
}
