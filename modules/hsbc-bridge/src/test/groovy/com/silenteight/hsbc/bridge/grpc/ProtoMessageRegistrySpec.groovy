package com.silenteight.hsbc.bridge.grpc

import com.silenteight.adjudication.api.v1.RecommendationsGenerated
import com.silenteight.data.api.v1.ProductionDataIndexRequest

import com.google.protobuf.Any
import com.google.protobuf.Descriptors
import com.google.protobuf.Duration
import com.google.protobuf.Empty
import com.google.protobuf.Enum
import com.google.protobuf.Value
import spock.lang.Specification
import spock.lang.Unroll

class ProtoMessageRegistrySpec extends Specification {

  def underTest = new ProtoMessageRegistry()

  @Unroll
  def 'should find parsers for #typeName'() {
    when:
    def result = underTest.findParser(typeName)

    then:
    result.isPresent()

    where:
    typeName << [
        getName(Any.getDescriptor()),
        getName(Empty.getDescriptor()),
        getName(Enum.getDescriptor()),
        getName(ProductionDataIndexRequest.getDescriptor()),
        getName(RecommendationsGenerated.getDescriptor())
    ]
  }

  @Unroll
  def 'should not find parsers for unknown types like: #typeName'() {
    when:
    def result = underTest.findParser(typeName)

    then:
    !result.isPresent()

    where:
    typeName << [
        null,
        '',
        getName(Duration.getDescriptor()),
        getName(Value.getDescriptor())
    ]
  }

  def getName(Descriptors.Descriptor descriptor) {
    descriptor.getFullName()
  }
}
