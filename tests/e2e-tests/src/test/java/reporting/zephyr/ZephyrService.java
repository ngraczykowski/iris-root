package reporting.zephyr;

import com.intuit.karate.FileUtils;
import com.intuit.karate.Http;
import com.intuit.karate.core.Step;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ZephyrService {

  private String generateKey(String testCaseName) {
    return testCaseName.substring(0, testCaseName.indexOf(" "));
  }

  private String getScenarioAsString(List<Step> scenarioSteps) {
    StringBuilder scenario = new StringBuilder();
    for (Step step : scenarioSteps) {
      scenario.append(step.toString()).append("\\n");
    }
    return scenario.toString();
  }

  private String getStatusAsString(boolean isFailed) {
    if (!isFailed) {
      return "Pass";
    } else {
      return "Fail";
    }
  }

  public ZephyrTestCase createTestCase(
      String testCaseName, boolean isFailed, List<Step> scenarioSteps) {
    return ZephyrTestCase.builder()
        .key(generateKey(testCaseName))
        .scenario(getScenarioAsString(scenarioSteps))
        .status(getStatusAsString(isFailed))
        .build();
  }

  public ZephyrTestCycle createTestCycle(String featureName) {
    return ZephyrTestCycle.builder()
        .key(
            Http.to("https://api.zephyrscale.smartbear.com/v2/testcycles")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + System.getProperty("zephyrAuthToken"))
                .post(
                    FileUtils.toString(
                            new File("src/test/resources/zephyrAPI/testCycleCreation.json"))
                        .replace(
                            "${testCycleName}",
                            String.format(
                                "Karate Test Cycle for %s-%s",
                                featureName,
                                new SimpleDateFormat("dd-MM-yyyy").format(new Date()))))
                .json()
                .get("key"))
        .build();
  }
}
