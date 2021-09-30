package com.silenteight.payments.bridge.svb.etl.response;

import lombok.Builder;
import lombok.Value;

import com.silenteight.payments.bridge.common.dto.common.SolutionType;
import com.silenteight.payments.bridge.common.dto.common.WatchlistType;
import com.silenteight.payments.bridge.svb.etl.model.AbstractMessageStructure;

import java.util.List;

@Value
@Builder
public class HitAndWatchlistPartyData {

  AbstractMessageStructure messageStructure;
  SolutionType solutionType;
  WatchlistType watchlistType;
  String tag;
  String id;
  String name;
  String entityText;
  String matchingText;
  List<String> allMatchingTexts;
  String fieldValue;
  List<String> allMatchingFieldValues;
  String accountNumberOrNormalizedName;
  List<String> postalAddresses;
  List<String> cities;
  List<String> states;
  List<String> countries;
  boolean mainAddress;
  String origin;
  String designation;
  String direction;
}
