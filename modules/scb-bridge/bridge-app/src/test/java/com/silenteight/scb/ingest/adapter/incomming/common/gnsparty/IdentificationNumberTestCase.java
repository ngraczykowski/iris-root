package com.silenteight.scb.ingest.adapter.incomming.common.gnsparty;

import lombok.Builder;
import lombok.Value;

import static org.testcontainers.shaded.org.apache.commons.lang3.StringUtils.defaultString;

@Builder
@Value
class IdentificationNumberTestCase {

  private static final String GROUP_TWO_TEMPLATE =
      "(CLOB) ECDDP~1008000330_ABC00349762_803f3b8b-2a15-439d-8ab6-1585756ad5f4~U~"
          + "Armir TAPTA~~~~~1~2~~~APPARTMENT 5/A WEST, SAVOY COURT,150 ROBINSON ROAD,MIDLEVELS,"
          + "SINGAPOUR~~~~~~~~~~~~IN~~~%s~%s~%s~%s~%s~%s~%s~%s~%s~%s~1985-09-24~~~~~HK~~M~~"
          + "Private Company~Middle Market~Middle Markets~ACTIVE~~2015-05-13~"
          + "13526370_ABC00349762_1~~~RELATED PARTY~U~|Authorised Signatory|Beneficial Owner"
          + "|Director|~~1257742~~~~~~~~~F~2011-04-10 14:40:35.107";
  private static final String GROUP_FIVE_TEMPLATE =
      "(CLOB) ICDD~1008000330_ABC00349762_803f3b8b-2a15-439d-8ab6-1585756ad5f4~U~"
          + "Armir TAPTA~~~~~1~2~~~APPARTMENT 5/A WEST, SAVOY COURT,150 ROBINSON ROAD,"
          + "MIDLEVELS,SINGAPOUR~~~~~~~~~~~~IN~~~%s~%s~%s~%s~%s~%s~%s~%s~%s~%s~"
          + "1985-09-24~~~~~HK~~M~~Private Company~Middle Market~Middle Markets~ACTIVE~~"
          + "2015-05-13~13526370_ABC00349762_1~~~RELATED PARTY~U~"
          + "|Authorised Signatory|Beneficial Owner|Director|~~1257742~~~~~~~~~F~"
          + "2011-04-10 14:40:35.107~~number2~~number3~~~~~~~~~~";

  String identificationType1;
  String identificationNumber1;
  String identificationType2;
  String identificationNumber2;
  String identificationType3;
  String identificationNumber3;
  String identificationType4;
  String identificationNumber4;
  String identificationTypeRest;
  String identificationNumberRest;

  GnsParty parseToGnsPartyTwo() {
    return parse("SCB_EDMP_DUED", formatGnsRecord(GROUP_TWO_TEMPLATE));
  }

  private static GnsParty parse(String fmtName, String record) {
    return RecordParser.parse("system_id", '~', fmtName, record);
  }

  private String formatGnsRecord(String template) {
    return String.format(
        template,
        defaultString(identificationType1), defaultString(identificationNumber1),
        defaultString(identificationType2), defaultString(identificationNumber2),
        defaultString(identificationType3), defaultString(identificationNumber3),
        defaultString(identificationType4), defaultString(identificationNumber4),
        defaultString(identificationTypeRest), defaultString(identificationNumberRest));
  }

  GnsParty parseToGnsPartyFive() {
    return parse("SCB_EDMP_DUED_V1", formatGnsRecord(GROUP_FIVE_TEMPLATE));
  }
}
