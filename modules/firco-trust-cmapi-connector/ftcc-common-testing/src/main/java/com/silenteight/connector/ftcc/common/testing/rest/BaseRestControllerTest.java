package com.silenteight.connector.ftcc.common.testing.rest;

import com.silenteight.connector.ftcc.common.testing.rest.BaseRestControllerTest.TestRestConfiguration;
import com.silenteight.connector.ftcc.common.testing.rest.BaseRestControllerTest.WebConfig;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.module.mockmvc.response.ValidatableMockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestAsyncSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import static io.restassured.http.ContentType.JSON;

@WebAppConfiguration
@SpringBootTest(classes = { TestRestConfiguration.class, WebConfig.class })
@ExtendWith({ SpringExtension.class })
public class BaseRestControllerTest {

  @Autowired
  private WebApplicationContext context;

  @BeforeEach
  void setUp() {
    RestAssuredMockMvc.standaloneSetup(
        MockMvcBuilders.webAppContextSetup(context));
  }

  public static <T> ValidatableMockMvcResponse post(String mapping, T body) {
    return toValidatableResponse(asyncSender(body).post(mapping));
  }

  private static ValidatableMockMvcResponse toValidatableResponse(MockMvcResponse response) {
    return response
        .then()
        .log()
        .ifValidationFails();
  }

  private static <T> MockMvcRequestAsyncSender asyncSender(T body) {
    return RestAssuredMockMvc.given()
        .config(RestAssuredMockMvc.config()
            .encoderConfig(RestAssuredMockMvc.config()
                .getEncoderConfig()
                .appendDefaultContentCharsetToContentTypeIfUndefined(false)))
        .accept(JSON)
        .contentType(JSON)
        .body(body)
        .when();
  }

  @Configuration
  @EnableWebMvc
  static class TestRestConfiguration  {
  }

  /**
   * Workaround: do not serialize dates as floats
   * see: https://github.com/rest-assured/rest-assured/issues/1116
   */
  @Configuration
  static class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
      ObjectMapper mapper = new ObjectMapper()
          .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

      messageConverters.add(new MappingJackson2HttpMessageConverter(mapper));
    }
  }
}
