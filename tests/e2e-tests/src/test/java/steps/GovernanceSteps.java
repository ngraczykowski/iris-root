package steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.http.ContentType;
import utils.ScenarioContext;
import utils.datageneration.governance.*;

import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;
import static utils.AuthUtils.getAuthTokenHeaderForAdmin;

public class GovernanceSteps implements En {

  GovernanceGenerationService governanceGenerationService = new GovernanceGenerationService();
  ScenarioContext scenarioContext = Hooks.scenarioContext;

  public GovernanceSteps() {
    And("Policies endpoint responses with status code 200", () -> {
      when().get("rest/governance/api/v1/policies").then().statusCode(200);
    });

    And("Prepare feature list for step", (DataTable dataTable) -> {
      List<Feature> featureList = new java.util.ArrayList<>(Collections.emptyList());

      dataTable
          .asMaps()
          .forEach(feature -> featureList.add(
              governanceGenerationService.generateFeature(
                  feature.get("name"),
                  feature.get("condition"),
                  feature.get("values"))));
      scenarioContext.set("feature list", featureList);
    });

    And("Prepare policy steps with predefined feature list", (DataTable dataTable) -> {
      Policy policy = (Policy) scenarioContext.get("policy");
      List<PolicyStep> policySteps = new java.util.ArrayList<>(Collections.emptyList());

      dataTable.asMaps().forEach(step -> {
        @SuppressWarnings("unchecked")
        List<Feature> featuresList = (List<Feature>) scenarioContext.get("feature list");
        policySteps.add(
            governanceGenerationService.generatePolicyStep(step.get("name"), step.get("solution"),
                featuresList));
      });

      policy.setSteps(policySteps);
      policy.setStepAdditionPayloads(
          new GovernanceGenerationService().generatePayloadsForStepsAddition(policySteps));
    });

    And("Create empty policy with name {string}", (String value) -> {
      Policy policy =
          governanceGenerationService.generatePolicy(value, "DRAFT", Collections.emptyList());
      scenarioContext.set("policy", policy);

      given()
          .body(policy.getCreationPayload())
          .contentType("application/json")
          .when()
          .post("/rest/governance/api/v1/policies")
          .then().statusCode(anyOf(is(202), is(201)));
    });

    And("Add prepared steps to policy", () -> {
      Policy policy = (Policy) scenarioContext.get("policy");

      policy
          .getStepAdditionPayloads()
          .forEach(payload -> given()
              .body(payload)
              .contentType(ContentType.JSON)
              .when()
              .post(String.format("/rest/governance/api/v1/policies/%s/steps", policy.getUuid()))
              .then()
              .statusCode(204));
    });

    And("Add prepared features to steps", () -> {
      Policy policy = (Policy) scenarioContext.get("policy");

      policy
          .getSteps()
          .forEach(step -> given()
              .body(step.getTemplatedFeatureList())
              .contentType(ContentType.JSON)
              .when()
              .put(String.format("/rest/governance/api/v1/steps/%s/logic", step.getUuid()))
              .then()
              .statusCode(202));
    });

    And("Mark created policy as ready", () -> {
      Policy policy = (Policy) scenarioContext.get("policy");
      given()
          .body("{\"state\":\"SAVED\"}")
          .contentType("application/json")
          .when()
          .patch(String.format("/rest/governance/api/v1/policies/%s", policy.getUuid()))
          .then()
          .statusCode(200);
    });

    And("Policy is created", () -> {
      Policy policy = (Policy) scenarioContext.get("policy");
      when()
          .get(String.format("/rest/governance/api/v1/policies/%s", policy.getUuid()))
          .then()
          .statusCode(200);
    });

    And("Create solving model for created policy", () -> {
      Policy policy = (Policy) scenarioContext.get("policy");
      SolvingModel solvingModel = governanceGenerationService.generateSolvingModel(policy.getUuid());
      scenarioContext.set("solvingModel", solvingModel);

      given()
          .body(solvingModel.getCreationPayload())
          .contentType("application/json")
          .when()
          .post("/rest/governance/api/v1/solvingModels")
          .then()
          .statusCode(202);
    });

    And("Create policy state change request", () -> {
      SolvingModel solvingModel = (SolvingModel) scenarioContext.get("solvingModel");

      given()
          .body(solvingModel.getActivationPayload())
          .contentType("application/json")
          .when()
          .post("/rest/governance/api/v1/changeRequests")
          .then()
          .statusCode(202);
    });

    And("Activate solving model as other user", () -> {
      SolvingModel solvingModel = (SolvingModel) scenarioContext.get("solvingModel");

      given()
          .body("{\"approverComment\":\"Lorem ipsum dolor sit amet\"}")
          .contentType("application/json")
          .header("Authorization", getAuthTokenHeaderForAdmin())
          .when()
          .post(String.format("/rest/governance/api/v1/changeRequests/%s:approve", solvingModel.getActivationUuid()))
          .then()
          .statusCode(204);
    });
  }
}
