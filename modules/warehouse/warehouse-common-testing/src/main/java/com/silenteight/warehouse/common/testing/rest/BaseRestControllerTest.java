package com.silenteight.warehouse.common.testing.rest;

import com.silenteight.sep.auth.authorization.AuthorizationModule;
import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest.TestRestConfiguration;
import com.silenteight.warehouse.common.testing.rest.testwithrole.TestWithRoleExtension;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.module.mockmvc.response.ValidatableMockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestAsyncSender;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Map;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@WebAppConfiguration
@SpringBootTest(classes = { TestRestConfiguration.class })
@ExtendWith({ SpringExtension.class })
@TestPropertySource(properties = { "spring.config.location = classpath:application-test.yml" })
public abstract class BaseRestControllerTest {

  @Autowired
  private WebApplicationContext context;

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

  private static MockMvcRequestAsyncSender asyncSender() {
    return given()
        .config(RestAssuredMockMvc.config()
            .encoderConfig(RestAssuredMockMvc.config()
                .getEncoderConfig()
                .appendDefaultContentCharsetToContentTypeIfUndefined(false)))
        .accept(JSON)
        .when();
  }

  private static MockMvcRequestAsyncSender asyncSender(Map<String, ?> headers) {
    return given()
        .config(RestAssuredMockMvc.config()
            .encoderConfig(RestAssuredMockMvc.config()
                .getEncoderConfig()
                .appendDefaultContentCharsetToContentTypeIfUndefined(false)))
        .accept(JSON)
        .headers(headers)
        .when();
  }

  private static ValidatableMockMvcResponse toValidatableResponse(MockMvcResponse response) {
    return response
        .then()
        .log()
        .ifValidationFails();
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
        .config(RestAssuredMockMvc.config()
            .encoderConfig(RestAssuredMockMvc.config()
                .getEncoderConfig()
                .appendDefaultContentCharsetToContentTypeIfUndefined(false)))
        .accept(JSON)
        .contentType(JSON)
        .body(body)
        .when();
  }

  private static <T> MockMvcRequestAsyncSender asyncSender(T body, Map<String, ?> headers) {
    return given()
        .config(RestAssuredMockMvc.config()
            .encoderConfig(RestAssuredMockMvc.config()
                .getEncoderConfig()
                .appendDefaultContentCharsetToContentTypeIfUndefined(false)))
        .accept(JSON)
        .contentType(JSON)
        .body(body)
        .headers(headers)
        .when();
  }

  public static <T> ValidatableMockMvcResponse patch(String mapping, T body) {
    return toValidatableResponse(asyncSender(body).patch(withRoot(mapping)));
  }

  @Configuration
  @ComponentScan(basePackageClasses = { AuthorizationModule.class })
  @EnableWebMvc
  @EnableWebSecurity
  @EnableSpringDataWebSupport
  static class TestRestConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.csrf().disable();
    }
  }
}
