package com.example.demo.config.swagger;

import org.springframework.context.annotation.Configuration;

@Configuration
//@EnableSwagger2
public class SwaggerConfig {

/*
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(a->true)
                .paths(PathSelectors.any())
                .build()
                .apiInfo(new ApiInfo(
                        "TaskMaster API",
                        "API for task tracking system",
                        "1.0",
                        "Terms of service",
                        new Contact("Alex", "java_er", "lexagri200430@gmail.com"),
                        "Лицензия", "Лицензия URL", Collections.emptyList()))
                .securitySchemes(List.of(apiKey()))
                .securityContexts(Collections.singletonList(securityContext()))
                .useDefaultResponseMessages(false)
               .globalResponses(org.springframework.http.HttpMethod.GET, List.of(
                        new ResponseBuilder().code("500")
                                .description("Server Error").build(),
                        new ResponseBuilder().code("403")
                                .description("Forbidden!!!!!").build(),
                        new ResponseBuilder().code("401")
                                .description("Unauthorized!!!!!").build()
                ))
                .globalResponses(org.springframework.http.HttpMethod.POST,  List.of(
                        new ResponseBuilder().code("500")
                                .description("Server Error").build(),
                        new ResponseBuilder().code("403")
                                .description("Forbidden!!!!!").build(),
                        new ResponseBuilder().code("401")
                                .description("Unauthorized!!!!!").build()
                ))
                .globalResponses(org.springframework.http.HttpMethod.PUT, List.of(
                        new ResponseBuilder().code("500")
                                .description("Server Error").build(),
                        new ResponseBuilder().code("403")
                                .description("Forbidden!!!!!").build(),
                        new ResponseBuilder().code("401")
                                .description("Unauthorized!!!!!").build()
                ))
                .globalResponses(org.springframework.http.HttpMethod.DELETE, List.of(
                        new ResponseBuilder().code("500")
                                .description("Server Error").build(),
                        new ResponseBuilder().code("403")
                                .description("Forbidden!!!!!").build(),
                        new ResponseBuilder().code("401")
                                .description("Unauthorized!!!!!").build()
                ))
                .globalResponses(HttpMethod.PATCH,  List.of(
                        new ResponseBuilder().code("500")
                                .description("Server Error").build(),
                        new ResponseBuilder().code("403")
                                .description("Forbidden!!!!!").build(),
                        new ResponseBuilder().code("401")
                                .description("Unauthorized!!!!!").build()
                ))
                ;
    }

    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.ant("/taskmaster/**")) // Замените на нужные вам пути
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        return Collections.singletonList(SecurityReference.builder()
                .reference("JWT")
                .scopes(new AuthorizationScope[0])
                .build());
    }

 */



}
