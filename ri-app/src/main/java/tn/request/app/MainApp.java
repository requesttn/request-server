package tn.request.app;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tn.request.data.user.UserRepository;
import tn.request.domain.question.QuestionService;

@AllArgsConstructor
@SpringBootApplication
@EnableJpaRepositories("tn.request.*")
@EntityScan("tn.request.*")
@RestController
@RequestMapping("/")
@ComponentScan(basePackages = "tn.request.*")
public class MainApp implements CommandLineRunner {

    private UserRepository userRepository;
    private QuestionService questionService;

    @Autowired
    private Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(MainApp.class, args);
    }

    @GetMapping
    public String getMessage() {
        return "<h1> Hello World </h1>";
    }

    @Override
    public void run(String... args) {
        System.out.println(environment.getProperty("server.port"));
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:8080");
            }
        };
    }
}
