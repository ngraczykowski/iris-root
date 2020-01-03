package com.silenteight.sens.webapp.backend.rest;

import com.silenteight.sens.webapp.backend.RestConstants;
import com.silenteight.sens.webapp.backend.config.WebModule;
import com.silenteight.sens.webapp.backend.domain.decisiontree.DecisionTreeService;
import com.silenteight.sens.webapp.backend.rest.BaseRestControllerIT.TestRestConfiguration;
import com.silenteight.sens.webapp.common.database.DataSourceAutoConfiguration;
import com.silenteight.sens.webapp.common.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.sens.webapp.kernel.security.SensUserDetails;
import com.silenteight.sens.webapp.security.SecurityModule;
import com.silenteight.sens.webapp.security.context.UserContextRunner;
import com.silenteight.sens.webapp.users.UsersModule;
import com.silenteight.sens.webapp.users.user.User;
import com.silenteight.sens.webapp.users.user.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.ValidatableMockMvcResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Optional;
import java.util.Set;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

@WebAppConfiguration
@SpringBootTest(classes = TestRestConfiguration.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(initializers = { PostgresTestInitializer.class })
@ImportAutoConfiguration(DataSourceAutoConfiguration.class)
@TestPropertySource("classpath:data-test.properties")
abstract class BaseRestControllerIT {

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private TestDataFacade testDataFacade;

  @BeforeEach
  public void initUsers() {
    testDataFacade.initSensUsers();
    RestAssuredMockMvc.standaloneSetup(MockMvcBuilders.webAppContextSetup(context));
  }

  @AfterEach
  public void cleanupUsers() {
    testDataFacade.cleanUpSensUsers();
  }

  void executeAs(String username, Runnable function) {
    SensUserDetails userDetails =
        (SensUserDetails) userDetailsService.loadUserByUsername(username);
    UserContextRunner.runAs(userDetails, function);
  }

  ValidatableMockMvcResponse performGet(String mapping) {
    return given()
        .accept(ContentType.JSON)
        .when()
        .get(RestConstants.ROOT + mapping)
        .then()
        .contentType(ContentType.JSON)
        .log().ifValidationFails();
  }

  ValidatableMockMvcResponse performPost(String mapping, Object body) {
    return given()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(body)
        .when()
        .post(RestConstants.ROOT + mapping)
        .then()
        .log().ifValidationFails();
  }

  @Configuration
  @EnableAutoConfiguration
  @EnableWebMvc
  @ComponentScan(basePackageClasses = {
      SecurityModule.class,
      WebModule.class,
      UsersModule.class,
      TestDataFacade.class,
      UsersDataInitializer.class })
  @ContextConfiguration(classes = GlobalMethodSecurityConfiguration.class)
  static class TestRestConfiguration {

    @MockBean
    private DecisionTreeService decisionTreeService;

    @Bean
    ObjectMapper objectMapper() {
      return new ObjectMapper();
    }

    @Bean
    UserDetailsService userDetailsService(UserService service) {
      return username -> {
        final Optional<User> user = service.getUserByName(username);
        if (user.isEmpty())
          throw new UsernameNotFoundException("Cannot find user: " + username);
        return makeUserDetails(
            user.get().getId(), username, user.get().isSuperUser(), user.get().getAuthorities());
      };
    }

    private static SensUserDetails makeUserDetails(
        long id, String name, boolean superUser, Set<GrantedAuthority> authorities) {

      return SensUserDetails
          .builder()
          .userId(id)
          .username(name)
          .authorities(authorities)
          .password("dummy_password")
          .superUser(superUser)
          .build();
    }
  }
}
