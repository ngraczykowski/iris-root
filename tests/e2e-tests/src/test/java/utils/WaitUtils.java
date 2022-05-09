package utils;

import lombok.SneakyThrows;

import com.intuit.karate.Http;

public class WaitUtils {

  Integer maxWait = Integer.parseInt(System.getProperty("karate.maxWaitTime"));
  OAuthUtils oauthUtils = new OAuthUtils();

  @SneakyThrows
  public boolean waitForSolvingToComplete(String batchId) {
    for (int i = 0; i <= maxWait; i++) {
      if (Http.to(
              String.format(
                  "%srest/hsbc-bridge/async/batch/v1/%s/status",
                  System.getProperty("karate.baseUrl"), batchId))
          .header("Authorization", "Bearer " + oauthUtils.getAccessToken())
          .get()
          .json()
          .get("batchStatus")
          .equals("COMPLETED")) {
        return true;
      } else {
        Thread.sleep(1000);
      }
    }
    throw new RuntimeException(
        String.format("Batch was not in \"COMPLETED\" status in %d seconds", maxWait));
  }

  @SneakyThrows
  public boolean waitForReportToGenerate(String reportPath) {
    for (int i = 0; i <= maxWait; i++) {
      if (Http.to(
              String.format(
                  "%srest/warehouse/api/v2/%s/status",
                  System.getProperty("karate.baseUrl"), reportPath))
          .header("Authorization", "Bearer " + oauthUtils.getAccessToken())
          .get()
          .json()
          .get("status")
          .equals("OK")) {
        return true;
      } else {
        Thread.sleep(1000);
      }
    }
    throw new RuntimeException(String.format("Report was not generated in %d seconds", maxWait));
  }
}
