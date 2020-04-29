package com.silenteight.sens.webapp.common.testing.rest;

import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest.TestRestConfiguration;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRoleExtension;

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
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@WebAppConfiguration
@SpringBootTest(classes = { TestRestConfiguration.class })
@ExtendWith({ SpringExtension.class })
@TestPropertySource(properties = { "spring.cloud.consul.enabled=false" })
public abstract class BaseRestControllerTest {

  @Autowired
  private WebApplicationContext context;

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
        .accept(JSON)
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

  public static <T> ValidatableMockMvcResponse put(String mapping, T body) {
    return toValidatableResponse(asyncSender(body).put(withRoot(mapping)));
  }

  public static ValidatableMockMvcResponse patch(String mapping) {
    return toValidatableResponse(asyncSender().patch(withRoot(mapping)));
  }

  private static <T> MockMvcRequestAsyncSender asyncSender(T body) {
    return given()
        .accept(JSON)
        .contentType(JSON)
        .body(body)
        .when();
  }

  public static <T> ValidatableMockMvcResponse patch(String mapping, T body) {
    return toValidatableResponse(asyncSender(body).patch(withRoot(mapping)));
  }

  @Configuration
  @EnableWebMvc
  @EnableWebSecurity
  @EnableGlobalMethodSecurity(prePostEnabled = true)
  static class TestRestConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.csrf().disable();
    }
  }
}
