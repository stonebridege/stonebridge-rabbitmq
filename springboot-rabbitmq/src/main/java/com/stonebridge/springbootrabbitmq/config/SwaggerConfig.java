package com.stonebridge.springbootrabbitmq.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    /**
     * 创建Docket类型的对象，并使用spring容器管理
     * Docke是swagger中的全局配置对象
     *
     * @return
     */
    @Bean
    public Docket getDocket() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(swaggerDemoApiInfo())
                .select().build();
    }

    private ApiInfo swaggerDemoApiInfo() {
        //api帮助文档的描述信息。information。
        return new ApiInfoBuilder()
                .contact(new Contact(     //配置swagger文档主题内容
                        "stonebridge的swagger练习项目的开发接口文档", //是文档的发布者这名称
                        "http://www.bjsxt.com",  //文档发布者的网站地址，一般为企业网站
                        "xxx@163.com"))   //文档发布者的电子邮箱
                //文档标题
                .title("这里是开发接口文档的标题")
                //文档描述
                .description("这里是开发接口文档的描述")
                //文档版本
                .version("1.0.1")
                .build();
    }
}
