package steps;

import io.cucumber.core.backend.TestCaseState;
import io.cucumber.core.gherkin.Pickle;
import io.cucumber.core.gherkin.Step;
import io.cucumber.java.AfterStep;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import io.cucumber.plugin.event.TestCase;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;

import lombok.SneakyThrows;

import utils.AuthUtils;
import utils.CustomLogFilter;
import utils.ScenarioContext;

import java.lang.reflect.Field;
import java.util.List;

public class Hooks {

  public static ScenarioContext scenarioContext = new ScenarioContext();
  static CustomLogFilter customLogFilter = new CustomLogFilter();
  int stepNo = 0;
  String scenarioName = "";

  @BeforeAll
  public static void setupRestAssured() {
    RestAssured.baseURI = "https://bravo.dev.silenteight.com/";
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    RestAssured.requestSpecification = new RequestSpecBuilder()
        .build()
        .header("Authorization", "Bearer " + new AuthUtils().getAuthToken())
        .given()
        .filter(customLogFilter)
        .log()
        .all();
  }

  @AfterStep
  public void afterStep(Scenario scenario) {
    /*
      Cucumber do not provide steps info, so We had to create this mechanism
      We check scenario name to select index for desired step
     */
    String currentScenarioName = scenario.getName();
    if (!scenarioName.equals(currentScenarioName)) {
      scenarioName = currentScenarioName;
      stepNo = 0;
    }
    List<Step> stepList = getStepListFromScenario(scenario);
    String currentStepName = stepList.get(stepNo).getText();

    if (!currentStepName.startsWith("Prepare")) {
      scenario.attach(
          "\n" + "API Request: " + customLogFilter.getRequestBuilder()
              + "\n" + "API Response: " + customLogFilter.getResponseBuilder(), "application/json",
          "API communication for: " + currentStepName);
    }
    stepNo++;
  }

  @SneakyThrows
  private List<Step> getStepListFromScenario(Scenario scenario) {
    /*
      Hack to get cucumber steps name
     */
    Field f = scenario.getClass().getDeclaredField("delegate");
    f.setAccessible(true);
    TestCaseState testCaseState = (TestCaseState) f.get(scenario);
    Field x = testCaseState.getClass().getDeclaredField("testCase");
    x.setAccessible(true);
    TestCase testCase = (TestCase) x.get(testCaseState);
    testCase.getTestSteps();
    Field y = testCase.getClass().getDeclaredField("pickle");
    y.setAccessible(true);
    Pickle pickle = (Pickle) y.get(testCase);

    return pickle.getSteps();
  }
}
