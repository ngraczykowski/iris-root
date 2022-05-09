package utils;

import lombok.SneakyThrows;

import com.aventstack.extentreports.ExtentTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuit.karate.RuntimeHook;
import com.intuit.karate.core.*;
import com.intuit.karate.http.HttpRequest;
import reporting.extent.ExtentManager;
import reporting.zephyr.ZephyrService;
import reporting.zephyr.ZephyrTestCase;
import reporting.zephyr.ZephyrTestCycle;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

public class KarateHooks implements RuntimeHook {
  ExtentTest scenario;
  OAuthUtils oauthUtils = new OAuthUtils();
  ZephyrService zephyrService = new ZephyrService();
  ZephyrTestCycle zephyrTestCycle;
  ZephyrTestCase zephyrTestCase;
  boolean isRegression = Boolean.parseBoolean(System.getProperty("karate.isRegression"));
  String featureName;

  @Override
  public boolean beforeScenario(ScenarioRuntime scenarioRuntime) {
    if (!featureName.equals("Utils")) {
      setSystemPropertiesForKarateVars(scenarioRuntime.engine.vars);
      generateScenarioNode(scenarioRuntime);
    }
    return true;
  }

  @Override
  public void afterScenario(ScenarioRuntime scenarioRuntime) {
    featureName = scenarioRuntime.featureRuntime.feature.getName();

    if (!featureName.equals("Utils")) {
      Scenario scenario = scenarioRuntime.scenario;

      if (isRegression) {
        zephyrTestCase =
            zephyrService.createTestCase(
                scenario.getName(), scenarioRuntime.result.isFailed(), scenario.getSteps());
        zephyrTestCase.updateTestScript();
        zephyrTestCycle.executeTestCase(zephyrTestCase);
      }
      createNodeWithReport(scenarioRuntime.engine);
    }
  }

  @Override
  public boolean beforeFeature(FeatureRuntime featureRuntime) {
    featureName = featureRuntime.feature.getName();

    if (!featureName.equals("Utils")) {
      if (isRegression) {
        zephyrTestCycle = zephyrService.createTestCycle(featureName);
      }
      ExtentManager.createReport();
    }
    return true;
  }

  @Override
  public void afterFeature(FeatureRuntime featureRuntime) {
    ExtentManager.getInstance().flush();
  }

  @Override
  public boolean beforeStep(Step step, ScenarioRuntime scenarioRuntime) {
    return true;
  }

  @Override
  public void beforeHttpCall(HttpRequest request, ScenarioRuntime sr) {
    authorizeRequest(request);
  }

  @Override
  public void afterStep(StepResult result, ScenarioRuntime scenarioRuntime) {
    generateStepNode(scenarioRuntime.engine, result);
  }

  // region Generic methods
  private void setSystemPropertiesForKarateVars(Map<String, Variable> karateVars) {
    System.setProperty("karate.baseUrl", karateVars.get("baseUrl").getValue());
    System.setProperty("karate.maxWaitTime", karateVars.get("maxWaitTime").getAsString());
  }

  @SneakyThrows
  private void authorizeRequest(HttpRequest request) {
    request.putHeader("Authorization", "Bearer " + oauthUtils.getAccessToken());
  }
  // endregion

  // region Report generation methods
  @SneakyThrows
  private void createNodeWithReport(ScenarioEngine context) {
    if (context.getAllVariablesAsMap().containsKey("report")) {
      scenario
          .createNode("Report")
          .info(
              "<b>Report: </b>"
                  + new ObjectMapper()
                      .writeValueAsString(context.getAllVariablesAsMap().get("report")));
    }
  }

  private void generateScenarioNode(ScenarioRuntime scenarioRuntime) {
    String featureName = scenarioRuntime.featureRuntime.feature.getName();

    setSystemPropertiesForKarateVars(scenarioRuntime.engine.vars);

    scenario =
        ExtentManager.getInstance()
            .createTest(
                String.format(
                    "<b>%s.feature</b><br>Scenario: %s",
                    featureName, scenarioRuntime.result.getScenario().getName()));
  }

  private void generateStepNode(ScenarioEngine context, StepResult result) {
    Step gherkinStep = result.getStep();
    String gherkinStepPlainText = gherkinStep.toString();

    if (Stream.of("* def", "* url", "* call read").noneMatch(gherkinStepPlainText::contains)) {
      ExtentTest step =
          scenario.createNode(
              String.format("<b>%s</b> %s", gherkinStep.getPrefix(), gherkinStep.getText()));
      if (result.getResult().isFailed()) {
        step.fail(
            "<b>Error:</b> " + result.getResult().getError().getMessage().split(", response:")[0]);
      }
      try {
        if (gherkinStep
            .getText()
            .contains(context.getRequest().getMethod().toLowerCase(Locale.ROOT))) {
          step.info("<b>URL: </b>" + context.getRequest().getUrl());
          step.info("<b>Headers: </b>" + context.getRequest().getHeaders());
          if (context.getRequest().getBody() != null) {
            step.info("<b>Body: </b>" + context.getRequest().getBodyAsString());
          }
          step.info("<b>Response: </b>" + context.getResponse().getBodyAsString());
        }
      } catch (Exception e) {
        // do nothing
      }
    }
  }
  // endregion
}
