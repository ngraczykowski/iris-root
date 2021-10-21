package com.silenteight.payments.bridge.svb.oldetl.service.shitcode;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure;

@RequiredArgsConstructor
public class ExtractGtexAlertedPartyData {

  /* CASE 1
  :50K:/1234567890123
  NAME NAME NAME
  ADDRESS STREET ADDRESS
  US POSTCODE### ,US
  */

  /* CASE 2
  :50K:/1234567890123
  NAME NAME NAME
  ADDRESS STREET ADDRESS
  XXXXXX?
  /US/CITY CITY
  */

  /* CASE 3
  :50K:/1234567890123
  NAME NAME NAME
  ADDRESS STREET ADDRESS
  /US/POSTCODE### CITY CITY
  */

  /* CASE 4
  :59:/1234567890123
  NAME NAME NAME
  */

  /* CASE 5
  :59:/1234567890123
  NAME NAME NAME
  */

  /* CASE 6
  :59:/1234567890123
  NAME NAME NAME
  ADDRESS STREET ADDRESS, CITY CITY
  STATE, COUNTRY, POSTCODE###
  */

  /* CASE 7
  :50F:/1234567890123
  1/NAME NAME NAME
  1/NAME NAME NAME
  2/ADDRESS STREET ADDRESS
  3/US/POSTCODE### CITY CITY
  */

  private final MessageData messageData;
  private final String hitTag;

  public AlertedPartyData extract(MessageFieldStructure messageFieldStructure) {
    return null;
  }
}
