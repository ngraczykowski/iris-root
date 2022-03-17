package com.silenteight.scb.ingest.adapter.incomming.common.batch.ecm

import spock.lang.Specification

class ExternalIdTest extends Specification {

  def CORRECT_HIT_UNIQUE_ID = "329382_NAM_34"
  def WATCHLIST_ID = "329382"

  def "should extract from HIT_UNIQUE_ID WATCHLIST_ID"() {
    when:
    def result = ExternalId.tryToExtractWatchlistIdFromHitUniqueId(CORRECT_HIT_UNIQUE_ID)

    then:
    result == WATCHLIST_ID
  }

  def "should fail while extracting from incorrect hit_unique_id"() {
    when:
    ExternalId.tryToExtractWatchlistIdFromHitUniqueId(incorrectUniqueId)

    then:
    thrown(IllegalArgumentException)

    where:
    incorrectUniqueId << [
        null,
        '',
        "329382"
    ]
  }

}
