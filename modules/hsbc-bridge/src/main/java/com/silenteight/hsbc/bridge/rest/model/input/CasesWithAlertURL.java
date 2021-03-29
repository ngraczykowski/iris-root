package com.silenteight.hsbc.bridge.rest.model.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;


@javax.annotation.Generated(
    value = "io.swagger.codegen.v3.generators.java.JavaResteasyServerCodegen",
    date = "2021-03-05T14:11:51.641Z[GMT]")
public class CasesWithAlertURL {

  private int id;
  private String caseGroup = null;
  private String caseType = null;
  private String externalId = null;
  private String externalIdSort = null;
  private String caseKey = null;
  private String keyLabel = null;
  private Integer parentId = null;
  private String supplementaryKey = null;
  private String supplementaryType = null;
  private String flagKey = null;
  private String description = null;
  private Integer createdBy = null;
  private String createdDateTime = null;
  private Integer modifiedBy = null;
  private String modifiedDateTime = null;
  private Integer assignedUser = null;
  private Integer assignedBy = null;
  private String assignedDateTime = null;
  private Integer priority = null;
  private String permission = null;
  private String currentState = null;
  private String derivedState = null;
  private String stateExpiry = null;
  private Integer stateChangeBy = null;
  private String stateChangeDateTime = null;
  private Integer sourceId = null;
  private String sourceName = null;
  private Integer caseMarker = null;
  private Integer updatedBy = null;
  private String updatedDateTime = null;
  private String groupId = null;
  private Integer groupLevel = null;
  private Integer extendedAttribute1 = null;
  private Integer extendedAttribute2 = null;
  private String extendedAttribute3 = null;
  private String extendedAttribute4 = null;
  private String extendedAttribute5 = null;
  private String extendedAttribute6 = null;
  private String extendedAttribute7 = null;
  private String extendedAttribute8 = null;
  private String extendedAttribute9 = null;
  private String extendedAttribute10 = null;
  private String extendedAttribute11 = null;
  private String extendedAttribute12 = null;
  private Integer extendedAttribute13 = null;
  private String alertUrl = null;

  /**
   * Internal unique Identifier assigned to the Case or Alert within Case Management . Used to link
   * to other tables.
   **/

  @Schema(description = "Internal unique Identifier assigned to the Case or Alert within Case Management . Used to link to other tables.")
  @JsonProperty("id")
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * Records an associated group to the Case or Alert, for example&amp;#58; Match
   **/

  @Schema(description = "Records an associated group to the Case or Alert, for example&#58; Match")
  @JsonProperty("caseGroup")
  public String getCaseGroup() {
    return caseGroup;
  }

  public void setCaseGroup(String caseGroup) {
    this.caseGroup = caseGroup;
  }

  /**
   * Denotes if this is a Case (\&quot;Case\&quot;) or an Alert (\&quot;Issue\&quot;)
   **/

  @Schema(description = "Denotes if this is a Case (\"Case\") or an Alert (\"Issue\")")
  @JsonProperty("caseType")
  public String getCaseType() {
    return caseType;
  }

  public void setCaseType(String caseType) {
    this.caseType = caseType;
  }

  /**
   * A unique ID of the alerts as it appears in Case Management.
   **/

  @Schema(description = "A unique ID of the alerts as it appears in Case Management.")
  @JsonProperty("externalId")
  public String getExternalId() {
    return externalId;
  }

  public void setExternalId(String externalId) {
    this.externalId = externalId;
  }

  /**
   * A unique ID of the alert that is used in reporting. The value of the code is padded with zeros
   **/

  @Schema(description = "A unique ID of the alert that is used in reporting. The value of the code is padded with zeros")
  @JsonProperty("externalIdSort")
  public String getExternalIdSort() {
    return externalIdSort;
  }

  public void setExternalIdSort(String externalIdSort) {
    this.externalIdSort = externalIdSort;
  }

  /**
   * Unique internal Hash which identifies an alert.
   **/

