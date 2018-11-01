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
public class AdjustmentMessageTest {
	
	private String product;
	private BigDecimal price;
	private Operation operation;

	public AdjustmentMessageTest(String product, BigDecimal price, Operation operation) {
		super();
		this.product = product;
		this.price = price;
		this.operation = operation;
	}
	
	@Test(expected=IllegalArgumentException.class) public void testAdjustment(){
		
		AdjustmentMessage message = new AdjustmentMessage(product, price, operation);
	}

	@Parameters
    public static Collection<Object[]> data() throws ParseException {
    	
		return Arrays.asList(new Object[][] {     
                       
			{ null, null, Operation.ADD},
			{ null, BigDecimal.ONE, Operation.ADD},
            { "", BigDecimal.ONE, Operation.ADD},
            { "A", null, Operation.ADD},
            { "A", BigDecimal.valueOf(-1), Operation.ADD},
            { "A", BigDecimal.ONE, null},
		});
    }

 
}
