package com.silenteight.hsbc.bridge.json;

public interface ObjectConverter {

  <T> T convert(byte[] src, Class<T> valueType) throws ObjectConversionException;

  byte[] convert(Object value);

  class ObjectConversionException extends Exception {

    private static final long serialVersionUID = 5313812874007551249L;

    ObjectConversionException(Throwable throwable) {
      super(throwable);
    }
  }
}
