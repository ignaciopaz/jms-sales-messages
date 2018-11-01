package jpmorgan.salesmessaging.client;

import java.math.BigDecimal;

import jpmorgan.salesmessaging.domain.Operation;
import jpmorgan.salesmessaging.message.AdjustmentMessage;
import jpmorgan.salesmessaging.message.SaleMessage;

/**
 * @author ignacio paz
 * This class acts as a Facade for clients.
 * Clients can send messages using implementations of this class.
 * Clients coupling to SaleMessage class is avoided.
 * - Another implementation that sends Sale or Adjustment objects can be created.
 * - Another implementation that posts messages to a remote service or to a class instance 
 *   instead of JMS can be created.
 */
public abstract class AbstractSalesMessageSender implements SalesMessageSender {
	//template method
	protected abstract void send(SaleMessage message);
	protected abstract void send(AdjustmentMessage message);

	public void sendSale(String productName, Double price, Integer quantity) {
    	send(new SaleMessage(productName,BigDecimal.valueOf(price), quantity));
    }
    
	public void sendSale(String productName, Double price) {
		send(new SaleMessage(productName,BigDecimal.valueOf(price)));
    }
    
    private void sendAdjustment(String productName, Double ammount, Operation operation) {
    	//send(SaleMessage.createAdjustment(productName, BigDecimal.valueOf(ammount), operation));
    	send(new AdjustmentMessage(productName, BigDecimal.valueOf(ammount), operation));
    }

	public void sendAdd(String productName, Double ammount) {
		sendAdjustment(productName, ammount, Operation.ADD);
	}
	
	public void sendSubstract(String productName, Double ammount) {
		sendAdjustment(productName, ammount, Operation.SUBSTRACT);
	}
	
	public void sendMultiply(String productName, Double ammount) {
		sendAdjustment(productName, ammount, Operation.MULTIPLY);
	}

}
