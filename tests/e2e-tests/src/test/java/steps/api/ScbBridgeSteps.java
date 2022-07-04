/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package steps.api;

import io.cucumber.java8.En;
import utils.datageneration.namescreening.scbbridge.GnsRtRequest;
import utils.datageneration.namescreening.scbbridge.GnsRtRequestGenerationService;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class ScbBridgeSteps implements En {

  GnsRtRequestGenerationService gnsRtRequestGenerationService = new GnsRtRequestGenerationService();

  public ScbBridgeSteps() {
    And(
        "Send request to get recommendation with recommendation type {string}",
        (String type) -> {
            GnsRtRequest gnsrtRequest = gnsRtRequestGenerationService.generate(type);

            given()
                .auth()
                .none()
                .body(gnsrtRequest.getPayload())
                .when()
                .post("/rest/scb-bridge/v1/gnsrt/recommendation")
                .then()
                .statusCode(200)
                .body("silent8Response.alerts[0].recommendation", is(type));
        });
  }
}
