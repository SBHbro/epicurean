package com.web.curation.config;

import com.google.common.base.Predicates;

import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api(ServletContext servletContext){
        return new Docket(DocumentationType.SWAGGER_2)
        		.pathProvider(new RelativePathProvider(servletContext) {
                    @Override
                    public String getApplicationBasePath() {
                        return "/api";
                    }
                })
        		.apiInfo(this.apiInfo())
        		.select()
                .apis(Predicates.not(RequestHandlerSelectors.
                        basePackage("org.springframework.boot")))
                .paths(PathSelectors.any()).build();
    }
    
    private ApiInfo apiInfo() {
        return new ApiInfo(
                "돈독한미식가",
                "맛집 추천 & 밋업 SNS",
                "0.0.6",
                "https://github.com/SBHbro/epicurean/",
                new Contact("Contact Me", "", "ssd018@gmail.com"),
                "Licenses",
                "https://github.com/SBHbro1",
                new ArrayList<>()
        );
    }
}
