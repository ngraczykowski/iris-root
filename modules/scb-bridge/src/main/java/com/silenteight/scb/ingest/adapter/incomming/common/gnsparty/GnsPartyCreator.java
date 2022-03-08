package com.silenteight.scb.ingest.adapter.incomming.common.gnsparty;

interface GnsPartyCreator {

  boolean supports(String[] values);

  GnsParty create(String[] values);
}
