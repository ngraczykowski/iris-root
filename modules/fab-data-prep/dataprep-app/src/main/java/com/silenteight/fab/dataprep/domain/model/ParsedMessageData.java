package com.silenteight.fab.dataprep.domain.model;

import lombok.Builder;
import lombok.Value;

import org.apache.commons.lang3.StringUtils;

@Value
@Builder
public class ParsedMessageData {

  public enum CustomerType {
    ENTITY_TYPE_UNSPECIFIED,
    INDIVIDUAL,
    ORGANIZATION
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
  String national;
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
      return CustomerType.ENTITY_TYPE_UNSPECIFIED;
    } else if ("I".equalsIgnoreCase(type)) {
      return CustomerType.INDIVIDUAL;
    } else {
      return CustomerType.ORGANIZATION;
    }
  }
}
