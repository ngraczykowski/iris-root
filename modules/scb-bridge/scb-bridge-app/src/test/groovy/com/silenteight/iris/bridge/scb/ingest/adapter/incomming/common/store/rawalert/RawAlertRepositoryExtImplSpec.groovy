/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.store.rawalert

import groovy.util.logging.Slf4j
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import java.time.OffsetDateTime
import javax.persistence.EntityManager
import javax.persistence.Query

@Slf4j
class RawAlertRepositoryExtImplSpec extends Specification {

  def entityManager = Mock(EntityManager)

  @Subject
  def underTest = new RawAlertRepositoryExtImpl(entityManager)

  @Unroll
  def 'dates should be rounded to months #partitionDate'() {
    when:
    underTest.createPartition(OffsetDateTime.parse(partitionDate))

    then:
    1 * entityManager.createNativeQuery(
        {String sql ->
          assert sql.contains(" FROM ('$rangeFrom') TO ('$rangeTo')")
          assert sql.contains(" raw_alert_$partitionName ")
        }) >> Mock(Query)

    where:
    partitionDate             | partitionName | rangeFrom    | rangeTo
    '2010-01-01T00:00:00.00Z' | '2010_01'     | '2010-01-01' | '2010-02-01'
    '2010-01-31T00:00:00.00Z' | '2010_01'     | '2010-01-01' | '2010-02-01'
    '2010-01-31T23:59:59.99Z' | '2010_01'     | '2010-01-01' | '2010-02-01'
    '2010-12-10T00:00:00.00Z' | '2010_12'     | '2010-12-01' | '2011-01-01'
    '2010-02-28T00:00:00.00Z' | '2010_02'     | '2010-02-01' | '2010-03-01'
  }

  def 'should remove expired partitions'() {
    given:
    def date = '2021-11-12T00:00+02:00'
    def expiredDate = OffsetDateTime.parse(date)

    when:
    underTest.removeExpiredPartitions(expiredDate)

    then:
    1 * entityManager.createNativeQuery(
        {String sql ->
          assert sql
              .contains("CALL drop_expired_partitions('scb_raw_alert', 'raw_alert_', '2021-11-01')")
        }) >> Mock(Query)
  }
}
