package steps;

import lombok.SneakyThrows;

import io.cucumber.core.backend.TestCaseState;
import io.cucumber.core.gherkin.Pickle;
import io.cucumber.core.gherkin.Step;
import io.cucumber.java8.En;
import io.cucumber.java8.Scenario;
import io.cucumber.plugin.event.TestCase;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HeaderConfig;
import io.restassured.config.RestAssuredConfig;
import utils.CustomLogFilter;
import utils.ScenarioContext;

import java.lang.reflect.Field;
import java.util.List;

import static utils.AuthUtils.getAuthTokenHeader;

public class Hooks implements En {

  public static ScenarioContext scenarioContext = new ScenarioContext();
  static CustomLogFilter customLogFilter = new CustomLogFilter();
  int stepNo = 0;
  String scenarioName = "";

  public Hooks() {
    Before(0, () -> {
      scenarioContext.clearScenarioContext();
      RestAssured.baseURI = System.getProperty("test.url");
      RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
      RestAssured.requestSpecification = new RequestSpecBuilder()
          .setConfig(RestAssuredConfig
              .config()
              .headerConfig(HeaderConfig.headerConfig().overwriteHeadersWithName("Authorization", "Content-Type")))
          .addHeader("Authorization", getAuthTokenHeader())
          .build()
          .given()
          .filter(customLogFilter)
          .log()
          .all();
    });
    AfterStep(0, (Scenario scenario) -> {
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
            "\n" + "API Request: " + customLogFilter.getRequestBuilder() + "\n" + "API Response: "
                + customLogFilter.getResponseBuilder(), "application/json",
            "API communication for: " + currentStepName);
      }
      stepNo++;
    });
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
