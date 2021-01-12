package seassoon.rule;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@MapperScan("seassoon.rule.mapper")
public class App extends SpringBootServletInitializer{

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    /**
     * war包启动
     */
    @Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(App.class);
    }

}
