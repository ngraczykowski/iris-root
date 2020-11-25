package com.silenteight.serp.governance.app.rest;

import com.silenteight.serp.common.rest.RestConstants;
import com.silenteight.serp.governance.app.rest.BaseRestControllerTest.TestRestConfiguration;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.ValidatableMockMvcResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

// TODO(iwnek) Consider moving this class to serp-common-testing
@WebAppConfiguration
@SpringBootTest(classes = TestRestConfiguration.class)
@ExtendWith(SpringExtension.class)
abstract class BaseRestControllerTest {

  @Autowired
  private WebApplicationContext context;

  @BeforeEach
  void setUp() {
    RestAssuredMockMvc.standaloneSetup(MockMvcBuilders.webAppContextSetup(context));
  }

  ValidatableMockMvcResponse perform(String mapping) {
    return given()
        .accept(ContentType.JSON)
        .when()
        .get(RestConstants.ROOT + mapping)
        .then()
        .contentType(ContentType.JSON)
        .log().ifValidationFails();
  }

  // TODO(iwnek) Import RestErrorAutoConfiguration to make error mappings working in tests
  @Configuration
  @EnableWebMvc
  static class TestRestConfiguration {
  }
}
