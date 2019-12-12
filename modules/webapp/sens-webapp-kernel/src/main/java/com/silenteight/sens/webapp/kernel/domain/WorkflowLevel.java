package com.silenteight.sens.webapp.kernel.domain;

import lombok.Value;

@Value(staticConstructor = "of")
public class WorkflowLevel {

  long decisionTreeId;

  int level;
}
