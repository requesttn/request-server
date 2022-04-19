package tn.request.app;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.request.data.user.UserEntity;
import tn.request.data.user.UserRepository;

@AllArgsConstructor
@SpringBootApplication
@EnableJpaRepositories("tn.request.*")
@EntityScan("tn.request.*")
@RestController
@RequestMapping("/")
@ComponentScan(basePackages = "tn.request.*")
public class MainApp implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

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
        userRepository.save(new UserEntity(null, "Test1", "Test", "test1@test.com", true));
        userRepository.save(new UserEntity(null, "Test2", "Test", "test2@test.com", false));
    }
}
