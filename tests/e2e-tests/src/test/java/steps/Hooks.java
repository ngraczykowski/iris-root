package steps;

import lombok.SneakyThrows;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.core.backend.TestCaseState;
import io.cucumber.core.gherkin.Pickle;
import io.cucumber.core.gherkin.Step;
import io.cucumber.java8.En;
import io.cucumber.java8.Scenario;
import io.cucumber.plugin.event.TestCase;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HeaderConfig;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.StringUtils;
import utils.CustomAuthFilter;
import utils.CustomLogFilter;
import utils.ScenarioContext;
import utils.datageneration.webapp.User;

import java.lang.reflect.Field;
import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Hooks implements En {

  public static ScenarioContext scenarioContext = new ScenarioContext();
  public static final String STOMP_SESSION = "stompSession";

  public static final String BASE_URL = System.getProperty("test.url");
  private static final CustomLogFilter CUSTOM_LOG_FILTER = new CustomLogFilter();
  int stepNo = 0;
  String scenarioName = "";

  public Hooks() {
    Before(
        0,
        (Scenario scenario) -> {
          scenarioContext.clearScenarioContext();
          scenarioContext.setUser(
              "admin",
              User.builder()
                  .userName(System.getProperty("test.admin.username", "iris"))
                  .password(System.getProperty("test.admin.password", "iris"))
                  .build());

          RestAssured.baseURI = BASE_URL;
          RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
          RestAssured.requestSpecification =
              new RequestSpecBuilder()
                  .setConfig(
                      RestAssuredConfig.config()
                          .objectMapperConfig(
                              new ObjectMapperConfig()
                                  .jackson2ObjectMapperFactory(
                                      (cls, charset) -> new ObjectMapper()))
                          .headerConfig(
                              HeaderConfig.headerConfig()
                                  .overwriteHeadersWithName("Authorization", "Content-Type")))
                  .build()
                  .contentType(ContentType.JSON)
                  .given()
                  .filter(new CustomAuthFilter())
                  .filter(CUSTOM_LOG_FILTER)
                  .log()
                  .all();
        });
    AfterStep(
        0,
        (Scenario scenario) -> {
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
            scenario.attach(
                CUSTOM_LOG_FILTER.getCallLog().get(),
                "text/plain",
                "API communication for: " + currentStepName);
            CUSTOM_LOG_FILTER.clear();
          stepNo++;
        });

    And(
        "Default user is {string}",
        (String label) -> scenarioContext.setDefaultUser(scenarioContext.getUser(label)));

    And(
        "Default user is admin",
        () -> scenarioContext.setDefaultUser(scenarioContext.getAdminUser()));

    And("Give it time - {int} seconds", (Integer seconds) -> SECONDS.sleep(seconds));
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
