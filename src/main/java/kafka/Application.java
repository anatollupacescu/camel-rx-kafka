package kafka;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.apache.camel.rx.ReactiveCamel;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class Application {

	static boolean startRx = false;

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		if (startRx) {
			context.getBean(RxObservable.class).start();
		}
	}

	@Configuration
	public static class CamelConfiguration {

		private static final String CAMEL_URL_MAPPING = "/camel/*";
		private static final String CAMEL_SERVLET_NAME = "CamelServlet";

		@Bean
		public ServletRegistrationBean servletRegistrationBean() {
			ServletRegistrationBean registration = new ServletRegistrationBean(new CamelHttpTransportServlet(),
					CAMEL_URL_MAPPING);
			registration.setName(CAMEL_SERVLET_NAME);
			return registration;
		}

		@Bean
		public SpringCamelContext camelContext(ApplicationContext applicationContext) throws Exception {
			SpringCamelContext camelContext = new SpringCamelContext(applicationContext);
			camelContext.addRoutes(kafkaRoute());
			return camelContext;
		}

		@Bean
		public ReactiveCamel reactiveCamel(SpringCamelContext camelContext) {
			return new ReactiveCamel(camelContext);
		}

		public @Bean RxObservable rx() {
			return new RxObservable();
		}

		@Bean
		public RoutesBuilder kafkaRoute() {
			return new KafkaRoute();
		}
	}
}