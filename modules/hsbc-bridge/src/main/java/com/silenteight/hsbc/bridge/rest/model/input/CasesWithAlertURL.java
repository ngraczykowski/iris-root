package com.silenteight.hsbc.bridge.rest.model.input;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

/**
 * CasesWithAlertURL
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-02-17T10:41:43.102Z[GMT]")


public class CasesWithAlertURL   {
  @JsonProperty("iD")
  private Integer iD = null;

  @JsonProperty("caseGroup")
  private String caseGroup = null;

  @JsonProperty("caseType")
  private String caseType = null;

  @JsonProperty("externalID")
  private String externalID = null;

  @JsonProperty("externalIdSort")
  private String externalIdSort = null;

  @JsonProperty("caseKey")
  private String caseKey = null;

  @JsonProperty("keyLabel")
  private String keyLabel = null;

  @JsonProperty("parentID")
  private Integer parentID = null;

  @JsonProperty("supplementaryKey")
  private String supplementaryKey = null;

  @JsonProperty("supplementaryType")
  private String supplementaryType = null;

  @JsonProperty("flagKey")
  private String flagKey = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("createdBy")
  private Integer createdBy = null;

  @JsonProperty("createdDateTime")
  private String createdDateTime = null;

  @JsonProperty("modifiedBy")
  private Integer modifiedBy = null;

  @JsonProperty("modifiedDateTime")
  private String modifiedDateTime = null;

  @JsonProperty("assignedUser")
  private Integer assignedUser = null;

  @JsonProperty("assignedBy")
  private Integer assignedBy = null;

  @JsonProperty("assignedDateTime")
  private String assignedDateTime = null;

  @JsonProperty("priority")
  private Integer priority = null;

  @JsonProperty("permission")
  private String permission = null;

  @JsonProperty("currentState")
  private String currentState = null;

  @JsonProperty("derivedState")
  private String derivedState = null;

  @JsonProperty("stateExpiry")
  private String stateExpiry = null;

  @JsonProperty("stateChangeBy")
  private Integer stateChangeBy = null;

  @JsonProperty("stateChangeDateTime")
  private String stateChangeDateTime = null;

  @JsonProperty("sourceId")
  private Integer sourceId = null;

  @JsonProperty("sourceName")
  private String sourceName = null;

  @JsonProperty("caseMarker")
  private Integer caseMarker = null;

  @JsonProperty("updatedBy")
  private Integer updatedBy = null;

  @JsonProperty("updatedDateTime")
  private String updatedDateTime = null;

  @JsonProperty("groupId")
  private String groupId = null;

  @JsonProperty("groupLevel")
  private Integer groupLevel = null;

  @JsonProperty("extendedAttribute1")
  private Integer extendedAttribute1 = null;

  @JsonProperty("extendedAttribute2")
  private Integer extendedAttribute2 = null;

  @JsonProperty("extendedAttribute3")
  private String extendedAttribute3 = null;

  @JsonProperty("extendedAttribute4")
  private String extendedAttribute4 = null;

  @JsonProperty("extendedAttribute5")
  private String extendedAttribute5 = null;

  @JsonProperty("extendedAttribute6")
  private String extendedAttribute6 = null;

  @JsonProperty("extendedAttribute7")
  private String extendedAttribute7 = null;

  @JsonProperty("extendedAttribute8")
  private String extendedAttribute8 = null;

  @JsonProperty("extendedAttribute9")
  private String extendedAttribute9 = null;

  @JsonProperty("extendedAttribute10")
  private String extendedAttribute10 = null;

  @JsonProperty("extendedAttribute11")
  private String extendedAttribute11 = null;

  @JsonProperty("extendedAttribute12")
  private String extendedAttribute12 = null;

  @JsonProperty("extendedAttribute13")
  private Integer extendedAttribute13 = null;

  @JsonProperty("alertURL")
  private String alertURL = null;

  public CasesWithAlertURL iD(Integer iD) {
    this.iD = iD;
    return this;
  }

  /**
   * Internal unique Identifier assigned to the Case or Alert within Case Management . Used to link to other tables.
   * @return iD
   **/
  @Schema(description = "Internal unique Identifier assigned to the Case or Alert within Case Management . Used to link to other tables.")
  
    public Integer getID() {
    return iD;
  }

  public void setID(Integer iD) {
    this.iD = iD;
  }

  public CasesWithAlertURL caseGroup(String caseGroup) {
    this.caseGroup = caseGroup;
    return this;
  }

  /**
   * Records an associated group to the Case or Alert, for example&#58; Match
   * @return caseGroup
   **/
  @Schema(description = "Records an associated group to the Case or Alert, for example&#58; Match")
  
    public String getCaseGroup() {
    return caseGroup;
  }

  public void setCaseGroup(String caseGroup) {
    this.caseGroup = caseGroup;
  }

  public CasesWithAlertURL caseType(String caseType) {
    this.caseType = caseType;
    return this;
  }

  /**
   * Denotes if this is a Case (\"Case\") or an Alert (\"Issue\")
   * @return caseType
   **/
  @Schema(description = "Denotes if this is a Case (\"Case\") or an Alert (\"Issue\")")
  
    public String getCaseType() {
    return caseType;
  }

  public void setCaseType(String caseType) {
    this.caseType = caseType;
  }

  public CasesWithAlertURL externalID(String externalID) {
    this.externalID = externalID;
    return this;
  }

  /**
   * A unique ID of the alerts as it appears in Case Management.
   * @return externalID
   **/
  @Schema(description = "A unique ID of the alerts as it appears in Case Management.")
  
    public String getExternalID() {
    return externalID;
  }

  public void setExternalID(String externalID) {
    this.externalID = externalID;
  }

  public CasesWithAlertURL externalIdSort(String externalIdSort) {
    this.externalIdSort = externalIdSort;
    return this;
  }

  /**
   * A unique ID of the alert that is used in reporting. The value of the code is padded with zeros
   * @return externalIdSort
   **/
  @Schema(description = "A unique ID of the alert that is used in reporting. The value of the code is padded with zeros")
  
    public String getExternalIdSort() {
    return externalIdSort;
  }

  public void setExternalIdSort(String externalIdSort) {
    this.externalIdSort = externalIdSort;
  }

  public CasesWithAlertURL caseKey(String caseKey) {
    this.caseKey = caseKey;
    return this;
  }

  /**
   * Unique internal Hash which identifies an alert.
   * @return caseKey
   **/
  @Schema(description = "Unique internal Hash which identifies an alert.")
  
    public String getCaseKey() {
    return caseKey;
  }

  public void setCaseKey(String caseKey) {
    this.caseKey = caseKey;
  }

  public CasesWithAlertURL keyLabel(String keyLabel) {
    this.keyLabel = keyLabel;
    return this;
  }

  /**
   * A Unique Identifier for the Case or Alert, composed of \"Customer ID&#58; Watchlist Alert Generated Against&#58; Watch Person ID\"
   * @return keyLabel
   **/
  @Schema(description = "A Unique Identifier for the Case or Alert, composed of \"Customer ID&#58; Watchlist Alert Generated Against&#58; Watch Person ID\"")
  
    public String getKeyLabel() {
    return keyLabel;
  }

  public void setKeyLabel(String keyLabel) {
    this.keyLabel = keyLabel;
  }

  public CasesWithAlertURL parentID(Integer parentID) {
    this.parentID = parentID;
    return this;
  }

  /**
   * ID number for the parent Case of the Alert. If viewing at Case Level; the field will display \"-1\"
   * @return parentID
   **/
  @Schema(description = "ID number for the parent Case of the Alert. If viewing at Case Level; the field will display \"-1\"")
  
    public Integer getParentID() {
    return parentID;
  }

  public void setParentID(Integer parentID) {
    this.parentID = parentID;
  }

  public CasesWithAlertURL supplementaryKey(String supplementaryKey) {
    this.supplementaryKey = supplementaryKey;
    return this;
  }

  /**
   * Hash of the supplementary data held against an alert, used by the system to identify if any supplementary data has changed.
   * @return supplementaryKey
   **/
  @Schema(description = "Hash of the supplementary data held against an alert, used by the system to identify if any supplementary data has changed.")
  
    public String getSupplementaryKey() {
    return supplementaryKey;
  }

  public void setSupplementaryKey(String supplementaryKey) {
    this.supplementaryKey = supplementaryKey;
  }

  public CasesWithAlertURL supplementaryType(String supplementaryType) {
    this.supplementaryType = supplementaryType;
    return this;
  }

  /**
   * Identifies and provides grouping by type of supplementary data
   * @return supplementaryType
   **/
  @Schema(description = "Identifies and provides grouping by type of supplementary data")
  
    public String getSupplementaryType() {
    return supplementaryType;
  }

  public void setSupplementaryType(String supplementaryType) {
    this.supplementaryType = supplementaryType;
  }

  public CasesWithAlertURL flagKey(String flagKey) {
    this.flagKey = flagKey;
    return this;
  }

  /**
   * Hash of the flag key data held against an alert, used by the system to identify if any flag key data has changed. If a change is identified the caseMaker is updated.
   * @return flagKey
   **/
  @Schema(description = "Hash of the flag key data held against an alert, used by the system to identify if any flag key data has changed. If a change is identified the caseMaker is updated.")
  
    public String getFlagKey() {
    return flagKey;
  }

  public void setFlagKey(String flagKey) {
    this.flagKey = flagKey;
  }

  public CasesWithAlertURL description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Name of the Customer when initially provided or if updated by a user.
   * @return description
   **/
  @Schema(description = "Name of the Customer when initially provided or if updated by a user.")
  
    public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public CasesWithAlertURL createdBy(Integer createdBy) {
    this.createdBy = createdBy;
    return this;
  }

  /**
   * Records how the Case was created, for example&#58; Batch, Manual or Real-Time Screening
   * @return createdBy
   **/
  @Schema(description = "Records how the Case was created, for example&#58; Batch, Manual or Real-Time Screening")
  
    public Integer getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(Integer createdBy) {
    this.createdBy = createdBy;
  }

  public CasesWithAlertURL createdDateTime(String createdDateTime) {
    this.createdDateTime = createdDateTime;
    return this;
  }

  /**
   * Date the Case or Alert was created
   * @return createdDateTime
   **/
  @Schema(description = "Date the Case or Alert was created")
  
    public String getCreatedDateTime() {
    return createdDateTime;
  }

  public void setCreatedDateTime(String createdDateTime) {
    this.createdDateTime = createdDateTime;
  }

  public CasesWithAlertURL modifiedBy(Integer modifiedBy) {
    this.modifiedBy = modifiedBy;
    return this;
  }

  /**
   * Last User to modify the Case or Alert. Changes to the Case or Alert State, Comments or Attachments constitute a modification
   * @return modifiedBy
   **/
  @Schema(description = "Last User to modify the Case or Alert. Changes to the Case or Alert State, Comments or Attachments constitute a modification")
  
    public Integer getModifiedBy() {
    return modifiedBy;
  }

  public void setModifiedBy(Integer modifiedBy) {
    this.modifiedBy = modifiedBy;
  }

  public CasesWithAlertURL modifiedDateTime(String modifiedDateTime) {
    this.modifiedDateTime = modifiedDateTime;
    return this;
  }

  /**
   * Date and time of the modification
   * @return modifiedDateTime
   **/
  @Schema(description = "Date and time of the modification")
  
    public String getModifiedDateTime() {
    return modifiedDateTime;
  }

  public void setModifiedDateTime(String modifiedDateTime) {
    this.modifiedDateTime = modifiedDateTime;
  }

  public CasesWithAlertURL assignedUser(Integer assignedUser) {
    this.assignedUser = assignedUser;
    return this;
  }

  /**
   * ID of the user to whom the Case or Alert is assigned
   * @return assignedUser
   **/
  @Schema(description = "ID of the user to whom the Case or Alert is assigned")
  
    public Integer getAssignedUser() {
    return assignedUser;
  }

  public void setAssignedUser(Integer assignedUser) {
    this.assignedUser = assignedUser;
  }

  public CasesWithAlertURL assignedBy(Integer assignedBy) {
    this.assignedBy = assignedBy;
    return this;
  }

  /**
   * ID of the user who assigned the Case or Alert to the User
   * @return assignedBy
   **/
  @Schema(description = "ID of the user who assigned the Case or Alert to the User")
  
    public Integer getAssignedBy() {
    return assignedBy;
  }

  public void setAssignedBy(Integer assignedBy) {
    this.assignedBy = assignedBy;
  }

  public CasesWithAlertURL assignedDateTime(String assignedDateTime) {
    this.assignedDateTime = assignedDateTime;
    return this;
  }

  /**
   * Date and time the Case or Alert was assigned
   * @return assignedDateTime
   **/
  @Schema(description = "Date and time the Case or Alert was assigned")
  
    public String getAssignedDateTime() {
    return assignedDateTime;
  }

  public void setAssignedDateTime(String assignedDateTime) {
    this.assignedDateTime = assignedDateTime;
  }

  public CasesWithAlertURL priority(Integer priority) {
    this.priority = priority;
    return this;
  }

  /**
   * Records the alert priority level (250=Low, 500=Medium, 750=High)
   * @return priority
   **/
  @Schema(description = "Records the alert priority level (250=Low, 500=Medium, 750=High)")
  
    public Integer getPriority() {
    return priority;
  }

  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  public CasesWithAlertURL permission(String permission) {
    this.permission = permission;
    return this;
  }

  /**
   * Permission assigned to the Case or Alert
   * @return permission
   **/
  @Schema(description = "Permission assigned to the Case or Alert")
  
    public String getPermission() {
    return permission;
  }

  public void setPermission(String permission) {
    this.permission = permission;
  }

  public CasesWithAlertURL currentState(String currentState) {
    this.currentState = currentState;
    return this;
  }

  /**
   * Workflow state in which the Alert currently resides, for example&#58; Level 1 Review
   * @return currentState
   **/
  @Schema(description = "Workflow state in which the Alert currently resides, for example&#58; Level 1 Review")
  
    public String getCurrentState() {
    return currentState;
  }

  public void setCurrentState(String currentState) {
    this.currentState = currentState;
  }

  public CasesWithAlertURL derivedState(String derivedState) {
    this.derivedState = derivedState;
    return this;
  }

  /**
   * Workflow state in which the Case currently resides. Case state is controlled by the system and is derived from the activity associated with the Alerts within the Case. A Case cannot be closed until all of the Alerts within it have been closed.
   * @return derivedState
   **/
  @Schema(description = "Workflow state in which the Case currently resides. Case state is controlled by the system and is derived from the activity associated with the Alerts within the Case. A Case cannot be closed until all of the Alerts within it have been closed.")
  
    public String getDerivedState() {
    return derivedState;
  }

  public void setDerivedState(String derivedState) {
    this.derivedState = derivedState;
  }

  public CasesWithAlertURL stateExpiry(String stateExpiry) {
    this.stateExpiry = stateExpiry;
    return this;
  }

  /**
   * Date when the current state of the Alert will expire
   * @return stateExpiry
   **/
  @Schema(description = "Date when the current state of the Alert will expire")
  
    public String getStateExpiry() {
    return stateExpiry;
  }

  public void setStateExpiry(String stateExpiry) {
    this.stateExpiry = stateExpiry;
  }

  public CasesWithAlertURL stateChangeBy(Integer stateChangeBy) {
    this.stateChangeBy = stateChangeBy;
    return this;
  }

  /**
   * Which User undertook the last transition of the Alert through the Workflow
   * @return stateChangeBy
   **/
  @Schema(description = "Which User undertook the last transition of the Alert through the Workflow")
  
    public Integer getStateChangeBy() {
    return stateChangeBy;
  }

  public void setStateChangeBy(Integer stateChangeBy) {
    this.stateChangeBy = stateChangeBy;
  }

  public CasesWithAlertURL stateChangeDateTime(String stateChangeDateTime) {
    this.stateChangeDateTime = stateChangeDateTime;
    return this;
  }

  /**
   * Date and time the state of the Case or Alert was changed
   * @return stateChangeDateTime
   **/
  @Schema(description = "Date and time the state of the Case or Alert was changed")
  
    public String getStateChangeDateTime() {
    return stateChangeDateTime;
  }

  public void setStateChangeDateTime(String stateChangeDateTime) {
    this.stateChangeDateTime = stateChangeDateTime;
  }

  public CasesWithAlertURL sourceId(Integer sourceId) {
    this.sourceId = sourceId;
    return this;
  }

  /**
   * Case Source System ID
   * @return sourceId
   **/
  @Schema(description = "Case Source System ID")
  
    public Integer getSourceId() {
    return sourceId;
  }

  public void setSourceId(Integer sourceId) {
    this.sourceId = sourceId;
  }

  public CasesWithAlertURL sourceName(String sourceName) {
    this.sourceName = sourceName;
    return this;
  }

  /**
   * Case Source System Name
   * @return sourceName
   **/
  @Schema(description = "Case Source System Name")
  
    public String getSourceName() {
    return sourceName;
  }

  public void setSourceName(String sourceName) {
    this.sourceName = sourceName;
  }

  public CasesWithAlertURL caseMarker(Integer caseMarker) {
    this.caseMarker = caseMarker;
    return this;
  }

  /**
   * this is the review flag which if  indicates if customer data in any of the review flag fields has been updated. This is used to signify a change or trigger a realert. 0 indicates no review flag set.
   * @return caseMarker
   **/
  @Schema(description = "this is the review flag which if  indicates if customer data in any of the review flag fields has been updated. This is used to signify a change or trigger a realert. 0 indicates no review flag set.")
  
    public Integer getCaseMarker() {
    return caseMarker;
  }

  public void setCaseMarker(Integer caseMarker) {
    this.caseMarker = caseMarker;
  }

  public CasesWithAlertURL updatedBy(Integer updatedBy) {
    this.updatedBy = updatedBy;
    return this;
  }

  /**
   * User ID of the account that updated the case.
   * @return updatedBy
   **/
  @Schema(description = "User ID of the account that updated the case.")
  
    public Integer getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(Integer updatedBy) {
    this.updatedBy = updatedBy;
  }

  public CasesWithAlertURL updatedDateTime(String updatedDateTime) {
    this.updatedDateTime = updatedDateTime;
    return this;
  }

  /**
   * Date and time the alert was updated.
   * @return updatedDateTime
   **/
  @Schema(description = "Date and time the alert was updated.")
  
    public String getUpdatedDateTime() {
    return updatedDateTime;
  }

  public void setUpdatedDateTime(String updatedDateTime) {
    this.updatedDateTime = updatedDateTime;
  }

  public CasesWithAlertURL groupId(String groupId) {
    this.groupId = groupId;
    return this;
  }

  /**
   * ID number of the associated permission or grouping to which the Case or Alert belongs, for example&#58; Country or Regional Group.
   * @return groupId
   **/
  @Schema(description = "ID number of the associated permission or grouping to which the Case or Alert belongs, for example&#58; Country or Regional Group.")
  
    public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public CasesWithAlertURL groupLevel(Integer groupLevel) {
    this.groupLevel = groupLevel;
    return this;
  }

  /**
   * Where the group sits within the permission or grouping structure
   * @return groupLevel
   **/
  @Schema(description = "Where the group sits within the permission or grouping structure")
  
    public Integer getGroupLevel() {
    return groupLevel;
  }

  public void setGroupLevel(Integer groupLevel) {
    this.groupLevel = groupLevel;
  }

  public CasesWithAlertURL extendedAttribute1(Integer extendedAttribute1) {
    this.extendedAttribute1 = extendedAttribute1;
    return this;
  }

  /**
   * Escalation. Indicates if the alert has been escalated.
   * @return extendedAttribute1
   **/
  @Schema(description = "Escalation. Indicates if the alert has been escalated.")
  
    public Integer getExtendedAttribute1() {
    return extendedAttribute1;
  }

  public void setExtendedAttribute1(Integer extendedAttribute1) {
    this.extendedAttribute1 = extendedAttribute1;
  }

  public CasesWithAlertURL extendedAttribute2(Integer extendedAttribute2) {
    this.extendedAttribute2 = extendedAttribute2;
    return this;
  }

  /**
   * Priority Score. Indicates how accurate the match is between the customer and the list record scored 0 to 100. It is not a percentage.
   * @return extendedAttribute2
   **/
  @Schema(description = "Priority Score. Indicates how accurate the match is between the customer and the list record scored 0 to 100. It is not a percentage.")
  
    public Integer getExtendedAttribute2() {
    return extendedAttribute2;
  }

  public void setExtendedAttribute2(Integer extendedAttribute2) {
    this.extendedAttribute2 = extendedAttribute2;
  }

  public CasesWithAlertURL extendedAttribute3(String extendedAttribute3) {
    this.extendedAttribute3 = extendedAttribute3;
    return this;
  }

  /**
   * Risk Score. Indicates, if provided with the list data, how much of a risk the individual or entity is. 0 to 100
   * @return extendedAttribute3
   **/
  @Schema(description = "Risk Score. Indicates, if provided with the list data, how much of a risk the individual or entity is. 0 to 100")
  
    public String getExtendedAttribute3() {
    return extendedAttribute3;
  }

  public void setExtendedAttribute3(String extendedAttribute3) {
    this.extendedAttribute3 = extendedAttribute3;
  }

  public CasesWithAlertURL extendedAttribute4(String extendedAttribute4) {
    this.extendedAttribute4 = extendedAttribute4;
    return this;
  }

  /**
   * PEP Risk Score. Indicates, if provided with the list data, how much of a risk the individual or entity is. 0 to 100
   * @return extendedAttribute4
   **/
  @Schema(description = "PEP Risk Score. Indicates, if provided with the list data, how much of a risk the individual or entity is. 0 to 100")
  
    public String getExtendedAttribute4() {
    return extendedAttribute4;
  }

  public void setExtendedAttribute4(String extendedAttribute4) {
    this.extendedAttribute4 = extendedAttribute4;
  }

  public CasesWithAlertURL extendedAttribute5(String extendedAttribute5) {
    this.extendedAttribute5 = extendedAttribute5;
    return this;
  }

  /**
   * List Record Type. Describes the type of list record the match is against.
   * @return extendedAttribute5
   **/
  @Schema(description = "List Record Type. Describes the type of list record the match is against.")
  
    public String getExtendedAttribute5() {
    return extendedAttribute5;
  }

  public void setExtendedAttribute5(String extendedAttribute5) {
    this.extendedAttribute5 = extendedAttribute5;
  }

  public CasesWithAlertURL extendedAttribute6(String extendedAttribute6) {
    this.extendedAttribute6 = extendedAttribute6;
    return this;
  }

  /**
   * Screening Mode. Indicate the type of screening that created the alert.
   * @return extendedAttribute6
   **/
  @Schema(description = "Screening Mode. Indicate the type of screening that created the alert.")
  
    public String getExtendedAttribute6() {
    return extendedAttribute6;
  }

  public void setExtendedAttribute6(String extendedAttribute6) {
    this.extendedAttribute6 = extendedAttribute6;
  }

  public CasesWithAlertURL extendedAttribute7(String extendedAttribute7) {
    this.extendedAttribute7 = extendedAttribute7;
    return this;
  }

  /**
   * is the QA Flag which indicates if an alert has been subject to a QA review.
   * @return extendedAttribute7
   **/
  @Schema(description = "is the QA Flag which indicates if an alert has been subject to a QA review.")
  
    public String getExtendedAttribute7() {
    return extendedAttribute7;
  }

  public void setExtendedAttribute7(String extendedAttribute7) {
    this.extendedAttribute7 = extendedAttribute7;
  }

  public CasesWithAlertURL extendedAttribute8(String extendedAttribute8) {
    this.extendedAttribute8 = extendedAttribute8;
    return this;
  }

  /**
   * Day 1 Spike Flag. Indicates if an alert was created on day 1. \"Day 1 Spike\" = created on Day 1, \"BAU\" = createdon Day 1 but has ben work to closure.
   * @return extendedAttribute8
   **/
  @Schema(description = "Day 1 Spike Flag. Indicates if an alert was created on day 1. \"Day 1 Spike\" = created on Day 1, \"BAU\" = createdon Day 1 but has ben work to closure.")
  
    public String getExtendedAttribute8() {
    return extendedAttribute8;
  }

  public void setExtendedAttribute8(String extendedAttribute8) {
    this.extendedAttribute8 = extendedAttribute8;
  }

  public CasesWithAlertURL extendedAttribute9(String extendedAttribute9) {
    this.extendedAttribute9 = extendedAttribute9;
    return this;
  }

  /**
   * BAU Spike Flag. Indicates if the alert was created as part of an influx of alert i.e. a spike, after day 1. \"Yes\" indicates this is a BAU spike
   * @return extendedAttribute9
   **/
  @Schema(description = "BAU Spike Flag. Indicates if the alert was created as part of an influx of alert i.e. a spike, after day 1. \"Yes\" indicates this is a BAU spike")
  
    public String getExtendedAttribute9() {
    return extendedAttribute9;
  }

  public void setExtendedAttribute9(String extendedAttribute9) {
    this.extendedAttribute9 = extendedAttribute9;
  }

  public CasesWithAlertURL extendedAttribute10(String extendedAttribute10) {
    this.extendedAttribute10 = extendedAttribute10;
    return this;
  }

  /**
   * OWS Watchlist Name, describe the specific type of list the matching record came from.
   * @return extendedAttribute10
   **/
  @Schema(description = "OWS Watchlist Name, describe the specific type of list the matching record came from.")
  
    public String getExtendedAttribute10() {
    return extendedAttribute10;
  }

  public void setExtendedAttribute10(String extendedAttribute10) {
    this.extendedAttribute10 = extendedAttribute10;
  }

  public CasesWithAlertURL extendedAttribute11(String extendedAttribute11) {
    this.extendedAttribute11 = extendedAttribute11;
    return this;
  }

  /**
   * Line of Business, describes the Line of business the alert is related to.
   * @return extendedAttribute11
   **/
  @Schema(description = "Line of Business, describes the Line of business the alert is related to.")
  
    public String getExtendedAttribute11() {
    return extendedAttribute11;
  }

  public void setExtendedAttribute11(String extendedAttribute11) {
    this.extendedAttribute11 = extendedAttribute11;
  }

  public CasesWithAlertURL extendedAttribute12(String extendedAttribute12) {
    this.extendedAttribute12 = extendedAttribute12;
    return this;
  }

  /**
   * HSBC priority Flag, indicates the HSBC priority of an alert.
   * @return extendedAttribute12
   **/
  @Schema(description = "HSBC priority Flag, indicates the HSBC priority of an alert.")
  
    public String getExtendedAttribute12() {
    return extendedAttribute12;
  }

  public void setExtendedAttribute12(String extendedAttribute12) {
    this.extendedAttribute12 = extendedAttribute12;
  }

  public CasesWithAlertURL extendedAttribute13(Integer extendedAttribute13) {
    this.extendedAttribute13 = extendedAttribute13;
    return this;
  }

  /**
   * SLA Flag, a marker to define when an alert was created, closed or re-opened
   * @return extendedAttribute13
   **/
  @Schema(description = "SLA Flag, a marker to define when an alert was created, closed or re-opened")
  
    public Integer getExtendedAttribute13() {
    return extendedAttribute13;
  }

  public void setExtendedAttribute13(Integer extendedAttribute13) {
    this.extendedAttribute13 = extendedAttribute13;
  }

  public CasesWithAlertURL alertURL(String alertURL) {
    this.alertURL = alertURL;
    return this;
  }

  /**
   * URL, which take you directly to the system where this alert resides and open the alert.
   * @return alertURL
   **/
  @Schema(description = "URL, which take you directly to the system where this alert resides and open the alert.")
  
    public String getAlertURL() {
    return alertURL;
  }

  public void setAlertURL(String alertURL) {
    this.alertURL = alertURL;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CasesWithAlertURL casesWithAlertURL = (CasesWithAlertURL) o;
    return Objects.equals(this.iD, casesWithAlertURL.iD) &&
        Objects.equals(this.caseGroup, casesWithAlertURL.caseGroup) &&
        Objects.equals(this.caseType, casesWithAlertURL.caseType) &&
        Objects.equals(this.externalID, casesWithAlertURL.externalID) &&
        Objects.equals(this.externalIdSort, casesWithAlertURL.externalIdSort) &&
        Objects.equals(this.caseKey, casesWithAlertURL.caseKey) &&
        Objects.equals(this.keyLabel, casesWithAlertURL.keyLabel) &&
        Objects.equals(this.parentID, casesWithAlertURL.parentID) &&
        Objects.equals(this.supplementaryKey, casesWithAlertURL.supplementaryKey) &&
        Objects.equals(this.supplementaryType, casesWithAlertURL.supplementaryType) &&
        Objects.equals(this.flagKey, casesWithAlertURL.flagKey) &&
        Objects.equals(this.description, casesWithAlertURL.description) &&
        Objects.equals(this.createdBy, casesWithAlertURL.createdBy) &&
        Objects.equals(this.createdDateTime, casesWithAlertURL.createdDateTime) &&
        Objects.equals(this.modifiedBy, casesWithAlertURL.modifiedBy) &&
        Objects.equals(this.modifiedDateTime, casesWithAlertURL.modifiedDateTime) &&
        Objects.equals(this.assignedUser, casesWithAlertURL.assignedUser) &&
        Objects.equals(this.assignedBy, casesWithAlertURL.assignedBy) &&
        Objects.equals(this.assignedDateTime, casesWithAlertURL.assignedDateTime) &&
        Objects.equals(this.priority, casesWithAlertURL.priority) &&
        Objects.equals(this.permission, casesWithAlertURL.permission) &&
        Objects.equals(this.currentState, casesWithAlertURL.currentState) &&
        Objects.equals(this.derivedState, casesWithAlertURL.derivedState) &&
        Objects.equals(this.stateExpiry, casesWithAlertURL.stateExpiry) &&
        Objects.equals(this.stateChangeBy, casesWithAlertURL.stateChangeBy) &&
        Objects.equals(this.stateChangeDateTime, casesWithAlertURL.stateChangeDateTime) &&
        Objects.equals(this.sourceId, casesWithAlertURL.sourceId) &&
        Objects.equals(this.sourceName, casesWithAlertURL.sourceName) &&
        Objects.equals(this.caseMarker, casesWithAlertURL.caseMarker) &&
        Objects.equals(this.updatedBy, casesWithAlertURL.updatedBy) &&
        Objects.equals(this.updatedDateTime, casesWithAlertURL.updatedDateTime) &&
        Objects.equals(this.groupId, casesWithAlertURL.groupId) &&
        Objects.equals(this.groupLevel, casesWithAlertURL.groupLevel) &&
        Objects.equals(this.extendedAttribute1, casesWithAlertURL.extendedAttribute1) &&
        Objects.equals(this.extendedAttribute2, casesWithAlertURL.extendedAttribute2) &&
        Objects.equals(this.extendedAttribute3, casesWithAlertURL.extendedAttribute3) &&
        Objects.equals(this.extendedAttribute4, casesWithAlertURL.extendedAttribute4) &&
        Objects.equals(this.extendedAttribute5, casesWithAlertURL.extendedAttribute5) &&
        Objects.equals(this.extendedAttribute6, casesWithAlertURL.extendedAttribute6) &&
        Objects.equals(this.extendedAttribute7, casesWithAlertURL.extendedAttribute7) &&
        Objects.equals(this.extendedAttribute8, casesWithAlertURL.extendedAttribute8) &&
        Objects.equals(this.extendedAttribute9, casesWithAlertURL.extendedAttribute9) &&
        Objects.equals(this.extendedAttribute10, casesWithAlertURL.extendedAttribute10) &&
        Objects.equals(this.extendedAttribute11, casesWithAlertURL.extendedAttribute11) &&
        Objects.equals(this.extendedAttribute12, casesWithAlertURL.extendedAttribute12) &&
        Objects.equals(this.extendedAttribute13, casesWithAlertURL.extendedAttribute13) &&
        Objects.equals(this.alertURL, casesWithAlertURL.alertURL);
  }

  @Override
  public int hashCode() {
    return Objects.hash(iD, caseGroup, caseType, externalID, externalIdSort, caseKey, keyLabel, parentID, supplementaryKey, supplementaryType, flagKey, description, createdBy, createdDateTime, modifiedBy, modifiedDateTime, assignedUser, assignedBy, assignedDateTime, priority, permission, currentState, derivedState, stateExpiry, stateChangeBy, stateChangeDateTime, sourceId, sourceName, caseMarker, updatedBy, updatedDateTime, groupId, groupLevel, extendedAttribute1, extendedAttribute2, extendedAttribute3, extendedAttribute4, extendedAttribute5, extendedAttribute6, extendedAttribute7, extendedAttribute8, extendedAttribute9, extendedAttribute10, extendedAttribute11, extendedAttribute12, extendedAttribute13, alertURL);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CasesWithAlertURL {\n");
    
    sb.append("    iD: ").append(toIndentedString(iD)).append("\n");
    sb.append("    caseGroup: ").append(toIndentedString(caseGroup)).append("\n");
    sb.append("    caseType: ").append(toIndentedString(caseType)).append("\n");
    sb.append("    externalID: ").append(toIndentedString(externalID)).append("\n");
    sb.append("    externalIdSort: ").append(toIndentedString(externalIdSort)).append("\n");
    sb.append("    caseKey: ").append(toIndentedString(caseKey)).append("\n");
    sb.append("    keyLabel: ").append(toIndentedString(keyLabel)).append("\n");
    sb.append("    parentID: ").append(toIndentedString(parentID)).append("\n");
    sb.append("    supplementaryKey: ").append(toIndentedString(supplementaryKey)).append("\n");
    sb.append("    supplementaryType: ").append(toIndentedString(supplementaryType)).append("\n");
    sb.append("    flagKey: ").append(toIndentedString(flagKey)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    createdBy: ").append(toIndentedString(createdBy)).append("\n");
    sb.append("    createdDateTime: ").append(toIndentedString(createdDateTime)).append("\n");
    sb.append("    modifiedBy: ").append(toIndentedString(modifiedBy)).append("\n");
    sb.append("    modifiedDateTime: ").append(toIndentedString(modifiedDateTime)).append("\n");
    sb.append("    assignedUser: ").append(toIndentedString(assignedUser)).append("\n");
    sb.append("    assignedBy: ").append(toIndentedString(assignedBy)).append("\n");
    sb.append("    assignedDateTime: ").append(toIndentedString(assignedDateTime)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    permission: ").append(toIndentedString(permission)).append("\n");
    sb.append("    currentState: ").append(toIndentedString(currentState)).append("\n");
    sb.append("    derivedState: ").append(toIndentedString(derivedState)).append("\n");
    sb.append("    stateExpiry: ").append(toIndentedString(stateExpiry)).append("\n");
    sb.append("    stateChangeBy: ").append(toIndentedString(stateChangeBy)).append("\n");
    sb.append("    stateChangeDateTime: ").append(toIndentedString(stateChangeDateTime)).append("\n");
    sb.append("    sourceId: ").append(toIndentedString(sourceId)).append("\n");
    sb.append("    sourceName: ").append(toIndentedString(sourceName)).append("\n");
    sb.append("    caseMarker: ").append(toIndentedString(caseMarker)).append("\n");
    sb.append("    updatedBy: ").append(toIndentedString(updatedBy)).append("\n");
    sb.append("    updatedDateTime: ").append(toIndentedString(updatedDateTime)).append("\n");
    sb.append("    groupId: ").append(toIndentedString(groupId)).append("\n");
    sb.append("    groupLevel: ").append(toIndentedString(groupLevel)).append("\n");
    sb.append("    extendedAttribute1: ").append(toIndentedString(extendedAttribute1)).append("\n");
    sb.append("    extendedAttribute2: ").append(toIndentedString(extendedAttribute2)).append("\n");
    sb.append("    extendedAttribute3: ").append(toIndentedString(extendedAttribute3)).append("\n");
    sb.append("    extendedAttribute4: ").append(toIndentedString(extendedAttribute4)).append("\n");
    sb.append("    extendedAttribute5: ").append(toIndentedString(extendedAttribute5)).append("\n");
    sb.append("    extendedAttribute6: ").append(toIndentedString(extendedAttribute6)).append("\n");
    sb.append("    extendedAttribute7: ").append(toIndentedString(extendedAttribute7)).append("\n");
    sb.append("    extendedAttribute8: ").append(toIndentedString(extendedAttribute8)).append("\n");
    sb.append("    extendedAttribute9: ").append(toIndentedString(extendedAttribute9)).append("\n");
    sb.append("    extendedAttribute10: ").append(toIndentedString(extendedAttribute10)).append("\n");
    sb.append("    extendedAttribute11: ").append(toIndentedString(extendedAttribute11)).append("\n");
    sb.append("    extendedAttribute12: ").append(toIndentedString(extendedAttribute12)).append("\n");
    sb.append("    extendedAttribute13: ").append(toIndentedString(extendedAttribute13)).append("\n");
    sb.append("    alertURL: ").append(toIndentedString(alertURL)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
