package steps;

import io.cucumber.java.en.And;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.Hooks;
import utils.ScenarioContext;
import utils.datageneration.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class GovernanceSteps {

  DataGenerationService dataGenerationService = new DataGenerationService();
  ScenarioContext scenarioContext = Hooks.scenarioContext;

  @And("Policies endpoint responses with status code 200")
  public void datasetsEndpointIsAlive() {
    when()
        .get("rest/governance/api/v1/policies")
        .then()
        .statusCode(200);
  }

  @And("Prepare feature list for step")
  public void prepareFeatureListForStep(List<Map<String, String>> featureValues) {
    List<Feature> featureList = new java.util.ArrayList<>(Collections.emptyList());

    featureValues.forEach(feature -> featureList.add(
        dataGenerationService.generateFeature(feature.get("name"), feature.get("condition"),
            feature.get("values"))));
    scenarioContext.set("feature list", featureList);
  }

  @And("Prepare policy steps with predefined feature list")
  public void preparePolicyStepsAndFeatures(List<Map<String, String>> stepValues) {
    Policy policy = (Policy) scenarioContext.get("policy");
    List<PolicyStep> policySteps = new java.util.ArrayList<>(Collections.emptyList());

    stepValues.forEach(step -> {
      List<Feature> featuresList = (List<Feature>) scenarioContext.get("feature list");
      policySteps.add(
          dataGenerationService.generatePolicyStep(step.get("name"), step.get("solution"),
              featuresList));
    });

    policy.setSteps(policySteps);
    policy.setStepAdditionPayloads(
        new PolicyGenerationService().generatePayloadsForStepsAddition(policySteps));
  }

  @And("Create empty policy with name {string}")
  public void createPolicyWithName(String value) {
    Policy policy =
        dataGenerationService.generatePolicyWithSteps(value, "DRAFT", Collections.emptyList());
    scenarioContext.set("policy", policy);

    Response response = given()
        .body(policy.getCreationPayload())
        .contentType("application/json")
        .when()
        .post("/rest/governance/api/v1/policies");

    response
        .then()
        .statusCode(202);
  }

  @And("Add prepared steps to policy")
  public void addStepsToPolicy() {
    Policy policy = (Policy) scenarioContext.get("policy");

    policy.getStepAdditionPayloads().forEach(payload -> given()
        .body(payload)
        .contentType(ContentType.JSON)
        .when()
        .post(String.format("/rest/governance/api/v1/policies/%s/steps", policy.getUuid()))
        .then()
        .statusCode(204));
  }

  @And("Add prepared features to steps")
  public void addFeaturesToStep() {
    Policy policy = (Policy) scenarioContext.get("policy");

    policy.getSteps().forEach(step -> given()
        .body(step.getTemplatedFeatureList())
        .contentType(ContentType.JSON)
        .when()
        .put(String.format("/rest/governance/api/v1/steps/%s/logic", step.getId()))
        .then()
        .statusCode(202));
  }

  @And("Policy is created")
  public void policyIsCreated() {
    Policy policy = (Policy) scenarioContext.get("policy");
    when()
        .get(String.format(
            "/rest/governance/api/v1/policies/%s",
            policy.getUuid()))
        .then()
        .statusCode(200);
  }
}
