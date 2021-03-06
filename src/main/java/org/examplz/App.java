package org.examplz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App {
    public static void main( String[] args )
    {
        SpringApplication.run(App.class, args);
    }

//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/signIn").allowedOrigins("*");
//                registry.addMapping("/validate/*").allowedOrigins("*");
//                registry.addMapping("/register/user").allowedOrigins("*");
//                registry.addMapping("/register/sale").allowedOrigins("*");
//            }
//        };
//    }
}
