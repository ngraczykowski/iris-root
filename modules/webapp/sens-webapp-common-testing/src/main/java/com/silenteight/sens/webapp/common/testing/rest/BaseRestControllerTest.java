package com.silenteight.sens.webapp.common.testing.rest;

import com.silenteight.sens.webapp.common.rest.RestConstants;
import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest.TestRestConfiguration;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.ValidatableMockMvcResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

@WebAppConfiguration
@SpringBootTest(classes = TestRestConfiguration.class)
@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = { "spring.cloud.consul.enabled=false" })
public abstract class BaseRestControllerTest {

  @Autowired
  private WebApplicationContext context;

  @BeforeEach
  void setUp() {
    RestAssuredMockMvc.standaloneSetup(MockMvcBuilders.webAppContextSetup(context));
  }

  public ValidatableMockMvcResponse get(String mapping) {
    return given()
        .accept(ContentType.JSON)
        .when()
        .get(RestConstants.ROOT + mapping)
        .then()
        .contentType(ContentType.JSON)
        .log()
        .ifValidationFails();
  }

  public <T> ValidatableMockMvcResponse post(String mapping, T body) {
    return given()
        .contentType(ContentType.JSON)
        .body(body)
        .when()
        .post(RestConstants.ROOT + mapping)
        .then()
        .log()
        .ifValidationFails();
  }


  @Configuration
  @EnableWebMvc
  static class TestRestConfiguration {
  }
}
