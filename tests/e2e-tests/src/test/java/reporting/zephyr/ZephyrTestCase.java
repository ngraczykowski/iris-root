package reporting.zephyr;

import lombok.Builder;
import lombok.Value;

import com.intuit.karate.FileUtils;
import com.intuit.karate.Http;

import java.io.File;

@Value
@Builder(toBuilder = true)
public class ZephyrTestCase {
  String key;
  String status;
  String scenario;

  public void updateTestScript() {
    Http.to(
            String.format(
                "https://api.zephyrscale.smartbear.com/v2/testcases/%s/testscript", getKey()))
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer " + System.getProperty("zephyrAuthToken"))
        .post(
            FileUtils.toString(new File("src/test/resources/zephyrAPI/scenarioUpdate.json"))
                .replace("${scenario}", getScenario()));
  }
}
