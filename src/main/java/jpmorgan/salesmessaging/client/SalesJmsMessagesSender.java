package jpmorgan.salesmessaging.client;

import org.springframework.jms.core.JmsTemplate;

import jpmorgan.salesmessaging.message.AdjustmentMessage;
import jpmorgan.salesmessaging.message.SaleMessage;

public class SalesJmsMessagesSender extends AbstractSalesMessageSender {
	private String salesDestinationName;
	private String adjustmentDestinationName;
	private JmsTemplate jmsTemplate;
	
	public SalesJmsMessagesSender(JmsTemplate jmsTemplate, String destinationName, String adjustmentDestinationName) {
		this.jmsTemplate = jmsTemplate;
		this.salesDestinationName = destinationName;
		this.adjustmentDestinationName = adjustmentDestinationName;
	}
	public SalesJmsMessagesSender(JmsTemplate jmsTemplate) {
		this(jmsTemplate,"sales", "adjustments");
	}
	
	protected void send(SaleMessage message) {
		//adapter
    	jmsTemplate.convertAndSend(salesDestinationName, message);		
	}
	protected void send(AdjustmentMessage message) {
    	jmsTemplate.convertAndSend(adjustmentDestinationName, message);		
	}
}