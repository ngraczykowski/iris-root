package steps.api;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import utils.datageneration.governance.*;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static steps.Hooks.scenarioContext;
import static utils.AuthUtils.getAuthTokenHeaderForUser;
import static utils.datageneration.governance.GovernanceGenerationService.*;

public class GovernanceSteps implements En {

  private static final String GOV_API_BASE_PATH =
      System.getProperty("test.govApiBasePath", "/rest/governance/api/v1");

  public GovernanceSteps() {
    And(
        "Policies endpoint responses with status code {int}",
        (Integer code) -> when().get(GOV_API_BASE_PATH + "/policies").then().statusCode(code));

    And(
        "Create empty policy with name {string}",
        (String value) -> {
          CreatePolicy policy = createPolicy(value);
          scenarioContext.set("policy", policy);

          given()
              .body(policy)
              .when()
              .post(GOV_API_BASE_PATH + "/policies")
              .then()
              .statusCode(anyOf(is(202), is(201)));
        });

    And(
        "Add steps to recently created policy",
        (DataTable dataTable) -> {
          CreatePolicy policy = (CreatePolicy) scenarioContext.get("policy");
          List<CreatePolicyStep> policyStepList = new ArrayList<>();

          dataTable
              .asMaps()
              .forEach(
                  step -> {
                    CreatePolicyStep policyStep =
                        createPolicyStep(step.get("name"), step.get("solution"));
                    given()
                        .body(policyStep)
                        .when()
                        .post(GOV_API_BASE_PATH + format("/policies/%s/steps", policy.getId()))
                        .then()
                        .statusCode(204);

                    policyStepList.add(policyStep);
                    scenarioContext.set("policySteps", policyStepList);
                  });
        });

    // TODO(kkicza): Make it able to process more than 1 feature per step
    And(
        "Add features to recently created steps",
        (DataTable dataTable) -> {
          @SuppressWarnings("unchecked")
          List<CreatePolicyStep> policySteps =
              (List<CreatePolicyStep>) scenarioContext.get("policySteps");

          dataTable
              .asMaps()
              .forEach(
                  feature -> {
                    CreateFeatureLogic featureLogic =
                        createFeatureLogic(
                            feature.get("name"), feature.get("condition"), feature.get("values"));
                    given()
                        .body(featureLogic)
                        .when()
                        .put(
                            GOV_API_BASE_PATH
                                + format("/steps/%s/logic", policySteps.get(0).getId()))
                        .then()
                        .statusCode(202);
                  });
        });

    And(
        "Mark created policy as ready",
        () -> {
          CreatePolicy policy = (CreatePolicy) scenarioContext.get("policy");
          given()
              .body("{\"state\":\"SAVED\"}")
              .when()
              .patch(GOV_API_BASE_PATH + format("/policies/%s", policy.getId()))
              .then()
              .statusCode(200);
        });

    And(
        "Policy is created",
        () -> {
          CreatePolicy policy = (CreatePolicy) scenarioContext.get("policy");
          when()
              .get(GOV_API_BASE_PATH + format("/policies/%s", policy.getId()))
              .then()
              .statusCode(200);
        });

    And(
        "Create solving model for created policy",
        () -> {
          CreatePolicy policy = (CreatePolicy) scenarioContext.get("policy");
          CreateSolvingModel solvingModel = createSolvingModel(policy.getId());
          scenarioContext.set("solvingModel", solvingModel);

          given()
              .body(solvingModel)
              .when()
              .post(GOV_API_BASE_PATH + "/solvingModels")
              .then()
              .statusCode(202);
        });

    And(
        "Create policy state change request",
        () -> {
          CreateSolvingModel solvingModel =
              (CreateSolvingModel) scenarioContext.get("solvingModel");
          ActivateSolvingModel activationData = activateSolvingModel(solvingModel.getId());

          scenarioContext.set("activationData", activationData);

          given()
              .body(activationData)
              .when()
              .post(GOV_API_BASE_PATH + "/changeRequests")
              .then()
              .statusCode(202);
        });

    And(
        "Activate solving model as user {string}",
        (String label) -> {
          ActivateSolvingModel activationData =
              (ActivateSolvingModel) scenarioContext.get("activationData");
          var user = scenarioContext.getUser(label);

          given()
              .body("{\"approverComment\":\"Lorem ipsum dolor sit amet\"}")
              .auth()
              .oauth2(getAuthTokenHeaderForUser(user))
              .when()
              .post(
                  GOV_API_BASE_PATH + format("/changeRequests/%s:approve", activationData.getId()))
              .then()
              .statusCode(204);
        });
    And(
        "Frontend Configuration API respond with 200 status code",
        GovernanceSteps::getFrontendConfiguration);
  }

  private static void getFrontendConfiguration() {
    given()
        .when()
        .get(GOV_API_BASE_PATH + "/frontend/configuration")
        .then()
        .statusCode(200)
        .body("serverApiUrl", is("/rest/webapp/api"))
        .body("governanceApiUrl", is("/rest/governance/api/v1"))
        .body("simulatorUrl", is("/rest/simulator/api/v1"));
  }
}
