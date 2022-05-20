package com.silenteight.adjudication.engine.mock.agents;

import lombok.NoArgsConstructor;

import com.google.protobuf.Struct;
import com.google.protobuf.util.JsonFormat;

import java.io.InputStream;
import java.io.InputStreamReader;
import javax.annotation.Nonnull;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
class JsonReasonReader {

  static Struct readFromResource(String jsonResource) {
    var builder = Struct.newBuilder();
    try (var is = getStream(jsonResource); var reader = new InputStreamReader(is)) {
      JsonFormat.parser().merge(reader, builder);
      return builder.build();
    } catch (Exception e) {
      throw new JsonToStructConversionException(e);
    }
  }

  @Nonnull
  private static InputStream getStream(String resource) {
    return requireNonNull(JsonReasonReader.class.getClassLoader().getResourceAsStream(resource));
  }

  static class JsonToStructConversionException extends RuntimeException {

    private static final long serialVersionUID = 7425583627565637536L;

    JsonToStructConversionException(Throwable cause) {
      super(cause);
    }
  }
}