  @Schema(description = "Unique internal Hash which identifies an alert.")
  @JsonProperty("caseKey")
  public String getCaseKey() {
    return caseKey;
  }

  public void setCaseKey(String caseKey) {
    this.caseKey = caseKey;
  }

  /**
   * A Unique Identifier for the Case or Alert, composed of \&quot;Customer ID&amp;#58; Watchlist
   * Alert Generated Against&amp;#58; Watch Person ID\&quot;
   **/

  @Schema(description = "A Unique Identifier for the Case or Alert, composed of \"Customer ID&#58; Watchlist Alert Generated Against&#58; Watch Person ID\"")
  @JsonProperty("keyLabel")
  public String getKeyLabel() {
    return keyLabel;
  }

  public void setKeyLabel(String keyLabel) {
    this.keyLabel = keyLabel;
  }

  /**
   * ID number for the parent Case of the Alert. If viewing at Case Level; the field will display
   * \&quot;-1\&quot;
   **/

  @Schema(description = "ID number for the parent Case of the Alert. If viewing at Case Level; the field will display \"-1\"")
  @JsonProperty("parentId")
  public Integer getParentId() {
    return parentId;
  }

  public void setParentId(Integer parentId) {
    this.parentId = parentId;
  }

  /**
   * Hash of the supplementary data held against an alert, used by the system to identify if any
   * supplementary data has changed.
   **/

  @Schema(description = "Hash of the supplementary data held against an alert, used by the system to identify if any supplementary data has changed.")
  @JsonProperty("supplementaryKey")
  public String getSupplementaryKey() {
    return supplementaryKey;
  }

  public void setSupplementaryKey(String supplementaryKey) {
    this.supplementaryKey = supplementaryKey;
  }

  /**
   * Identifies and provides grouping by type of supplementary data
   **/

  @Schema(description = "Identifies and provides grouping by type of supplementary data")
  @JsonProperty("supplementaryType")
  public String getSupplementaryType() {
    return supplementaryType;
  }

  public void setSupplementaryType(String supplementaryType) {
    this.supplementaryType = supplementaryType;
  }

  /**
   * Hash of the flag key data held against an alert, used by the system to identify if any flag key
   * data has changed. If a change is identified the caseMaker is updated.
   **/

  @Schema(description = "Hash of the flag key data held against an alert, used by the system to identify if any flag key data has changed. If a change is identified the caseMaker is updated.")
  @JsonProperty("flagKey")
  public String getFlagKey() {
    return flagKey;
  }

  public void setFlagKey(String flagKey) {
    this.flagKey = flagKey;
  }

  /**
   * Name of the Customer when initially provided or if updated by a user.
   **/

  @Schema(description = "Name of the Customer when initially provided or if updated by a user.")
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Records how the Case was created, for example&amp;#58; Batch, Manual or Real-Time Screening
   **/

  @Schema(description = "Records how the Case was created, for example&#58; Batch, Manual or Real-Time Screening")
  @JsonProperty("createdBy")
  public Integer getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(Integer createdBy) {
    this.createdBy = createdBy;
  }

  /**
   * Date the Case or Alert was created
   **/

  @Schema(description = "Date the Case or Alert was created")
  @JsonProperty("createdDateTime")
  public String getCreatedDateTime() {
    return createdDateTime;
  }

  public void setCreatedDateTime(String createdDateTime) {
    this.createdDateTime = createdDateTime;
  }

  /**
   * Last User to modify the Case or Alert. Changes to the Case or Alert State, Comments or
   * Attachments constitute a modification
   **/

  @Schema(description = "Last User to modify the Case or Alert. Changes to the Case or Alert State, Comments or Attachments constitute a modification")
  @JsonProperty("modifiedBy")
  public Integer getModifiedBy() {
    return modifiedBy;
  }

  public void setModifiedBy(Integer modifiedBy) {
    this.modifiedBy = modifiedBy;
  }

