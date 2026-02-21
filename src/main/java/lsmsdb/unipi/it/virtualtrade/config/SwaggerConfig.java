package lsmsdb.unipi.it.virtualtrade.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI virtualTradeOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("VirtualTrade API")
                        .description("Polyglot Trading Platform API backed by MongoDB and Redis")
                        .version("1.0.0"));
    }
}