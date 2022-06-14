package com.silenteight.fab.dataprep.domain

import org.springframework.test.util.JsonExpectationsHelper
import spock.lang.Specification
import spock.lang.Unroll

class MetadataConverterTest extends Specification {

  JsonExpectationsHelper jsonExpectationsHelper = new JsonExpectationsHelper()

  @Unroll
  def 'metadata should be created #systemId'() {
    expect:
    def result = MetadataConverter.getMetadata(systemId)
    jsonExpectationsHelper.assertJsonEqual(metadata, result)

    where:
    systemId            | metadata
    'TRAINING!60C2ED1B' | '{"systemId": "TRAINING!60C2ED1B", "recordId": "60C2ED1B"}'
    '60C2ED1B'          | '{"systemId": "60C2ED1B", "recordId": "60C2ED1B"}'
    ''                  | '{"systemId": "", "recordId": ""}'
  }
}
