package jpmorgan.salesmessaging.service;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jpmorgan.salesmessaging.domain.Operation;
import jpmorgan.salesmessaging.message.AdjustmentMessage;
import jpmorgan.salesmessaging.message.SaleMessage;

public class SalesMessageReportingAppTest {
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;
	private final PrintStream originalErr = System.err;
	private SalesMessageReportingApp app;
	private Integer reportFrequency;
	private Integer limit;
	
	@Before public void beforeClass() {
		reportFrequency = 10;
		limit = 50;
	}
	@Before public void setUpStreams() {
		app = new SalesMessageReportingApp();
	    System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));
	}

	@After public void restoreStreams() {
	    System.setOut(originalOut);
	    System.setErr(originalErr);
	}
	
	@Test(expected = IllegalArgumentException.class) public void emptySaleMessageIsRejected() {
		SaleMessage message = new SaleMessage();
		app.receive(message);
	}
	
	@Test(expected = IllegalArgumentException.class) public void emptyAdjustmentMessageIsRejected() {
		AdjustmentMessage message = new AdjustmentMessage();
		app.receive(message);
	}
	
	@Test public void productReportIsPrintedEvery20Messages() {
		for (int i=1; i <= reportFrequency; i++) {
			assertEquals("", outContent.toString());
			addSale("p1", 5);
		}
		String expected = "#### Product Sales Report ####\r\n"+
				"Product: p1 - Total: 50,00 € - Total sales: 10\r\n";
		assertEquals(expected, outContent.toString());
		
		for (int i=1; i <= reportFrequency; i++) {
			assertEquals(expected, outContent.toString());
			addSale("p1", 5);
		}
		expected += "#### Product Sales Report ####\r\n"+
				"Product: p1 - Total: 100,00 € - Total sales: 20\r\n";
		assertEquals(expected, outContent.toString());
	}
	
	@Test public void limitIsReached() {
		for (int i=1; i <= limit; i++) {
			addSale("p1", 5);
		}
		//TODO: report template and variables must be improved.
		String expected = "#### Product Sales Report ####\r\n"+
				"Product: p1 - Total: 50,00 € - Total sales: 10\r\n";
		expected += "#### Product Sales Report ####\r\n"+
				"Product: p1 - Total: 100,00 € - Total sales: 20\r\n";
		expected += "#### Product Sales Report ####\r\n"+
				"Product: p1 - Total: 150,00 € - Total sales: 30\r\n";
		expected += "#### Product Sales Report ####\r\n"+
				"Product: p1 - Total: 200,00 € - Total sales: 40\r\n";
		expected += "#### Product Sales Report ####\r\n"+
				"Product: p1 - Total: 250,00 € - Total sales: 50\r\n";
		expected +=	"#### Adjustments Report ####\r\n";
		assertEquals(expected, outContent.toString());
		
		for (int i=1;i<=reportFrequency+1; i++) {
			addSale("p1", 5);
			expected +=	"Message Limit of 50 reached\r\n";
			assertEquals(expected, outContent.toString());			
		}
	}
	
	@Test public void reportIsPrintedAndSorted() {
		addSale("p1", 5);
		addSale("p2", 3);
		addSale("p2", 3, 2);
		addSale("p3", 1, 3);
		adjustAdd("p1", 1);
		adjustAdd("p2", 1);
		adjustAdd("p3", 1);
		adjustSubstract("p1", 3);
		adjustSubstract("p3", 1);
		adjustMultiply("p3", 1);
		for (int i=1;i<=42; i++) {
			addSale("p1", 5);
		}
		//TODO: variable and report template must be improved
		String expected = "#### Product Sales Report ####\r\n"+
		"Product: p2 - Total: 12,00 € - Total sales: 3\r\n"+
		"Product: p3 - Total: 3,00 € - Total sales: 3\r\n"+
		"Product: p1 - Total: 3,00 € - Total sales: 1\r\n"+
		"#### Product Sales Report ####\r\n"+
		"Product: p1 - Total: 53,00 € - Total sales: 11\r\n"+
		"Product: p2 - Total: 12,00 € - Total sales: 3\r\n"+
		"Product: p3 - Total: 3,00 € - Total sales: 3\r\n"+
		"#### Product Sales Report ####\r\n"+
		"Product: p1 - Total: 103,00 € - Total sales: 21\r\n"+
		"Product: p2 - Total: 12,00 € - Total sales: 3\r\n"+
		"Product: p3 - Total: 3,00 € - Total sales: 3\r\n"+
		"#### Product Sales Report ####\r\n"+
		"Product: p1 - Total: 153,00 € - Total sales: 31\r\n"+
		"Product: p2 - Total: 12,00 € - Total sales: 3\r\n"+
		"Product: p3 - Total: 3,00 € - Total sales: 3\r\n"+
		"#### Product Sales Report ####\r\n"+
		"Product: p1 - Total: 203,00 € - Total sales: 41\r\n"+
		"Product: p2 - Total: 12,00 € - Total sales: 3\r\n"+
		"Product: p3 - Total: 3,00 € - Total sales: 3\r\n"+
		"#### Adjustments Report ####\r\n"+
		"Product: p1 - Amount: 1,00 € - Operation: ADD - Total Before: 5,00 € - Total After: 6,00 € - Processed Sales: 1\r\n"+
		"Product: p1 - Amount: 3,00 € - Operation: SUBSTRACT - Total Before: 6,00 € - Total After: 3,00 € - Processed Sales: 1\r\n"+
		"Product: p2 - Amount: 1,00 € - Operation: ADD - Total Before: 9,00 € - Total After: 12,00 € - Processed Sales: 3\r\n"+
		"Product: p3 - Amount: 1,00 € - Operation: ADD - Total Before: 3,00 € - Total After: 6,00 € - Processed Sales: 3\r\n"+
		"Product: p3 - Amount: 1,00 € - Operation: SUBSTRACT - Total Before: 6,00 € - Total After: 3,00 € - Processed Sales: 3\r\n"+
		"Product: p3 - Amount: 1,00 € - Operation: MULTIPLY - Total Before: 3,00 € - Total After: 3,00 € - Processed Sales: 3\r\n"+
		"Message Limit of 50 reached\r\n"+
		"Message Limit of 50 reached\r\n";
		assertEquals(expected, outContent.toString());
	}
	
	private void adjustAdd(String pId, double amount) {
		app.receive(new AdjustmentMessage(pId, BigDecimal.valueOf(amount), Operation.ADD));
	}
	private void adjustSubstract(String pId, double amount) {
		app.receive(new AdjustmentMessage(pId, BigDecimal.valueOf(amount), Operation.SUBSTRACT));
	}
	private void adjustMultiply(String pId, int amount) {
		app.receive(new AdjustmentMessage(pId, BigDecimal.valueOf(amount), Operation.MULTIPLY));
	}

	private void addSale(String pId, double price, int quantity) {
		app.receive(new SaleMessage(pId, BigDecimal.valueOf(price), quantity));
		
	}
	private void addSale(String pId, double price) {
		app.receive(new SaleMessage(pId, BigDecimal.valueOf(price)));
	}
	
}
