package com.silenteight.agent.facade.datasource;

import lombok.Value;

import java.util.List;

@Value
public class GetMatchInputsCommand {

  List<String> features;
  List<String> matches;
}
