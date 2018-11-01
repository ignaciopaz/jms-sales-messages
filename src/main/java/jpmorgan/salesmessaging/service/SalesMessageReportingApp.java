package jpmorgan.salesmessaging.service;

import java.util.Collection;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import jpmorgan.salesmessaging.domain.Product;
import jpmorgan.salesmessaging.message.AdjustmentMessage;
import jpmorgan.salesmessaging.message.SaleMessage;
import jpmorgan.salesmessaging.processor.SalesMessagesProcessor;
import jpmorgan.salesmessaging.report.SalesReport;
import jpmorgan.salesmessaging.repository.ProductRepository;
import jpmorgan.salesmessaging.repository.ProductRepositoryMemImpl;

/**
 * @author ignacio paz
 * This class can be used stand alone called directly from a service that receives messages
 * or inside Spring framework receiving JMS messages.
 */
@Scope(value = "singleton")
@Service
public class SalesMessageReportingApp {

	private SalesMessagesProcessor processor;
	private ProductRepository productRepository;
	private SalesReport salesReport;
	private Integer reportFrequency;
	private Integer limit;
	private Integer processed;
	
	public SalesMessageReportingApp() {
		this(10, 50); //default configuration values
	}
	
	public SalesMessageReportingApp(Integer reportFrequency, Integer limit) {
		this.limit = limit;
		this.reportFrequency = reportFrequency;
		processed = 0;
		productRepository = new ProductRepositoryMemImpl();
		processor = new SalesMessagesProcessor(productRepository);
		salesReport = new SalesReport();
	}
	
	public void receive(SaleMessage message) {
		checkLimit();
		processor.process(message);
		processed++;
		report();
	}
	
	public void receive(AdjustmentMessage message) {
		checkLimit();
		processor.process(message);
		processed++;
		report();
	}

	private void checkLimit() {
		if (processed >= limit) {
			System.out.println("Message Limit of " + limit + " reached");
			return;
		}
	}
	
	

	private void report() {
		productsReport();		
		adjustmentsReport();
	}

	private void adjustmentsReport() {
		if (processed % limit == 0) {
			//I can retrieve sorted products instead but it is not required and it will be slower.
			//It is not clear how to show adjustments details,
			// so I assume it is by product as adjustments are applied to the sales of a given product
			Collection<Product> products = productRepository.findAll();
			salesReport.adjustmentsReport(products);
		}
	}

	private void productsReport() {
		if (processed <= limit && processed % reportFrequency == 0) {
			//There are no requirements to sort the list. It can be slower.
			//If sorting is not important and faster output is needed findAll() can be used
			Collection<Product> products = productRepository.findAllSortedBySales();
			salesReport.productsReport(products);
		}
	}
	
}
