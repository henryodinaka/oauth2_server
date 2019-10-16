package market.henry.auth.config;

import com.google.common.collect.Lists;
import market.henry.auth.enums.EncryptionHeader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

@Configuration
@Profile({"dev","test"})
@EnableSwagger2
public class SwaggerConfig {

//    public static final String CLIENT_CODE = Constants.CLIENT_CODE;
    private static final String AUTHORIZATION = EncryptionHeader.AUTHORIZATION.getName();
    private static final String CHANNEL_CODE = EncryptionHeader.CHANNEL_CODE.getName();
    private static final String PUBLIC_KEY = EncryptionHeader.PUBLIC_KEY.getName();
    private static final String CLIENT_ID = EncryptionHeader.CLIENT_ID.getName();
//    public static final String SIGNATURE_METH = EncryptionHeader.SIGNATURE_METH.getName();

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("market.henry.auth.controller"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(Arrays.asList(apiKeyChannelCode(), apiKeyAuthorization(), apiKeyClientId(), apiKeyPublicKey()))
                /*.securitySchemes(Arrays.asList(apiKeyAuthorization()))*/
                .securityContexts(Arrays.asList(securityContext()));
    }

    private ApiInfo getApiInfo(){
        return new ApiInfoBuilder()
                .title("OAUTH 2 SERVER")
                .description("This is provides the platform for authenticating and authorization of clients.")
                .version("2.0")
                .build();
    }

//    private ApiKey apiKeyClientCode() {
//        return new ApiKey(CLIENT_CODE, CLIENT_CODE, "header");
//    }

    private ApiKey apiKeyAuthorization() {
        return new ApiKey(AUTHORIZATION, AUTHORIZATION, "header");
    }

    private ApiKey apiKeyChannelCode() {
        return new ApiKey(CHANNEL_CODE, CHANNEL_CODE, "header");
    }

    private ApiKey apiKeyPublicKey() {
        return new ApiKey(PUBLIC_KEY, PUBLIC_KEY, "header");
    }

    private ApiKey apiKeyClientId() {
        return new ApiKey(CLIENT_ID, CLIENT_ID, "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
//                .forPaths(PathSelectors.regex(DEFAULT_INCLUDE_PATTERN))
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(
                new SecurityReference(CHANNEL_CODE, authorizationScopes),
                new SecurityReference(AUTHORIZATION, authorizationScopes),
                new SecurityReference(PUBLIC_KEY, authorizationScopes),
                new SecurityReference(CLIENT_ID, authorizationScopes));
    }

}
