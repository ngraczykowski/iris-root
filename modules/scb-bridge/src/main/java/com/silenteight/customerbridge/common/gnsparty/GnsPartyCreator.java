package com.silenteight.customerbridge.common.gnsparty;

interface GnsPartyCreator {

  boolean supports(String[] values);

  GnsParty create(String[] values);
}