  /**
   * Date and time of the modification
   **/

  @Schema(description = "Date and time of the modification")
  @JsonProperty("modifiedDateTime")
  public String getModifiedDateTime() {
    return modifiedDateTime;
  }

  public void setModifiedDateTime(String modifiedDateTime) {
    this.modifiedDateTime = modifiedDateTime;
  }

  /**
   * ID of the user to whom the Case or Alert is assigned
   **/

  @Schema(description = "ID of the user to whom the Case or Alert is assigned")
  @JsonProperty("assignedUser")
  public Integer getAssignedUser() {
    return assignedUser;
  }

  public void setAssignedUser(Integer assignedUser) {
    this.assignedUser = assignedUser;
  }

  /**
   * ID of the user who assigned the Case or Alert to the User
   **/

  @Schema(description = "ID of the user who assigned the Case or Alert to the User")
  @JsonProperty("assignedBy")
  public Integer getAssignedBy() {
    return assignedBy;
  }

  public void setAssignedBy(Integer assignedBy) {
    this.assignedBy = assignedBy;
  }

  /**
   * Date and time the Case or Alert was assigned
   **/

  @Schema(description = "Date and time the Case or Alert was assigned")
  @JsonProperty("assignedDateTime")
  public String getAssignedDateTime() {
    return assignedDateTime;
  }

  public void setAssignedDateTime(String assignedDateTime) {
    this.assignedDateTime = assignedDateTime;
  }

  /**
   * Records the alert priority level (250&#x3D;Low, 500&#x3D;Medium, 750&#x3D;High)
   **/

  @Schema(description = "Records the alert priority level (250=Low, 500=Medium, 750=High)")
  @JsonProperty("priority")
  public Integer getPriority() {
    return priority;
  }

  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  /**
   * Permission assigned to the Case or Alert
   **/

  @Schema(description = "Permission assigned to the Case or Alert")
  @JsonProperty("permission")
  public String getPermission() {
    return permission;
  }

  public void setPermission(String permission) {
    this.permission = permission;
  }

  /**
   * Workflow state in which the Alert currently resides, for example&amp;#58; Level 1 Review
   **/

  @Schema(description = "Workflow state in which the Alert currently resides, for example&#58; Level 1 Review")
  @JsonProperty("currentState")
  public String getCurrentState() {
    return currentState;
  }

  public void setCurrentState(String currentState) {
    this.currentState = currentState;
  }

  /**
   * Workflow state in which the Case currently resides. Case state is controlled by the system and
   * is derived from the activity associated with the Alerts within the Case. A Case cannot be
   * closed until all of the Alerts within it have been closed.
   **/

  @Schema(description = "Workflow state in which the Case currently resides. Case state is controlled by the system and is derived from the activity associated with the Alerts within the Case. A Case cannot be closed until all of the Alerts within it have been closed.")
  @JsonProperty("derivedState")
  public String getDerivedState() {
    return derivedState;
  }

  public void setDerivedState(String derivedState) {
    this.derivedState = derivedState;
  }

  /**
   * Date when the current state of the Alert will expire
   **/

  @Schema(description = "Date when the current state of the Alert will expire")
  @JsonProperty("stateExpiry")
  public String getStateExpiry() {
    return stateExpiry;
  }

  public void setStateExpiry(String stateExpiry) {
    this.stateExpiry = stateExpiry;
  }

  /**
   * Which User undertook the last transition of the Alert through the Workflow
   **/

  @Schema(description = "Which User undertook the last transition of the Alert through the Workflow")
  @JsonProperty("stateChangeBy")
  public Integer getStateChangeBy() {
    return stateChangeBy;
  }

  public void setStateChangeBy(Integer stateChangeBy) {
    this.stateChangeBy = stateChangeBy;
  }

  /**
   * Date and time the state of the Case or Alert was changed
   **/

