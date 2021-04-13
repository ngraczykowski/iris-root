package com.silenteight.hsbc.bridge.http.security;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = {
        SecurityIntegrationTest.TestConfig.class,
        SecurityIntegrationTest.ProtectedController.class
    },
    properties = {
        "logging.level.org.springframework.security=DEBUG",
        "silenteight.bridge.security=true"
    }
)
@AutoConfigureMockMvc
@AutoConfigureWebMvc
class SecurityIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void shouldAuthenticateAndGetSuccess() throws Exception {
    var correctToken = Jwts.builder().setExpiration(Date.from(Instant.now().plusSeconds(300)))
        .compact();

    mockMvc.perform(get("/test/auth").header("Authorization", "Bearer " + correctToken))
        .andExpect(status().is2xxSuccessful())
        .andExpect(MockMvcResultMatchers.content().string("success"));
  }

  @Configuration
  @ComponentScan(basePackageClasses = {
      SecurityModule.class
  })
  static class TestConfig {
  }

  @RestController
  @RequestMapping("/test")
  static class ProtectedController {

    @GetMapping(value = "/auth")
    ResponseEntity<String> testEndpoint() {
      return ResponseEntity.ok("success");
    }
  }
}
