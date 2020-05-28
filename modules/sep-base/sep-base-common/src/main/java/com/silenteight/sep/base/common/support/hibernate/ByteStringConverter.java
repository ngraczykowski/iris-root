package com.silenteight.sep.base.common.support.hibernate;

import com.silenteight.sep.base.common.protocol.ByteStringUtils;

import com.google.protobuf.ByteString;

import javax.annotation.Nullable;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Converts ByteString into Base-64 encoded String for use as columns in the database.
 *
 * @apiNote (ahaczewski): Do not use for fields that are used in Spring Data JPA Repository
 *     queries because it does not work: https://jira.spring.io/browse/DATAJPA-1682.
 *     It is recommended to use String in entities instead and bytes in Proto files.
 */
@Converter
public class ByteStringConverter implements AttributeConverter<ByteString, String> {

  @Override
  @Nullable
  public String convertToDatabaseColumn(@Nullable ByteString attribute) {
    if (attribute == null)
      return null;

    if (attribute.isEmpty())
      return "";

    return ByteStringUtils.toBase64String(attribute);
  }

  @Override
  @Nullable
  public ByteString convertToEntityAttribute(@Nullable String dbData) {
    if (dbData == null)
      return null;

    if (dbData.isEmpty())
      return ByteString.EMPTY;

    return ByteStringUtils.fromBase64String(dbData);
  }
}
