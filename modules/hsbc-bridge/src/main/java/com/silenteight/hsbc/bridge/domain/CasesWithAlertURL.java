package com.silenteight.hsbc.bridge.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CasesWithAlertURL {

  private int id;
  private String caseGroup;
  private String caseType;
  private String externalId;
  private String externalIdSort;
  private String caseKey;
  private String keyLabel;
  private Integer parentId;
  private String supplementaryKey;
  private String supplementaryType;
  private String flagKey;
  private String description;
  private Integer createdBy;
  private String createdDateTime;
  private Integer modifiedBy;
  private String modifiedDateTime;
  private Integer assignedUser;
  private Integer assignedBy;
  private String assignedDateTime;
  private Integer priority;
  private String permission;
  private String currentState;
  private String derivedState;
  private String stateExpiry;
  private Integer stateChangeBy;
  private String stateChangeDateTime;
  private Integer sourceId;
  private String sourceName;
  private Integer caseMarker;
  private Integer updatedBy;
  private String updatedDateTime;
  private String groupId;
  private Integer groupLevel;
  private Integer extendedAttribute1;
  private Integer extendedAttribute2;
  private String extendedAttribute3;
  private String extendedAttribute4;
  private String extendedAttribute5;
  private String extendedAttribute6;
  private String extendedAttribute7;
  private String extendedAttribute8;
  private String extendedAttribute9;
  private String extendedAttribute10;
  private String extendedAttribute11;
  private String extendedAttribute12;
  private Integer extendedAttribute13;
  private String alertUrl;
}
