package ms_creditos.ms_creditos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsCreditosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsCreditosApplication.class, args);
	}

}
