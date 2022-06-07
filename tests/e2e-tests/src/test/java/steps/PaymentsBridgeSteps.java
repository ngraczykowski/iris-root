/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package steps;

import io.cucumber.java8.En;
import org.awaitility.Awaitility;
import utils.datageneration.paymentsbridge.PaymentsBridgeAlert;
import utils.datageneration.paymentsbridge.PbAlertsGeneratingService;

import java.time.Duration;
import java.time.Instant;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;
import static steps.Hooks.scenarioContext;

public class PaymentsBridgeSteps implements En {

  private PbAlertsGeneratingService pbAlertsGeneratingService = new PbAlertsGeneratingService();

  public PaymentsBridgeSteps() {
    And(
        "Send alert on solving",
        () -> {
          PaymentsBridgeAlert oneHitAlert = pbAlertsGeneratingService.generateAlertWithHit();
          scenarioContext.set("alert", oneHitAlert);

          given()
              .when()
              .body(oneHitAlert.getPayload())
              .post("rest/pb/alert")
              .then()
              .statusCode(200)
              .body("Body.msg_Acknowledgement.faultcode", is("0"))
              .body("Body.msg_Acknowledgement.faultstring", is("OK"));

          // TODO(pputerla): implement callback verification; in meantime - do a mocked sleep :)
          var start = Instant.now();
          Awaitility.await()
              .atMost(Duration.ofSeconds(20))
              .pollInterval(Duration.ofSeconds(1))
              .until(
                  () ->
                      Duration.ofSeconds(15)
                          .minus(Duration.between(start, Instant.now()))
                          .isNegative());
        });
  }
}
