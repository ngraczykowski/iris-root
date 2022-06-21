package com.silenteight.sens.governance.common.testing.rest;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest.TestRestConfiguration;
import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest.WebConfig;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRoleExtension;
import com.silenteight.sep.auth.authorization.AuthorizationModule;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.module.mockmvc.response.ValidatableMockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestAsyncSender;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.util.List;
import java.util.Map;

import static com.silenteight.sep.auth.authentication.RestConstants.ROOT;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@WebAppConfiguration
@SpringBootTest(classes = {WebConfig.class, TestRestConfiguration.class})
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
public abstract class BaseRestControllerTest {

  private static final String FILE_PARAMETER = "file";

  @Autowired private WebApplicationContext context;

  protected static final String USERNAME = "username";

  @RegisterExtension
  public static TestWithRoleExtension testWithRoleExtension =
      new TestWithRoleExtension(new NewContextRoleSetter());

  @BeforeEach
  void setUp() {
    RestAssuredMockMvc.standaloneSetup(
        MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()));
  }

  public static ValidatableMockMvcResponse get(String mapping) {
    return toValidatableResponse(asyncSender().get(withRoot(mapping)));
  }

  public static <T> ValidatableMockMvcResponse get(String mapping, T body) {
    return toValidatableResponse(asyncSender(body).get(withRoot(mapping)));
  }

  private static MockMvcRequestAsyncSender asyncSender() {
    return given()
        .config(
            RestAssuredMockMvc.config()
                .encoderConfig(
                    RestAssuredMockMvc.config()
                        .getEncoderConfig()
                        .appendDefaultContentCharsetToContentTypeIfUndefined(false)))
        .accept(JSON)
        .when();
  }

  private static MockMvcRequestAsyncSender asyncSender(Map<String, ?> headers) {
    return given()
        .config(
            RestAssuredMockMvc.config()
                .encoderConfig(
                    RestAssuredMockMvc.config()
                        .getEncoderConfig()
                        .appendDefaultContentCharsetToContentTypeIfUndefined(false)))
        .accept(JSON)
        .headers(headers)
        .when();
  }

  private static ValidatableMockMvcResponse toValidatableResponse(MockMvcResponse response) {
    return response.then().log().ifValidationFails();
  }

  @NotNull
  private static String withRoot(String mapping) {
    return ROOT + mapping;
  }

  public static ValidatableMockMvcResponse post(String mapping) {
    return toValidatableResponse(asyncSender().post(withRoot(mapping)));
  }

  public static <T> ValidatableMockMvcResponse post(String mapping, T body) {
    return toValidatableResponse(asyncSender(body).post(withRoot(mapping)));
  }

  public static <T> ValidatableMockMvcResponse post(
      String mapping, T body, Map<String, ?> headers) {
    return toValidatableResponse(asyncSender(body, headers).post(withRoot(mapping)));
  }

  public static ValidatableMockMvcResponse post(String mapping, File file) {
    return toValidatableResponse(asyncSender(file).post(withRoot(mapping)));
  }

  public static ValidatableMockMvcResponse postAsync(String mapping, File file) {
    return toValidatableResponse(asyncSender(file).async().post(withRoot(mapping)));
  }

  public static <T> ValidatableMockMvcResponse put(String mapping, T body) {
    return toValidatableResponse(asyncSender(body).put(withRoot(mapping)));
  }

  public static ValidatableMockMvcResponse patch(String mapping) {
    return toValidatableResponse(asyncSender().patch(withRoot(mapping)));
  }

  public static ValidatableMockMvcResponse delete(String mapping) {
    return toValidatableResponse(asyncSender().delete(withRoot(mapping)));
  }

  public static ValidatableMockMvcResponse patch(String mapping, Map<String, ?> headers) {
    return toValidatableResponse(asyncSender(headers).patch(withRoot(mapping)));
  }

  public static <T> ValidatableMockMvcResponse patch(
      String mapping, T body, Map<String, ?> headers) {
    return toValidatableResponse(asyncSender(body, headers).patch(withRoot(mapping)));
  }

  private static <T> MockMvcRequestAsyncSender asyncSender(T body) {
    return given()
        .config(
            RestAssuredMockMvc.config()
                .encoderConfig(
                    RestAssuredMockMvc.config()
                        .getEncoderConfig()
                        .appendDefaultContentCharsetToContentTypeIfUndefined(false)))
        .accept(JSON)
        .contentType(JSON)
        .body(body)
        .when();
  }

  private static <T> MockMvcRequestAsyncSender asyncSender(T body, Map<String, ?> headers) {
    return given()
        .config(
            RestAssuredMockMvc.config()
                .encoderConfig(
                    RestAssuredMockMvc.config()
                        .getEncoderConfig()
                        .appendDefaultContentCharsetToContentTypeIfUndefined(false)))
        .accept(JSON)
        .contentType(JSON)
        .body(body)
        .headers(headers)
        .when();
  }

  private static MockMvcRequestAsyncSender asyncSender(File file) {
    return given()
        .config(
            RestAssuredMockMvc.config()
                .encoderConfig(
                    RestAssuredMockMvc.config()
                        .getEncoderConfig()
                        .appendDefaultContentCharsetToContentTypeIfUndefined(false)))
        .accept(JSON)
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        .multiPart(FILE_PARAMETER, file)
        .when();
  }

  public static <T> ValidatableMockMvcResponse patch(String mapping, T body) {
    return toValidatableResponse(asyncSender(body).patch(withRoot(mapping)));
  }

  @Configuration
  @ComponentScan(basePackageClasses = AuthorizationModule.class)
  @EnableWebMvc
  @EnableWebSecurity
  @EnableSpringDataWebSupport
  public static class TestRestConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.csrf().disable();
    }
  }

  @Configuration
  static class WebConfig implements WebMvcConfigurer {

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
      return new MethodValidationPostProcessor();
    }

    /**
     * TODO(dsniezek): this configuration is a bad thing, as we force test to behave in expected
     * way. This configuration should be provided by the application(implicite) to test correctness
     * of the application behaviour
     *
     * <p>Workaround: do not serialize dates as floats see:
     * https://github.com/rest-assured/rest-assured/issues/1116
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
      ObjectMapper mapper =
          new ObjectMapper()
              .registerModule(new JavaTimeModule())
              // Support for Streams serialization etc...
              .registerModule(new Jdk8Module())
              .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
              .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      // Support for file download
      messageConverters.add(new ByteArrayHttpMessageConverter());
      // Support for @RequestBody String(Order matters)
      messageConverters.add(new StringHttpMessageConverter());
      messageConverters.add(new MappingJackson2HttpMessageConverter(mapper));
    }
  }
}
