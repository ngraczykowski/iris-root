package com.silenteight.customerbridge.gnsrt.model.request;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@Validated
public class GnsRtScreenCustomerNameResInfoHeader {

  @JsonProperty("userBankID")
  @NotNull
  private String userBankID;

  @JsonProperty("screeningUniqueReference")
  private String screeningUniqueReference;

  @JsonProperty("sourceSystem")
  private String sourceSystem;

  @JsonProperty("watchlistTypeAll")
  private String watchlistTypeAll;

  @JsonProperty("watchlistTypeSanctions")
  private String watchlistTypeSanctions;

  @JsonProperty("watchlistTypePEP")
  private String watchlistTypePep;

  @JsonProperty("watchlistTypeAM")
  private String watchlistTypeAM;

  @JsonProperty("watchlistTypeDD")
  private String watchlistTypeDD;

  @JsonProperty("countryCode")
  private String countryCode;

  @JsonProperty("segment")
  private String segment;

  @JsonProperty("clientType")
  private String clientType;

  @JsonProperty("partyType")
  private String partyType;

  @JsonProperty("genericSearchFlag")
  private String genericSearchFlag;

  @JsonProperty("priorityScreeningFlag")
  private String priorityScreeningFlag;

  @JsonProperty("userDepartment")
  private String userDepartment;

  @JsonProperty("requestorOrGroupMailId")
  private String requestorOrGroupMailId;

  @JsonProperty("emailNotificationRequiredOrNot")
  private String emailNotificationRequiredOrNot;

  @JsonProperty("responseType")
  private String responseType;

  @JsonProperty("requestInTime")
  private String requestInTime;

  @JsonProperty("requestOutTime")
  private String requestOutTime;
}
