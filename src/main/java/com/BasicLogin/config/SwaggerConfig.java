package com.BasicLogin.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;
import java.util.Collections;


@Configuration
@EnableSwagger2
@EnableAutoConfiguration
public class SwaggerConfig {
    //    @Bean
    ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Authentication API").description("API Reference").version("1.0.0").build();
    }

    @Bean
    public Docket customImplementation() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                .securitySchemes(Collections.singletonList(authorizationKey())).select().paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.BasicLogin")).build().pathMapping("/")
                .useDefaultResponseMessages(false).directModelSubstitute(LocalDate.class, String.class)
                .genericModelSubstitutes(ResponseEntity.class);
    }

    private ApiKey authorizationKey() {
        return new ApiKey("Authorization", "Authorization", "header"); // <<< === Create a Heaader (We are createing
    }

    @Bean
    SecurityConfiguration security() {
        return new SecurityConfiguration(null, null, null, null, null, ApiKeyVehicle.HEADER, "Authorization",
                ",");
    }

}