  @Schema(description = "Date and time the state of the Case or Alert was changed")
  @JsonProperty("stateChangeDateTime")
  public String getStateChangeDateTime() {
    return stateChangeDateTime;
  }

  public void setStateChangeDateTime(String stateChangeDateTime) {
    this.stateChangeDateTime = stateChangeDateTime;
  }

  /**
   * Case Source System ID
   **/

  @Schema(description = "Case Source System ID")
  @JsonProperty("sourceId")
  public Integer getSourceId() {
    return sourceId;
  }

  public void setSourceId(Integer sourceId) {
    this.sourceId = sourceId;
  }

  /**
   * Case Source System Name
   **/

  @Schema(description = "Case Source System Name")
  @JsonProperty("sourceName")
  public String getSourceName() {
    return sourceName;
  }

  public void setSourceName(String sourceName) {
    this.sourceName = sourceName;
  }

  /**
   * this is the review flag which if  indicates if customer data in any of the review flag fields
   * has been updated. This is used to signify a change or trigger a realert. 0 indicates no review
   * flag set.
   **/

  @Schema(description = "this is the review flag which if  indicates if customer data in any of the review flag fields has been updated. This is used to signify a change or trigger a realert. 0 indicates no review flag set.")
  @JsonProperty("caseMarker")
  public Integer getCaseMarker() {
    return caseMarker;
  }

  public void setCaseMarker(Integer caseMarker) {
    this.caseMarker = caseMarker;
  }

  /**
   * User ID of the account that updated the case.
   **/

  @Schema(description = "User ID of the account that updated the case.")
  @JsonProperty("updatedBy")
  public Integer getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(Integer updatedBy) {
    this.updatedBy = updatedBy;
  }

  /**
   * Date and time the alert was updated.
   **/

  @Schema(description = "Date and time the alert was updated.")
  @JsonProperty("updatedDateTime")
  public String getUpdatedDateTime() {
    return updatedDateTime;
  }

  public void setUpdatedDateTime(String updatedDateTime) {
    this.updatedDateTime = updatedDateTime;
  }

  /**
   * ID number of the associated permission or grouping to which the Case or Alert belongs, for
   * example&amp;#58; Country or Regional Group.
   **/

  @Schema(description = "ID number of the associated permission or grouping to which the Case or Alert belongs, for example&#58; Country or Regional Group.")
  @JsonProperty("groupId")
  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  /**
   * Where the group sits within the permission or grouping structure
   **/

  @Schema(description = "Where the group sits within the permission or grouping structure")
  @JsonProperty("groupLevel")
  public Integer getGroupLevel() {
    return groupLevel;
  }

  public void setGroupLevel(Integer groupLevel) {
    this.groupLevel = groupLevel;
  }

  /**
   * Escalation. Indicates if the alert has been escalated.
   **/

  @Schema(description = "Escalation. Indicates if the alert has been escalated.")
  @JsonProperty("extendedAttribute1")
  public Integer getExtendedAttribute1() {
    return extendedAttribute1;
  }

  public void setExtendedAttribute1(Integer extendedAttribute1) {
    this.extendedAttribute1 = extendedAttribute1;
  }

  /**
   * Priority Score. Indicates how accurate the match is between the customer and the list record
   * scored 0 to 100. It is not a percentage.
   **/

  @Schema(description = "Priority Score. Indicates how accurate the match is between the customer and the list record scored 0 to 100. It is not a percentage.")
  @JsonProperty("extendedAttribute2")
  public Integer getExtendedAttribute2() {
    return extendedAttribute2;
  }

  public void setExtendedAttribute2(Integer extendedAttribute2) {
    this.extendedAttribute2 = extendedAttribute2;
  }

  /**
   * Risk Score. Indicates, if provided with the list data, how much of a risk the individual or
   * entity is. 0 to 100
   **/

  @Schema(description = "Risk Score. Indicates, if provided with the list data, how much of a risk the individual or entity is. 0 to 100")
  @JsonProperty("extendedAttribute3")
  public String getExtendedAttribute3() {
    return extendedAttribute3;
  }

