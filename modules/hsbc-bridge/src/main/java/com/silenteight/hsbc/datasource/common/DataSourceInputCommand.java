package com.silenteight.hsbc.datasource.common;

import lombok.Builder;
import lombok.Value;

import com.silenteight.hsbc.bridge.match.MatchComposite;

import java.util.List;

@Builder
@Value
public class DataSourceInputCommand {

  List<MatchComposite> matches;
  List<String> features;
}
