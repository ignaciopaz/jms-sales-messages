package jpmorgan.salesmessaging.domain;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
@RunWith(Parameterized.class)
public class AdjustmentProductTest {

	private static Product p0, p1, p2;
	
	private BigDecimal amount;
	private Operation operation;
	private BigDecimal beforeExpected;
	private BigDecimal totalExpected;
	private Integer salesExpected;

	private Product product;

	static {
		beforeClass();
	}
	public AdjustmentProductTest (Product product, double amount, Operation operation, double totalExpected) {
		this.product = product;
		this.operation = operation;
		this.amount = BigDecimal.valueOf(amount);
		this.beforeExpected = product.getTotal();
		this.totalExpected = BigDecimal.valueOf(totalExpected);
		this.salesExpected = product.getQuantity();
	}
	
	@Before public void before() {
		beforeClass();
	}
	public static void beforeClass() {
		p0 = new Product("P0");
		p1 = new Product("P1");
		p1.addSale(new Sale(p1, BigDecimal.ONE));
		p2 = new Product("P2");
		p2.addSale(new Sale(p2, BigDecimal.valueOf(3), 2));
		p2.addSale(new Sale(p2, BigDecimal.valueOf(4), 3));
	}
	
	@Test public void test() {
		Adjustment a = new Adjustment(product, amount, operation);
		assertEquals(beforeExpected, a.getTotalBefore());
		assertEquals(totalExpected, a.getTotal());
		assertEquals(salesExpected, a.getProcessedSales());
	}
	
	@Parameters
    public static Collection<Object[]> data() throws ParseException {
    	
		return Arrays.asList(new Object[][] {     
                       
            { p0, 2.0, Operation.ADD, 0.0},
            { p0, 5.0, Operation.SUBSTRACT, 0.0},
            { p0, 5.0, Operation.MULTIPLY, 0.0},
            { p1, 2.0, Operation.ADD, 3.0},
            { p1, 5.0, Operation.SUBSTRACT, -4}, //should allow adjustment to negative?
            { p1, 1.0, Operation.SUBSTRACT, 0},
            { p1, 5.0, Operation.MULTIPLY, 5.0},
            { p2, 2.0, Operation.ADD, 18+2*5},
            { p2, 5.0, Operation.SUBSTRACT, 18-5*5}, //should allow adjustment to negative?
            { p2, 5.0, Operation.MULTIPLY, 18*5},
            { p2, 1, Operation.ADD, 18+5},
            { p2, 1.5, Operation.SUBSTRACT, 18-1.5*5},
            
		});
		      
    }
	
}
