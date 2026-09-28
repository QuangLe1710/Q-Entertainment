package com.app.Q_Entertainment;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.TimeZone;

@SpringBootApplication
public class QEntertainmentApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
		SpringApplication.run(QEntertainmentApplication.class, args);
	}

    @Bean
    public CommandLineRunner printAllBeans(ApplicationContext ctx) {
        return args -> {
            System.out.println("=== Total Beans: " + ctx.getBeanDefinitionCount() + " ===");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);

            for (String beanName : beanNames) {
                Object bean = ctx.getBean(beanName);
                System.out.println(beanName + " -> " + bean.getClass().getName());
            }
        };
    }

}