  public void setExtendedAttribute3(String extendedAttribute3) {
    this.extendedAttribute3 = extendedAttribute3;
  }

  /**
   * PEP Risk Score. Indicates, if provided with the list data, how much of a risk the individual or
   * entity is. 0 to 100
   **/

  @Schema(description = "PEP Risk Score. Indicates, if provided with the list data, how much of a risk the individual or entity is. 0 to 100")
  @JsonProperty("extendedAttribute4")
  public String getExtendedAttribute4() {
    return extendedAttribute4;
  }

  public void setExtendedAttribute4(String extendedAttribute4) {
    this.extendedAttribute4 = extendedAttribute4;
  }

  /**
   * List Record Type. Describes the type of list record the match is against.
   **/

  @Schema(description = "List Record Type. Describes the type of list record the match is against.")
  @JsonProperty("extendedAttribute5")
  public String getExtendedAttribute5() {
    return extendedAttribute5;
  }

  public void setExtendedAttribute5(String extendedAttribute5) {
    this.extendedAttribute5 = extendedAttribute5;
  }

  /**
   * Screening Mode. Indicate the type of screening that created the alert.
   **/

  @Schema(description = "Screening Mode. Indicate the type of screening that created the alert.")
  @JsonProperty("extendedAttribute6")
  public String getExtendedAttribute6() {
    return extendedAttribute6;
  }

  public void setExtendedAttribute6(String extendedAttribute6) {
    this.extendedAttribute6 = extendedAttribute6;
  }

  /**
   * is the QA Flag which indicates if an alert has been subject to a QA review.
   **/

  @Schema(description = "is the QA Flag which indicates if an alert has been subject to a QA review.")
  @JsonProperty("extendedAttribute7")
  public String getExtendedAttribute7() {
    return extendedAttribute7;
  }

  public void setExtendedAttribute7(String extendedAttribute7) {
    this.extendedAttribute7 = extendedAttribute7;
  }

  /**
   * Day 1 Spike Flag. Indicates if an alert was created on day 1. \&quot;Day 1 Spike\&quot; &#x3D;
   * created on Day 1, \&quot;BAU\&quot; &#x3D; createdon Day 1 but has ben work to closure.
   **/

  @Schema(description = "Day 1 Spike Flag. Indicates if an alert was created on day 1. \"Day 1 Spike\" = created on Day 1, \"BAU\" = createdon Day 1 but has ben work to closure.")
  @JsonProperty("extendedAttribute8")
  public String getExtendedAttribute8() {
    return extendedAttribute8;
  }

  public void setExtendedAttribute8(String extendedAttribute8) {
    this.extendedAttribute8 = extendedAttribute8;
  }

  /**
   * BAU Spike Flag. Indicates if the alert was created as part of an influx of alert i.e. a spike,
   * after day 1. \&quot;Yes\&quot; indicates this is a BAU spike
   **/

  @Schema(description = "BAU Spike Flag. Indicates if the alert was created as part of an influx of alert i.e. a spike, after day 1. \"Yes\" indicates this is a BAU spike")
  @JsonProperty("extendedAttribute9")
  public String getExtendedAttribute9() {
    return extendedAttribute9;
  }

  public void setExtendedAttribute9(String extendedAttribute9) {
    this.extendedAttribute9 = extendedAttribute9;
  }

  /**
   * OWS Watchlist Name, describe the specific type of list the matching record came from.
   **/

  @Schema(description = "OWS Watchlist Name, describe the specific type of list the matching record came from.")
  @JsonProperty("extendedAttribute10")
  public String getExtendedAttribute10() {
    return extendedAttribute10;
  }

  public void setExtendedAttribute10(String extendedAttribute10) {
    this.extendedAttribute10 = extendedAttribute10;
  }

  /**
   * Line of Business, describes the Line of business the alert is related to.
   **/

