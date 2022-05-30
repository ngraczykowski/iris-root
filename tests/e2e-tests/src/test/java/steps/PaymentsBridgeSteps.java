/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package steps;

import io.cucumber.java8.En;
import utils.ScenarioContext;
import utils.datageneration.paymentsbridge.PaymentsBridgeAlert;
import utils.datageneration.paymentsbridge.PbAlertsGeneratingService;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class PaymentsBridgeSteps implements En {

  private PbAlertsGeneratingService pbAlertsGeneratingService = new PbAlertsGeneratingService();
  ScenarioContext scenarioContext = Hooks.scenarioContext;

  public PaymentsBridgeSteps() {
    And("Send alert on solving", () -> {
      PaymentsBridgeAlert oneHitAlert = pbAlertsGeneratingService.generateAlertWithHit();
      scenarioContext.set("alert", oneHitAlert);

      given()
          .when()
          .contentType("application/json")
          .body(oneHitAlert.getPayload())
          .post("rest/pb/alert")
          .then()
          .statusCode(200)
          .body("Body.msg_Acknowledgement.faultcode", is("0"))
          .body("Body.msg_Acknowledgement.faultstring", is("OK"));
    });
  }
}
