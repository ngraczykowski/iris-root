package steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static utils.datageneration.governance.GovernanceGenerationService.*;

public class GovernanceSteps implements En {

  ScenarioContext scenarioContext = Hooks.scenarioContext;
  ObjectMapper objectMapper = new ObjectMapper();

  public GovernanceSteps() {
    And("Policies endpoint responses with status code 200", () -> {
      when().get("rest/governance/api/v1/policies").then().statusCode(200);
    });

    And("Create empty policy with name {string}", (String value) -> {
      CreatePolicy policy = createPolicy(value);
      scenarioContext.set("policy", policy);

      given()
          .body(objectMapper.writeValueAsString(policy))
          .contentType("application/json")
          .when()
          .post("/rest/governance/api/v1/policies")
          .then()
          .statusCode(anyOf(is(202), is(201)));
    });

    And("Add steps to recently created policy", (DataTable dataTable) -> {
      CreatePolicy policy = (CreatePolicy) scenarioContext.get("policy");
      List<CreatePolicyStep> policyStepList = new java.util.ArrayList<>(Collections.emptyList());

      dataTable.asMaps().forEach(step -> {
        CreatePolicyStep policyStep = createPolicyStep(step.get("name"), step.get("solution"));
        try {
          given()
              .body(objectMapper.writeValueAsString(policyStep))
              .contentType(ContentType.JSON)
              .when()
              .post(String.format("/rest/governance/api/v1/policies/%s/steps", policy.getId()))
              .then()
              .statusCode(204);

          policyStepList.add(policyStep);
        } catch (JsonProcessingException e) {
          throw new RuntimeException(e);
        }
        scenarioContext.set("policySteps", policyStepList);
      });
    });

    //TODO(kkicza): Make it able to process more than 1 feature per step
    And("Add features to recently created steps", (DataTable dataTable) -> {
      @SuppressWarnings("unchecked") List<CreatePolicyStep> policySteps =
          (List<CreatePolicyStep>) scenarioContext.get("policySteps");

      dataTable.asMaps().forEach(feature -> {
        CreateFeatureLogic featureLogic =
            createFeatureLogic(feature.get("name"), feature.get("condition"),
                feature.get("values"));

        try {
          given()
              .body(objectMapper.writeValueAsString(featureLogic))
              .contentType(ContentType.JSON)
              .when()
              .put(String.format(
                  "/rest/governance/api/v1/steps/%s/logic",
                  policySteps.get(0).getId()))
              .then()
              .statusCode(202);
        } catch (JsonProcessingException e) {
          throw new RuntimeException(e);
        }
      });
    });

    And("Mark created policy as ready", () -> {
      CreatePolicy policy = (CreatePolicy) scenarioContext.get("policy");
      given()
          .body("{\"state\":\"SAVED\"}")
          .contentType("application/json")
          .when()
          .patch(String.format("/rest/governance/api/v1/policies/%s", policy.getId()))
          .then()
          .statusCode(200);
    });

    And("Policy is created", () -> {
      CreatePolicy policy = (CreatePolicy) scenarioContext.get("policy");
      when()
          .get(String.format("/rest/governance/api/v1/policies/%s", policy.getId()))
          .then()
          .statusCode(200);
    });

    And("Create solving model for created policy", () -> {
      CreatePolicy policy = (CreatePolicy) scenarioContext.get("policy");
      CreateSolvingModel solvingModel = createSolvingModel(policy.getId());
      scenarioContext.set("solvingModel", solvingModel);

      given()
          .body(objectMapper.writeValueAsString(solvingModel))
          .contentType("application/json")
          .when()
          .post("/rest/governance/api/v1/solvingModels")
          .then()
          .statusCode(202);
    });

    And("Create policy state change request", () -> {
      CreateSolvingModel solvingModel = (CreateSolvingModel) scenarioContext.get("solvingModel");
      ActivateSolvingModel activationData = activateSolvingModel(solvingModel.getId());

      scenarioContext.set("activationData", activationData);

      given()
          .body(objectMapper.writeValueAsString(activationData))
          .contentType("application/json")
          .when()
          .post("/rest/governance/api/v1/changeRequests")
          .then()
          .statusCode(202);
    });

    And("Activate solving model as other user", () -> {
      ActivateSolvingModel activationData = (ActivateSolvingModel) scenarioContext.get("activationData");

      given()
          .body("{\"approverComment\":\"Lorem ipsum dolor sit amet\"}")
          .contentType("application/json")
          .header("Authorization", getAuthTokenHeaderForAdmin())
          .when()
          .post(String.format(
              "/rest/governance/api/v1/changeRequests/%s:approve",
              activationData.getId()))
          .then()
          .statusCode(204);
    });
  }
}
