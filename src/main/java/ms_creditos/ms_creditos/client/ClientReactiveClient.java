package ms_creditos.ms_creditos.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import ms_creditos.ms_creditos.util.ClientResponse;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(value = "api-gateway", path = "ms-client")
public interface ClientReactiveClient {

    @GetMapping("/v1/clients/{id}")
    Mono<ClientResponse> findById(@PathVariable String id);
}
