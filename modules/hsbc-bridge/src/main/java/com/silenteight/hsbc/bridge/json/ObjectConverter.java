package com.silenteight.hsbc.bridge.json;

public interface ObjectConverter {

  <T> T convert(byte[] src, Class<T> valueType);

  byte[] convert(Object value);
}
