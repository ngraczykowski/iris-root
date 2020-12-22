package com.silenteight.serp.governance.decisiontreesummaryquery.dto;

import lombok.Value;

import com.silenteight.proto.serp.v1.governance.DecisionTreeSummary;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@Value
public class DecisionTreeSummaryDto {

  long id;
  @NotNull
  String name;
  @NotNull
  List<String> decisionGroups;

  public DecisionTreeSummaryDto(DecisionTreeSummary summary) {
    this.id = summary.getId();
    this.name = summary.getName();
    this.decisionGroups = summary.getDecisionGroupList();
  }
}
