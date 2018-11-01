package jpmorgan.salesmessaging.client;

/**
 * @author ignacio paz
 * This interface acts as a Facade for clients.
 * Clients can send messages using implementations of this interface.
 * Clients coupling to SaleMessage class is avoided.
 * - Another implementation that sends Sale or Adjustment objects can be created.
 * - Another implementation that posts messages to a remote service or to a class instance 
 *   instead of JMS can be created.
 */
public abstract interface SalesMessageSender {

	public void sendSale(String productName, Double price, Integer quantity);
    
	public void sendSale(String productName, Double price);
    
	public void sendAdd(String productName, Double ammount);
	
	public void sendSubstract(String productName, Double ammount);
	
	public void sendMultiply(String productName, Double ammount);

}
