package tn.request.app;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableJpaRepositories("tn.request.*")
@EntityScan("tn.request.*")
@RestController
@RequestMapping("/")
@ComponentScan(basePackages = "tn.request.*")
public class MainApp implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(MainApp.class, args);
    }

    @GetMapping
    public String getMessage() {
        return "<h1> Hello World </h1>";
    }

    @Override
    public void run(String... args) {
    }
}
