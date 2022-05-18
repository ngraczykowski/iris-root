package com.silenteight.payments.bridge.agents.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class CompanyNameSurroundingRequest {

  List<String> allNames;
}
