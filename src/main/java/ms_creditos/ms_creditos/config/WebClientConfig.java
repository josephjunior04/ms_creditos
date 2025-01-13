package ms_creditos.ms_creditos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    /**
     * @return Flux Credit response to all credits
     */
    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}
