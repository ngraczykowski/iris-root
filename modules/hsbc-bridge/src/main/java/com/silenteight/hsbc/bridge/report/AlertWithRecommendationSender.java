package com.silenteight.hsbc.bridge.report;

import java.util.Collection;

public interface AlertWithRecommendationSender {

  void sendAlertsWithRecommendations(Collection<AlertWithRecommendation> alerts);

  interface AlertWithRecommendation {

    String getAlert();
    String getRecommendation();
  }
}
