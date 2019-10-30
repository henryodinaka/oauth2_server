package market.henry.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
//import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication
public class Oauth2ServerApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Oauth2ServerApplication.class);

    }

    public static void main(String[] args) {
        SpringApplication.run(Oauth2ServerApplication.class, args);
    }
//    @Bean
//    PasswordEncoder getEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}
