package com.silenteight.searpayments.scb.etl.response;

import lombok.Builder;
import lombok.Value;

import com.silenteight.searpayments.bridge.model.SolutionType;
import com.silenteight.searpayments.bridge.model.WatchlistType;
import com.silenteight.searpayments.scb.etl.utils.AbstractMessageStructure;

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
