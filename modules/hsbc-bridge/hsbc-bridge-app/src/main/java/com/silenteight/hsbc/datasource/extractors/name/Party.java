package com.silenteight.hsbc.datasource.extractors.name;

import lombok.Value;

import java.util.List;

@Value
public class Party {

  List<String> alertedPartyIndividuals;
  List<String> watchlistPartyIndividuals;

  List<String> nnsIndividuals;
}
