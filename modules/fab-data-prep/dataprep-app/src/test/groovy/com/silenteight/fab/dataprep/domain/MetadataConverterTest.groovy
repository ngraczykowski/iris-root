package com.silenteight.fab.dataprep.domain

import spock.lang.Specification
import spock.lang.Unroll

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals
import static org.skyscreamer.jsonassert.JSONCompareMode.LENIENT

class MetadataConverterTest extends Specification {

  @Unroll
  def 'metadata should be created #systemId'() {
    expect:
    def result = MetadataConverter.getMetadata(systemId)
    assertEquals(metadata, result, LENIENT)

    where:
    systemId            | metadata
    'TRAINING!60C2ED1B' | '{"systemId": "TRAINING!60C2ED1B", "recordId": "60C2ED1B"}'
    '60C2ED1B'          | '{"systemId": "60C2ED1B", "recordId": "60C2ED1B"}'
    ''                  | '{"systemId": "", "recordId": ""}'
  }
}
