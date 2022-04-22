package com.silenteight.fab.dataprep.domain.model;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import org.apache.commons.lang3.StringUtils;

@Value
@Builder
@Jacksonized
public class ParsedMessageData {

  public enum CustomerType {
    NO_DATA,
    DATA_SOURCE_ERROR,
    INDIVIDUAL,
    CORPORATE
  }

  String salutation;
  String name;
  String shortName;
  String customerType;
  String dob;
  String dateOfEstablishment;
  String gender;
  String swiftBic;
  String address1;
  String address2;
  String city;
  String state;
  String country;
  String countryOfIncorporation;
  String countryOfDomicile;
  String countryOfBirth;
  String customerSegment;
  String profession;
  String passportNum;
  String nationalId;
  String tradeLicPlaceOfIssue;
  String groupOrCompanyName;
  String source;
  String sourceSystemId;
  String customerNumber;
  String alternate;
  String latestCustomerNumber;
  String lastUpdateTime;

  public CustomerType getCustomerTypeAsEnum() {
    String type = getCustomerType();
    if (StringUtils.isBlank(type)) {
      return CustomerType.NO_DATA;
    } else if ("Individual".equalsIgnoreCase(type)) {
      return CustomerType.INDIVIDUAL;
    } else if ("Corporate".equalsIgnoreCase(type)) {
      return CustomerType.CORPORATE;
    } else {
      return CustomerType.DATA_SOURCE_ERROR;
    }
  }
}
