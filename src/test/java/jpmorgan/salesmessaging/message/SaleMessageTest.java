package jpmorgan.salesmessaging.message;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import jpmorgan.salesmessaging.domain.Operation;
@RunWith(Parameterized.class)
public class SaleMessageTest {
	
	private String product;
	private BigDecimal price;
	private Integer quantity;
	@Rule
    public ExpectedException thrown= ExpectedException.none();


	public SaleMessageTest(String product, BigDecimal price, Integer quantity) {
		super();
		this.product = product;
		this.price = price;
		this.quantity = quantity;
	}
	@Test public void testSale(){
		if (invalidQuantity()) {
			ExpectedException.none();
		} else {
			thrown.expect(IllegalArgumentException.class);
		}
		SaleMessage message = new SaleMessage(product, price);
	}
	@Test(expected=IllegalArgumentException.class) public void testMultiSale(){
		
		SaleMessage message = new SaleMessage(product, price, quantity);
	}

	@Parameters
    public static Collection<Object[]> data() throws ParseException {
    	
		return Arrays.asList(new Object[][] {     
                       
			{ null, null, 1},
			{ null, BigDecimal.ONE, 1},
            { "", BigDecimal.ONE, 1},
            { "A", null, 1},
            { "A", BigDecimal.valueOf(-1), 1},
            { "A", BigDecimal.ONE, null},
            { "A", BigDecimal.ONE, 0},
            { "A", BigDecimal.ONE, -1}, 
            { "A", BigDecimal.ONE, null},
		});
    }

    private boolean invalidQuantity() {
    	return quantity == null  || quantity <1;
    }
  
}
