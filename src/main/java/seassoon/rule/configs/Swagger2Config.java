package seassoon.rule.configs;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.OperationsSorter;
import springfox.documentation.swagger.web.TagsSorter;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 描述
 *
 * @author Y.S.K
 * @date 2018/6/9 11:46
 */
@Configuration
@EnableSwagger2
@ConditionalOnProperty(name = "enabled", prefix = "swagger", havingValue = "true", matchIfMissing = true)
public class Swagger2Config {

    @Bean
    UiConfiguration uiConfig() {
        return UiConfigurationBuilder
                .builder()
                .tagsSorter(TagsSorter.ALPHA)
                .operationsSorter(OperationsSorter.ALPHA)

                .build();
    }

    @Bean
    public Docket api() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("authorization").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());


        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .tags(new Tag("session", "session", 0),
                        new Tag[]{
                                new Tag("admin", "管理员", 20),
                                new Tag("role", "角色", 20),
                                new Tag("permission", "权限许可", 30),

                        })
                .select()

                // 自行修改为自己的包路径
                .apis(RequestHandlerSelectors.basePackage("seassoon.fwdemo.controller"))
                .paths(PathSelectors.any())


                .build()
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts())
                ;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("api文档")
                .description(" api 接入教程")
                //服务条款网址
                // .termsOfServiceUrl("https://blog.csdn.net/ysk_xh_521")
                .version("1.0")

                //  .contact(new Contact("Y.S.K", "http://ysk521.cn", "1176971130@qq.com"))
                .build();
    }


    private List<ApiKey> securitySchemes() {
        return Arrays.asList(
                new ApiKey("Authorization", "Authorization", "header"));
    }

    private List<SecurityContext> securityContexts() {
        return Arrays.asList(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .forPaths(PathSelectors.regex("^(?!auth).*$"))
                        .build()
        );
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(
                new SecurityReference("Authorization", authorizationScopes));
    }


}
