package jpmorgan.salesmessaging.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import jpmorgan.salesmessaging.message.AdjustmentMessage;
import jpmorgan.salesmessaging.message.SaleMessage;
/**
 * @author ignacio paz
 * This class can be used stand alone called directly from a service that receives messages
 * or inside Spring framework receiving JMS messages.

 */
@Scope(value = "singleton")
@Component
public class SalesMessageJmsReceiver {
	@Autowired private SalesMessageReportingApp salesMessageReportingApp;

	@JmsListener(destination = "sales", containerFactory = "myFactory")
	public void receive(SaleMessage message) {
		salesMessageReportingApp.receive(message);
	}
	
	@JmsListener(destination = "adjustments", containerFactory = "myFactory")
	public void receive(AdjustmentMessage message) {
		salesMessageReportingApp.receive(message);
	}
}
