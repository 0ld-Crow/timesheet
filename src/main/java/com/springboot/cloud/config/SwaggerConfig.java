package com.springboot.cloud.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
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
/**
 * @EnableSwaggerBootstrapUI
 * 在Swagger的配置文件中引入增强注解才可以自定义排序功能
 * 【@Api中的position属性，@ApiOperation中的order属性】
 */
@EnableSwaggerBootstrapUI
public class SwaggerConfig {


    //该套API的说明（简介）
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("timesheet api文档")
                .description("timesheet api文档")
                .version("1.0")
                .build();
    }

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)

                // 把该套API的说明弄进来
                .apiInfo(apiInfo())

                //用来控制哪些接口暴露给Swagger2来展现，就是哪些东西用来生成文档
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.springboot.cloud.app.timesheet.rest"))//扫描rest包下的所有API
                .paths(PathSelectors.any())//扫描所有的路径



                .build();
    }
}
