package com.silenteight.adjudication.engine.app;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

class WhitePageTest extends AdjudicationEngineAnalysisIntegrationTest {

  @LocalServerPort
  private int localServerPort;

  @Autowired
  private WebTestClient webTestClient;

  @Test
  @DisplayName("White page should not be expose")
  public void whitePageShouldNotBeExpose() {

    webTestClient
        .get()
        .uri(String.format("http://localhost:%s/eaaaaa24800", this.localServerPort))
        .exchange()
        .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
        .expectBody(String.class)
        .isEqualTo(null);

  }
}
