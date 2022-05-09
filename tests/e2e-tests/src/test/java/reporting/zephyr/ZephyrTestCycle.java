package reporting.zephyr;

import lombok.Builder;
import lombok.Value;

import com.intuit.karate.FileUtils;
import com.intuit.karate.Http;

import java.io.File;

@Value
@Builder(toBuilder = true)
public class ZephyrTestCycle {
  String key;

  public void executeTestCase(ZephyrTestCase zephyrTestCase) {
    Http.to("https://api.zephyrscale.smartbear.com/v2/testexecutions")
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer " + System.getProperty("zephyrAuthToken"))
        .post(
            FileUtils.toString(new File("src/test/resources/zephyrAPI/testExecution.json"))
                .replace("${testCycleKey}", getKey())
                .replace("${testCaseKey}", zephyrTestCase.getKey())
                .replace("${testCaseStatus}", zephyrTestCase.getStatus()));
  }
}
