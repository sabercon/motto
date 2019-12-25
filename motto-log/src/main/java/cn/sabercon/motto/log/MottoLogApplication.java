package cn.sabercon.motto.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@ComponentScan({"cn.sabercon.motto.common", "cn.sabercon.motto.log"})
public class MottoLogApplication {

    public static void main(String[] args) {
        SpringApplication.run(MottoLogApplication.class, args);
    }
}
