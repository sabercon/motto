package cn.sabercon.motto.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.TimeZone;

@SpringBootApplication
@ComponentScan({"cn.sabercon.motto.common", "cn.sabercon.motto.log"})
public class MottoLogApplication {

    public static void main(String[] args) {
        // 解决放到服务器上时出现的时区问题
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        SpringApplication.run(MottoLogApplication.class, args);
    }
}
