package com.silenteight.payments.bridge.svb.learning.reader.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
public class LearningCsvRow {

  @JsonProperty("FKCO_ID")
  private String fkcoId;

  @JsonProperty("FKCO_V_SYSTEM_ID")
  private String fkcoVSystemId;

  @JsonProperty("FKCO_V_FORMAT")
  private String fkcoVFormat;

  @JsonProperty("FKCO_V_TYPE")
  private String fkcoVType;

  @JsonProperty("FKCO_V_TRANSACTION_REF")
  private String fkcoVTransactionRef;

  @JsonProperty("FKCO_V_RELATED_REF")
  private String fkcoVRelatedRef;

  @JsonProperty("FKCO_V_SENS")
  private String fkcoVSens;

  @JsonProperty("FKCO_V_BUSINESS_UNIT")
  private String fkcoVBusinessUnit;

  @JsonProperty("FKCO_V_APPLICATION")
  private String fkcoVApplication;

  @JsonProperty("FKCO_V_CURRENCY")
  private String fkcoVCurrency;

  @JsonProperty("FKCO_F_AMOUNT")
  private String fkcoFAmount;

  @JsonProperty("FKCO_V_CONTENT")
  private String fkcoVContent;

  @JsonProperty("FKCO_B_HIGHLIGHT_ALL")
  private String fkcoBHighlightAll;

  @JsonProperty("FKCO_V_VALUE_DATE")
  private String fkcoVValueDate;

  @JsonProperty("FKCO_UNIT")
  private String fkcoUnit;

  @JsonProperty("FKCO_I_MSG_FML_PRIORITY")
  private String fkcoIMsgFmlPriority;

  @JsonProperty("FKCO_I_MSG_FML_CONFIDENTIALITY")
  private String fkcoIMsgFmlConfidentiality;

  @JsonProperty("FKCO_D_APP_DEADLINE")
  private String fkcoDAppDeadline;

  @JsonProperty("FKCO_I_APP_PRIORITY")
  private String fkcoIAppPriority;

  @JsonProperty("FKCO_I_NORMAMOUNT")
  private String fkcoINormamount;

  @JsonProperty("FKCO_V_MESSAGEID")
  private String fkcoVMessageid;

  @JsonProperty("FKCO_V_COPY_SERVICE")
  private String fkcoVCopyService;

  @JsonProperty("FKCO_V_ACTION_COMMENT")
  private String fkcoVActionComment;

  @JsonProperty("FKCO_ACTION_DATE")
  private String fkcoActionDate;

  @JsonProperty("FKCO_D_FILTERED_DATETIME")
  private String fkcoDFilteredDatetime;

  @JsonProperty("FKCO_D_ACTION_DATETIME")
  private String fkcoDActionDatetime;

  @JsonProperty("FKCO_OPERATOR")
  private String fkcoOperator;

  @JsonProperty("FKCO_STATUS")
  private String fkcoStatus;

  @JsonProperty("FKCO_I_TOTAL_ACTION")
  private String fkcoITotalAction;

  @JsonProperty("FKCO_UNIT_1")
  private String fkcoUnit1;

  @JsonProperty("FKCO_MESSAGES")
  private String fkcoMessages;

  @JsonProperty("FKCO_B_HIGHLIGHT_HIT")
  private String fkcoBHighlightHit;

  @JsonProperty("FKCO_V_NAME_MATCHED_TEXT")
  private String fkcoVNameMatchedText;

  @JsonProperty("FKCO_V_ADDRESS_MATCHED_TEXT")
  private String fkcoVAddressMatchedText;

  @JsonProperty("FKCO_V_CITY_MATCHED_TEXT")
  private String fkcoVCityMatchedText;

  @JsonProperty("FKCO_V_STATE_MATCHED_TEXT")
  private String fkcoVStateMatchedText;

  @JsonProperty("FKCO_V_COUNTRY_MATCHED_TEXT")
  private String fkcoVCountryMatchedText;

  @JsonProperty("FKCO_V_LIST_MATCHED_NAME")
  private String fkcoVListMatchedName;

  @JsonProperty("FKCO_V_FML_TYPE")
  private String fkcoVFmlType;

  @JsonProperty("FKCO_I_FML_PRIORITY")
  private String fkcoIFmlPriority;

  @JsonProperty("FKCO_I_FML_CONFIDENTIALITY")
  private String fkcoIFmlConfidentiality;

  @JsonProperty("FKCO_V_HIT_MATCH_LEVEL")
  private String fkcoVHitMatchLevel;

  @JsonProperty("FKCO_V_HIT_TYPE")
  private String fkcoVHitType;

  @JsonProperty("FKCO_I_NONBLOCKING")
  private String fkcoINonblocking;

  @JsonProperty("FKCO_I_BLOCKING")
  private String fkcoIBlocking;

  @JsonProperty("FKCO_LISTED_RECORD")
  private String fkcoListedRecord;

  @JsonProperty("FKCO_FILTERED_DATE")
  private String fkcoFilteredDate;

  @JsonProperty("FKCO_D_FILTERED_DATETIME_1")
  private String fkcoDFilteredDatetime1;

  @JsonProperty("FKCO_V_MATCHED_TAG")
  private String fkcoVMatchedTag;

  @JsonProperty("FKCO_V_MATCHED_TAG_CONTENT")
  private String fkcoVMatchedTagContent;

  @JsonProperty("FKCO_I_SEQUENCE")
  private String fkcoISequence;

  @JsonProperty("FKCO_UNIT_2")
  private String fkcoUnit2;

  @JsonProperty("FKCO_V_LIST_FMM_ID")
  private String fkcoVListFmmId;

  @JsonProperty("FKCO_V_LIST_OFFICIAL_REF")
  private String fkcoVListOfficialRef;

  @JsonProperty("FKCO_V_LIST_TYPE")
  private String fkcoVListType;

  @JsonProperty("FKCO_V_LIST_ORIGIN")
  private String fkcoVListOrigin;

  @JsonProperty("FKCO_V_LIST_DESIGNATION")
  private String fkcoVListDesignation;

  @JsonProperty("FKCO_V_LIST_PEP")
  private String fkcoVListPep;

  @JsonProperty("FKCO_V_LIST_FEP")
  private String fkcoVListFep;

  @JsonProperty("FKCO_V_LIST_NAME")
  private String fkcoVListName;

  @JsonProperty("FKCO_V_LIST_CITY")
  private String fkcoVListCity;

  @JsonProperty("FKCO_V_LIST_STATE")
  private String fkcoVListState;

  @JsonProperty("FKCO_V_LIST_COUNTRY")
  private String fkcoVListCountry;

  @JsonProperty("FKCO_V_LIST_USERDATA1")
  private String fkcoVListUserdata1;

  @JsonProperty("FKCO_V_LIST_USERDATA2")
  private String fkcoVListUserdata2;

  @JsonProperty("FKCO_V_LIST_KEYWORD")
  private String fkcoVListKeyword;

  @JsonProperty("FKCO_V_LIST_ADD_INFO")
  private String fkcoVListAddInfo;

  @JsonProperty("FKCO_V_STATUS_NAME")
  private String fkcoVStatusName;

  @JsonProperty("FKCO_V_STATUS_BEHAVIOR")
  private String fkcoVStatusBehavior;

  @JsonProperty("FKCO_MESSAGES_1")
  private String fkcoMessages1;

  @JsonProperty("FKCO_I_BLOCKINGHITS")
  private String fkcoIBlockinghits;

  public String getMatchId() {
    return getFkcoVListFmmId() + "(" + getFkcoVMatchedTag() + ", #" + getFkcoISequence()
        + ")";
  }
}
