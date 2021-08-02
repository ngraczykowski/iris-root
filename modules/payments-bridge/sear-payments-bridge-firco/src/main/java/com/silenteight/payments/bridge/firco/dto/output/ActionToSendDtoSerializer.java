package com.silenteight.payments.bridge.firco.dto.output;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

class ActionToSendDtoSerializer extends StdSerializer<ActionToSendDto> {

  private static final long serialVersionUID = 2494561902782496746L;

  public ActionToSendDtoSerializer() {
    this(null);
  }

  public ActionToSendDtoSerializer(Class<ActionToSendDto> t) {
    super(t);
  }

  @Override
  public void serialize(
      ActionToSendDto value, JsonGenerator jgen, SerializerProvider provider)
      throws IOException {

    jgen.writeStartObject();
    jgen.writeEndObject();
  }
}
