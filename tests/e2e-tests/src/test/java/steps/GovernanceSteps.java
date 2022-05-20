package steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.ScenarioContext;
import utils.datageneration.*;

import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;

public class GovernanceSteps implements En {

  DataGenerationService dataGenerationService = new DataGenerationService();
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
              dataGenerationService.generateFeature(feature.get("name"), feature.get("condition"),
                  feature.get("values"))));
      scenarioContext.set("feature list", featureList);
    });
    And("Prepare policy steps with predefined feature list", (DataTable dataTable) -> {
      Policy policy = (Policy) scenarioContext.get("policy");
      List<PolicyStep> policySteps = new java.util.ArrayList<>(Collections.emptyList());

      dataTable.asMaps().forEach(step -> {
        List<Feature> featuresList = (List<Feature>) scenarioContext.get("feature list");
        policySteps.add(
            dataGenerationService.generatePolicyStep(step.get("name"), step.get("solution"),
                featuresList));
      });

      policy.setSteps(policySteps);
      policy.setStepAdditionPayloads(
          new PolicyGenerationService().generatePayloadsForStepsAddition(policySteps));
    });
    And("Create empty policy with name {string}", (String value) -> {
      Policy policy =
          dataGenerationService.generatePolicyWithSteps(value, "DRAFT", Collections.emptyList());
      scenarioContext.set("policy", policy);

      Response response = given()
          .body(policy.getCreationPayload())
          .contentType("application/json")
          .when()
          .post("/rest/governance/api/v1/policies");

      response.then().statusCode(anyOf(is(202), is(201)));
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
              .put(String.format("/rest/governance/api/v1/steps/%s/logic", step.getId()))
              .then()
              .statusCode(202));
    });
    And("Policy is created", () -> {
      Policy policy = (Policy) scenarioContext.get("policy");
      when()
          .get(String.format("/rest/governance/api/v1/policies/%s", policy.getUuid()))
          .then()
          .statusCode(200);
    });
  }
}
