package com.example.demo.config.swagger;

public class SwaggerUiWebMvcConfigurer //implements WebMvcConfigurer
 {


/*
    private final String baseUrl;

    public SwaggerUiWebMvcConfigurer(
        @Value("${springfox.documentation.swagger-ui.base-url}") String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String baseUrl = StringUtils.trimTrailingCharacter(this.baseUrl, '/');
        registry.
            addResourceHandler(baseUrl + "/swagger-ui/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
            .resourceChain(false);
     registry.addResourceHandler("swagger-ui.html")
             .addResourceLocations("classpath:/META-INF/resources/");


     registry.addResourceHandler("/webjars/**")
             .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
     registry.addViewController(baseUrl + "/swagger-ui/")
             .setViewName("forward:" + baseUrl + "/swagger-ui/index.html");
    }

 */


}