  @Schema(description = "Line of Business, describes the Line of business the alert is related to.")
  @JsonProperty("extendedAttribute11")
  public String getExtendedAttribute11() {
    return extendedAttribute11;
  }

  public void setExtendedAttribute11(String extendedAttribute11) {
    this.extendedAttribute11 = extendedAttribute11;
  }

  /**
   * HSBC priority Flag, indicates the HSBC priority of an alert.
   **/

  @Schema(description = "HSBC priority Flag, indicates the HSBC priority of an alert.")
  @JsonProperty("extendedAttribute12")
  public String getExtendedAttribute12() {
    return extendedAttribute12;
  }

  public void setExtendedAttribute12(String extendedAttribute12) {
    this.extendedAttribute12 = extendedAttribute12;
  }

  /**
   * SLA Flag, a marker to define when an alert was created, closed or re-opened
   **/

  @Schema(description = "SLA Flag, a marker to define when an alert was created, closed or re-opened")
  @JsonProperty("extendedAttribute13")
  public Integer getExtendedAttribute13() {
    return extendedAttribute13;
  }

  public void setExtendedAttribute13(Integer extendedAttribute13) {
    this.extendedAttribute13 = extendedAttribute13;
  }

  /**
   * URL, which take you directly to the system where this alert resides and open the alert.
   **/

  @Schema(description = "URL, which take you directly to the system where this alert resides and open the alert.")
  @JsonProperty("alertUrl")
  public String getAlertUrl() {
    return alertUrl;
  }

