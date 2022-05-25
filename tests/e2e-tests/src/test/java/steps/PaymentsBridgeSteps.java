/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package steps;

import io.cucumber.java8.En;

import java.io.InputStream;

import static io.restassured.RestAssured.given;

public class PaymentsBridgeSteps implements En {

  public PaymentsBridgeSteps() {
    And("Send alert on solving", () -> {
      InputStream requestBody =
          PaymentsBridgeSteps.class.getResourceAsStream("/paymentsBridgeAlerts/alert-1.json");

      given()
          .when()
          .contentType("application/json")
          .body(requestBody)
          .post("rest/pb/alert")
          .then()
          .statusCode(200);
    });
  }
}
