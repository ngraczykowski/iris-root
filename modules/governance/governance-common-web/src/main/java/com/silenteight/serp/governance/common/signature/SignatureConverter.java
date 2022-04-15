package com.silenteight.serp.governance.common.signature;

import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import static com.silenteight.sep.base.common.protocol.ByteStringUtils.toBase64String;

@Converter
@Component
public class SignatureConverter implements AttributeConverter<Signature, String> {

  @Override
  public String convertToDatabaseColumn(Signature attribute) {
    if (attribute == null)
      return null;

    return toBase64String(attribute.getValue());
  }

  @Override
  public Signature convertToEntityAttribute(String value) {
    if (value == null)
      return null;

    return Signature.fromBase64(value);
  }
}
