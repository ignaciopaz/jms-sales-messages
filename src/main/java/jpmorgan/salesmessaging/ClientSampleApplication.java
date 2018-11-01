package jpmorgan.salesmessaging;

import javax.jms.ConnectionFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import jpmorgan.salesmessaging.client.SalesJmsMessagesSender;
import jpmorgan.salesmessaging.client.SalesMessageSender;

@SpringBootApplication
@EnableJms
public class ClientSampleApplication {
	@Bean
    public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory,
                                                    DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }
    
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ClientSampleApplication.class, args);
        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
        final SalesMessageSender sender = new SalesJmsMessagesSender(jmsTemplate);
        
        //multiple clients may be sending messages at the same time
        //Concurrency will be managed by the Queue receiving the messages.
        Thread thread1 = new Thread(() -> { sendSampleMessagesBatch1(sender); });
        Thread thread2 = new Thread(() -> { sendSampleMessagesBatch2(sender); });
        
        thread1.start();
        thread2.start();
    }

    private static void sendSampleMessagesBatch1(SalesMessageSender sender) {
		System.out.println("Sending sample messages from "+ Thread.currentThread().getName());
		sender.sendSale("cheese",1.0);
		for(int i=1 ; i < 20 ; i++) {
			sender.sendSale("book",1.0);
	
        	if (i % 4 == 0) {
        		 sender.sendAdd("book", 1.0);
        	}
        	if (i % 6 == 0) {
        		sender.sendSubstract("water", 1.0);
        	}
        }
	}
    
	private static void sendSampleMessagesBatch2(SalesMessageSender sender) {
		System.out.println("Sending sample messages from "+ Thread.currentThread().getName());
		sender.sendSale("cheese",1.05);
		for(int i=1 ; i < 20 ; i++) {
			sender.sendSale("water", i+1.0, i);
			sender.sendSale("shirt", 1.0, i+1);
       	
        	if (i % 10 == 0) {
        		sender.sendMultiply("water", 2.0);
        	}
        }
	}
    

}