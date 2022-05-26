/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package steps;

import io.cucumber.java8.En;
import utils.datageneration.paymentsbridge.AlertsGeneratingService;

import static io.restassured.RestAssured.given;

public class PaymentsBridgeSteps implements En {

  private AlertsGeneratingService alertsGeneratingService = new AlertsGeneratingService();

  public PaymentsBridgeSteps() {
    And("Send alert on solving", () -> {
      String oneHitAlert = alertsGeneratingService.generateAlertWithHits(1);

      given()
          .when()
          .contentType("application/json")
          .body(oneHitAlert)
          .post("rest/pb/alert")
          .then()
          .statusCode(200);
    });
  }
}
