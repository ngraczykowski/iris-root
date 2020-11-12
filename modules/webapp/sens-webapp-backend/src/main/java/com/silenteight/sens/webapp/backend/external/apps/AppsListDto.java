package com.silenteight.sens.webapp.backend.external.apps;

import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
public class AppsListDto {

  List<String> apps;
}
