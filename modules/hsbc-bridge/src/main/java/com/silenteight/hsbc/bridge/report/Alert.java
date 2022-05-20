package com.silenteight.hsbc.bridge.report;

import java.util.Collection;
import java.util.Map;

public interface Alert {

  String getName();

  String getDiscriminator();

  Map<String, String> getMetadata();

  Collection<Match> getMatches();

  default String getDiscriminatorSeparator() {
    return "_";
  }

  interface Match {

    String getName();

    Map<String, String> getMetadata();
  }
}