  public void setAlertUrl(String alertUrl) {
    this.alertUrl = alertUrl;
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
    return Objects.equals(id, casesWithAlertURL.id) &&
        Objects.equals(caseGroup, casesWithAlertURL.caseGroup) &&
        Objects.equals(caseType, casesWithAlertURL.caseType) &&
        Objects.equals(externalId, casesWithAlertURL.externalId) &&
        Objects.equals(externalIdSort, casesWithAlertURL.externalIdSort) &&
        Objects.equals(caseKey, casesWithAlertURL.caseKey) &&
        Objects.equals(keyLabel, casesWithAlertURL.keyLabel) &&
        Objects.equals(parentId, casesWithAlertURL.parentId) &&
        Objects.equals(supplementaryKey, casesWithAlertURL.supplementaryKey) &&
        Objects.equals(supplementaryType, casesWithAlertURL.supplementaryType) &&
        Objects.equals(flagKey, casesWithAlertURL.flagKey) &&
        Objects.equals(description, casesWithAlertURL.description) &&
        Objects.equals(createdBy, casesWithAlertURL.createdBy) &&
        Objects.equals(createdDateTime, casesWithAlertURL.createdDateTime) &&
        Objects.equals(modifiedBy, casesWithAlertURL.modifiedBy) &&
        Objects.equals(modifiedDateTime, casesWithAlertURL.modifiedDateTime) &&
        Objects.equals(assignedUser, casesWithAlertURL.assignedUser) &&
        Objects.equals(assignedBy, casesWithAlertURL.assignedBy) &&
        Objects.equals(assignedDateTime, casesWithAlertURL.assignedDateTime) &&
        Objects.equals(priority, casesWithAlertURL.priority) &&
        Objects.equals(permission, casesWithAlertURL.permission) &&
        Objects.equals(currentState, casesWithAlertURL.currentState) &&
        Objects.equals(derivedState, casesWithAlertURL.derivedState) &&
        Objects.equals(stateExpiry, casesWithAlertURL.stateExpiry) &&
        Objects.equals(stateChangeBy, casesWithAlertURL.stateChangeBy) &&
        Objects.equals(stateChangeDateTime, casesWithAlertURL.stateChangeDateTime) &&
        Objects.equals(sourceId, casesWithAlertURL.sourceId) &&
        Objects.equals(sourceName, casesWithAlertURL.sourceName) &&
        Objects.equals(caseMarker, casesWithAlertURL.caseMarker) &&
        Objects.equals(updatedBy, casesWithAlertURL.updatedBy) &&
        Objects.equals(updatedDateTime, casesWithAlertURL.updatedDateTime) &&
        Objects.equals(groupId, casesWithAlertURL.groupId) &&
        Objects.equals(groupLevel, casesWithAlertURL.groupLevel) &&
        Objects.equals(extendedAttribute1, casesWithAlertURL.extendedAttribute1) &&
        Objects.equals(extendedAttribute2, casesWithAlertURL.extendedAttribute2) &&
        Objects.equals(extendedAttribute3, casesWithAlertURL.extendedAttribute3) &&
        Objects.equals(extendedAttribute4, casesWithAlertURL.extendedAttribute4) &&
        Objects.equals(extendedAttribute5, casesWithAlertURL.extendedAttribute5) &&
        Objects.equals(extendedAttribute6, casesWithAlertURL.extendedAttribute6) &&
        Objects.equals(extendedAttribute7, casesWithAlertURL.extendedAttribute7) &&
        Objects.equals(extendedAttribute8, casesWithAlertURL.extendedAttribute8) &&
        Objects.equals(extendedAttribute9, casesWithAlertURL.extendedAttribute9) &&
        Objects.equals(extendedAttribute10, casesWithAlertURL.extendedAttribute10) &&
        Objects.equals(extendedAttribute11, casesWithAlertURL.extendedAttribute11) &&
        Objects.equals(extendedAttribute12, casesWithAlertURL.extendedAttribute12) &&
        Objects.equals(extendedAttribute13, casesWithAlertURL.extendedAttribute13) &&
        Objects.equals(alertUrl, casesWithAlertURL.alertUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        id, caseGroup, caseType, externalId, externalIdSort, caseKey, keyLabel, parentId,
        supplementaryKey, supplementaryType, flagKey, description, createdBy, createdDateTime,
        modifiedBy, modifiedDateTime, assignedUser, assignedBy, assignedDateTime, priority,
        permission, currentState, derivedState, stateExpiry, stateChangeBy, stateChangeDateTime,
        sourceId, sourceName, caseMarker, updatedBy, updatedDateTime, groupId, groupLevel,
        extendedAttribute1, extendedAttribute2, extendedAttribute3, extendedAttribute4,
        extendedAttribute5, extendedAttribute6, extendedAttribute7, extendedAttribute8,
        extendedAttribute9, extendedAttribute10, extendedAttribute11, extendedAttribute12,
        extendedAttribute13, alertUrl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CasesWithAlertURL {\n");

    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    caseGroup: ").append(toIndentedString(caseGroup)).append("\n");
    sb.append("    caseType: ").append(toIndentedString(caseType)).append("\n");
    sb.append("    externalId: ").append(toIndentedString(externalId)).append("\n");
    sb.append("    externalIdSort: ").append(toIndentedString(externalIdSort)).append("\n");
    sb.append("    caseKey: ").append(toIndentedString(caseKey)).append("\n");
    sb.append("    keyLabel: ").append(toIndentedString(keyLabel)).append("\n");
    sb.append("    parentId: ").append(toIndentedString(parentId)).append("\n");
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
    sb
        .append("    stateChangeDateTime: ")
        .append(toIndentedString(stateChangeDateTime))
        .append("\n");
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
    sb
        .append("    extendedAttribute10: ")
        .append(toIndentedString(extendedAttribute10))
        .append("\n");
    sb
        .append("    extendedAttribute11: ")
        .append(toIndentedString(extendedAttribute11))
        .append("\n");
    sb
        .append("    extendedAttribute12: ")
        .append(toIndentedString(extendedAttribute12))
        .append("\n");
    sb
        .append("    extendedAttribute13: ")
        .append(toIndentedString(extendedAttribute13))
        .append("\n");
    sb.append("    alertUrl: ").append(toIndentedString(alertUrl)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces (except the first
   * line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
