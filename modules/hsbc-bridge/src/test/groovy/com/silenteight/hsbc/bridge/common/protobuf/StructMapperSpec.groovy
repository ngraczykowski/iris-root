package com.silenteight.hsbc.bridge.common.protobuf

import com.google.protobuf.ListValue
import com.google.protobuf.NullValue
import com.google.protobuf.Struct
import com.google.protobuf.Value
import spock.lang.Specification

class StructMapperSpec extends Specification {

  def 'should map struct to flatten map of strings'() {
    given:
    def struct = Struct.newBuilder()
        .putAllFields(
            [
                'stringKey': Value.newBuilder().setStringValue('string').build(),
                'boolKey'  : Value.newBuilder().setBoolValue(true).build(),
                'numberKey': Value.newBuilder().setNumberValue(0.6).build(),
                'nullKey'  : Value.newBuilder().setNullValue(NullValue.NULL_VALUE).build(),
                'listKey'  : Value.newBuilder().setListValue(
                    ListValue.newBuilder()
                        .addAllValues(
                            [
                                Value.newBuilder().setStringValue('listString').build(),
                                Value.newBuilder().setListValue(
                                    ListValue.newBuilder().addValues(
                                        Value.newBuilder().
                                            setStringValue('anotherListValue').
                                            build())
                                        .build())
                                    .build()
                            ]).build())
                    .build(),
                'structKey': Value.newBuilder().setStructValue(
                    Struct.newBuilder().putFields(
                        'structNested', Value.newBuilder()
                        .setListValue(
                            ListValue.newBuilder()
                                .addValues(
                                    Value.newBuilder().setStringValue('nestedString').build())
                                .build())
                        .build())
                ).build(),
            ])
        .build()

    when:
    def result = StructMapper.toMap(struct)

    then:
    result.size() == 7
    result == [
        'boolKey'                  : 'true',
        'listKey[0]'               : 'listString',
        'listKey[1][0]'            : 'anotherListValue',
        'nullKey'                  : '',
        'numberKey'                : '0.6',
        'stringKey'                : 'string',
        'structKey.structNested[0]': 'nestedString'
    ]
  }
}
