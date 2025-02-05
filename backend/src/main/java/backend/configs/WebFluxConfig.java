package backend.configs;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import backend.rdb.entities.converters.ProductCategoryToStringConverter;
import backend.rdb.entities.converters.SaleStatusToStringConverter;
import backend.rdb.entities.converters.StringToProductCategoryConverter;
import backend.rdb.entities.converters.StringToSaleStatusConverter;

@Configuration
@EnableTransactionManagement
@EnableWebFlux
public class WebFluxConfig implements WebFluxConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new ProductCategoryToStringConverter());
        registry.addConverter(new StringToProductCategoryConverter());
        registry.addConverter(new SaleStatusToStringConverter());
        registry.addConverter(new StringToSaleStatusConverter());
    }

    @Bean
    public ReactiveTransactionManager reactiveTransactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }

    @Bean
    public TransactionalOperator transactionalOperator(ReactiveTransactionManager  transactionManager) {
        return TransactionalOperator.create(transactionManager);
    }

  @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Додає CORS для всіх маршрутів
                .allowedOrigins("http://localhost:4200")  // Дозволяє доступ лише з фронтенду
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Дозволяє ці HTTP методи
                .allowedHeaders("*")  // Дозволяє всі заголовки
                .allowCredentials(true);  // Дозволяє надсилати кукі
    }

}

