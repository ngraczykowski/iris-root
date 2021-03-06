package com.silenteight.hsbc.bridge.grpc;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.Duration;

@Value
@ConstructorBinding
@ConfigurationProperties("silenteight.bridge.grpc")
public class GrpcProperties {

  Duration recommendationDeadline;
  Duration datasetDeadline;
  Duration historicalDecisionsDeadline;
  Duration analysisDeadline;
  Duration alertDeadline;
  Duration isPepDeadline;
  Duration nameInformationDeadline;
  Duration modelDeadline;

  public long recommendationDeadlineInSeconds() {
    return recommendationDeadline.toSeconds();
  }

  public long datasetDeadlineInSeconds() {
    return datasetDeadline.toSeconds();
  }

  public long historicalDecisionsDeadlineInSeconds() {
    return historicalDecisionsDeadline.toSeconds();
  }

  public long analysisDeadlineInSeconds() {
    return analysisDeadline.toSeconds();
  }

  public long alertDeadlineInSeconds() {
    return alertDeadline.toSeconds();
  }

  public long isPepDeadlineInSeconds() {
    return isPepDeadline.toSeconds();
  }

  public long nameInformationDeadlineInSeconds() {
    return nameInformationDeadline.toSeconds();
  }

  public long modelDeadlineInSeconds() {
    return modelDeadline.toSeconds();
  }
}
