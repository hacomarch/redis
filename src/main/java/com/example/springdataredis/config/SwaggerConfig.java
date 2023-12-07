package com.example.springdataredis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
//API를 쉽게 테스트할 수 있음
public class SwaggerConfig {

    @Bean
    public Docket api() { //Docket : Swagger에 대한 메인 설정 클래스, API 문서를 빌드하는데 사용된다.
        return new Docket(DocumentationType.SWAGGER_2) //Swagger 2.0으로 설정
                .select() //API 문서를 어떻게 선택할지 설정
                .paths(PathSelectors.any()) //모든 경로를 대상으로 API 문서 생성하도록 설정
                .apis(RequestHandlerSelectors.basePackage("com.example.springdataredis.controller")) //컨트롤러 패키지의 클래스들을 기준으로 API 문서 생성하도록 설정
                .build().apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() { //Docket에 대한 API 정보를 설정하는 메서드
        return new ApiInfoBuilder() //API 기본 정보 설정
                .title("LowPriceWithRedis API") //API 문서의 제목 설정
                .description("LowPriceWithRedis API Document") //API 문서의 설명 설정
                .version("1.0") //API 버전 설정
                .build();
    }
}
