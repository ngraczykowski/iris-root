package com.silenteight.fab.dataprep.domain

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import spock.lang.Specification
import spock.lang.Unroll

import java.time.OffsetDateTime

class AlertRepositoryExtImplTest extends Specification {

  NamedParameterJdbcTemplate jdbcTemplate = Mock()

  def underTest = new AlertRepositoryExtImpl(jdbcTemplate)

  @Unroll
  def 'dates should be rounded to months #partitionDate'() {
    when:
    underTest.createPartition(OffsetDateTime.parse(partitionDate))

    then:
    1 * jdbcTemplate.execute(*_) >> {
      String sql = it.first()
      assert sql.contains(" FROM ('$rangeFrom') TO ('$rangeTo')")
      assert sql.contains(" dataprep_alert_$partitionName ")
    }

    where:
    partitionDate             | partitionName | rangeFrom    | rangeTo
    '2010-01-01T00:00:00.00Z' | '2010_01'     | '2010-01-01' | '2010-02-01'
    '2010-01-31T00:00:00.00Z' | '2010_01'     | '2010-01-01' | '2010-02-01'
    '2010-01-31T23:59:59.99Z' | '2010_01'     | '2010-01-01' | '2010-02-01'
    '2010-12-10T00:00:00.00Z' | '2010_12'     | '2010-12-01' | '2011-01-01'
    '2010-02-28T00:00:00.00Z' | '2010_02'     | '2010-02-01' | '2010-03-01'
  }
}
