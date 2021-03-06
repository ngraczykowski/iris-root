package com.silenteight.hsbc.bridge.json.external.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Slf4j
public class CaseInformation {

  @JsonProperty("DN_CASE.ID")
  private String id;
  @JsonProperty("DN_CASE.caseGroup")
  private String caseGroup;
  @JsonProperty("DN_CASE.caseType")
  private String caseType;
  @JsonProperty("DN_CASE.externalId")
  private String externalId;
  @JsonProperty("DN_CASE.externalIdSort")
  private String externalIdSort;
  @JsonProperty("DN_CASE.caseKey")
  private String caseKey;
  @JsonProperty("DN_CASE.keyLabel")
  private String keyLabel;
  @JsonProperty("DN_CASE.parentId")
  private String parentId;
  @JsonProperty("DN_CASE.supplementaryKey")
  private String supplementaryKey;
  @JsonProperty("DN_CASE.supplementaryType")
  private String supplementaryType;
  @JsonProperty("DN_CASE.flagKey")
  private String flagKey;
  @JsonProperty("DN_CASE.description")
  private String description;
  @JsonProperty("DN_CASE.createdBy")
  private String createdBy;
  @JsonProperty("DN_CASE.createdDateTime")
  private String createdDateTime;
  @JsonProperty("DN_CASE.modifiedBy")
  private String modifiedBy;
  @JsonProperty("DN_CASE.modifiedDateTime")
  private String modifiedDateTime;
  @JsonProperty("DN_CASE.assignedUser")
  private String assignedUser;
  @JsonProperty("DN_CASE.assignedBy")
  private String assignedBy;
  @JsonProperty("DN_CASE.assignedDateTime")
  private String assignedDateTime;
  @JsonProperty("DN_CASE.priority")
  private String priority;
  @JsonProperty("DN_CASE.permission")
  private String permission;
  @JsonProperty("DN_CASE.currentState")
  private String currentState;
  @JsonProperty("DN_CASE.derivedState")
  private String derivedState;
  @JsonProperty("DN_CASE.stateExpiry")
  private String stateExpiry;
  @JsonProperty("DN_CASE.stateChangeBy")
  private String stateChangeBy;
  @JsonProperty("DN_CASE.stateChangeDateTime")
  private String stateChangeDateTime;
  @JsonProperty("DN_CASE.sourceId")
  private String sourceId;
  @JsonProperty("DN_CASE.sourceName")
  private String sourceName;
  @JsonProperty("DN_CASE.caseMarker")
  private String caseMarker;
  @JsonProperty("DN_CASE.updatedBy")
  private String updatedBy;
  @JsonProperty("DN_CASE.updatedDateTime")
  private String updatedDateTime;
  @JsonProperty("DN_CASE.groupId")
  private String groupId;
  @JsonProperty("DN_CASE.groupLevel")
  private String groupLevel;
  @JsonProperty("DN_CASE.ExtendedAttribute1")
  private String extendedAttribute1;
  @JsonProperty("DN_CASE.ExtendedAttribute2")
  private String extendedAttribute2;
  @JsonProperty("DN_CASE.ExtendedAttribute3")
  private String extendedAttribute3;
  @JsonProperty("DN_CASE.ExtendedAttribute4")
  private String extendedAttribute4;
  @JsonProperty("DN_CASE.ExtendedAttribute5")
  private String extendedAttribute5;
  @JsonProperty("DN_CASE.ExtendedAttribute6")
  private String extendedAttribute6;
  @JsonProperty("DN_CASE.ExtendedAttribute7")
  private String extendedAttribute7;
  @JsonProperty("DN_CASE.ExtendedAttribute8")
  private String extendedAttribute8;
  @JsonProperty("DN_CASE.ExtendedAttribute9")
  private String extendedAttribute9;
  @JsonProperty("DN_CASE.ExtendedAttribute10")
  private String extendedAttribute10;
  @JsonProperty("DN_CASE.ExtendedAttribute11")
  private String extendedAttribute11;
  @JsonProperty("DN_CASE.ExtendedAttribute12")
  private String extendedAttribute12;
  @JsonProperty("DN_CASE.ExtendedAttribute13")
  private String extendedAttribute13;
  @JsonProperty("DN_CASE.ExtendedAttribute13_date_time")
  private String extendedAttribute13DateTime;
  @JsonProperty("DN_CASE.ExtendedAttribute14")
  private String extendedAttribute14;
  @JsonProperty("DN_CASE.ExtendedAttribute15")
  private String extendedAttribute15;
  @JsonProperty("DN_CASE.AlertURL")
  private String alertUrl;
}
