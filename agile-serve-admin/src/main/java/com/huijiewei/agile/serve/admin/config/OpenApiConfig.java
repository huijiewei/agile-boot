package com.huijiewei.agile.serve.admin.config;

import com.huijiewei.agile.app.user.consts.UserCreatedFrom;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author huijiewei
 */

@Configuration(proxyBeanMethods = false)
public class OpenApiConfig {

    @Bean
    public OpenAPI defineOpenApi() {
        return new OpenAPI()
                .info(defineInfo())
                .addSecurityItem(new SecurityRequirement().addList("ClientId").addList("AccessToken"))
                .components(new Components()
                        .addSchemas("DateRangeSearchRequestSchema", new ArraySchema()
                                .items(new Schema<>().type("string").format("date-time").uniqueItems(true))
                                .minItems(2)
                                .maxItems(2))
                        .addSchemas("UserCreatedFromSearchRequestSchema", defineUserCreatedFromSearchRequest())
                        .addResponses("NotFoundProblem", defineProblemResponse("资源不存在"))
                        .addResponses("BadRequestProblem", defineProblemResponse("无效的请求"))
                        .addResponses("ForbiddenProblem", defineProblemResponse("访问被拒绝"))
                        .addResponses("ConflictProblem", defineProblemResponse("资源有冲突"))
                        .addResponses("UnprocessableEntityProblem", defineUnprocessableEntityProblemResponse())
                        .addSecuritySchemes("ClientId", new SecurityScheme()
                                .name("X-Client-Id")
                                .description("客户端 Id")
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                        )
                        .addSecuritySchemes("AccessToken", new SecurityScheme()
                                .name("X-Access-Token")
                                .description("用户访问令牌")
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                        )
                );
    }

    private ArraySchema defineUserCreatedFromSearchRequest() {
        var schema = new Schema<String>();
        schema.setType("string");
        schema.setUniqueItems(true);

        schema.setEnum(Arrays.stream(UserCreatedFrom.values()).map(UserCreatedFrom::getValue).collect(Collectors.toList()));

        return new ArraySchema().items(schema);
    }

    private ApiResponse defineUnprocessableEntityProblemResponse() {
        return new ApiResponse()
                .description("输入验证错误")
                .content(new Content()
                        .addMediaType(org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE, new MediaType()
                                .schema(new Schema<>()
                                        .type("object")
                                        .addProperties("type", new Schema<>().type("string").description("错误类型"))
                                        .addProperties("status", new Schema<>().type("integer").description("错误状态"))
                                        .addProperties("title", new Schema<>().type("string").description("错误标题"))
                                        .addProperties(
                                                "violations",
                                                new ArraySchema()
                                                        .items(new Schema<>()
                                                                .addProperties("field", new Schema<>().type("string").description("错误字段"))
                                                                .addProperties("message", new Schema<>().type("string").description("错误信息"))
                                                        )
                                                        .description("错误信息")
                                        )
                                )
                        )
                );
    }

    private ApiResponse defineProblemResponse(String description) {
        return new ApiResponse()
                .description(description)
                .content(new Content()
                        .addMediaType(org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE, new MediaType()
                                .schema(new Schema<>()
                                        .type("object")
                                        .addProperties("status", new Schema<>().type("integer").description("错误状态"))
                                        .addProperties("title", new Schema<>().type("string").description("错误标题"))
                                        .addProperties("detail", new Schema<>().type("string").description("错误详情")
                                        )
                                )
                        )
                );
    }

    private Info defineInfo() {
        return new Info()
                .title("Agile 后台管理 API")
                .description("Agile 后台管理 API")
                .contact(new Contact().name("Huijie Wei").email("huijiewei@outlook.com"))
                .license(new License().name("Apache 2.0").url("http://www.apache.org/licenses/LICENSE-2.0.html"))
                .version("v1");
    }
}
