package com.silenteight.payments.bridge.svb.learning.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class LearningRegisteredAlert {

  AlertRegistrationSource alertRegistrationSource;

  String alertName;

  AlertDetails alertDetails;

  List<RegisteredMatch> registeredMatches;
}
