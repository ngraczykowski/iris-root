package com.silenteight.payments.bridge.common.dto.output;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

class AlertDecisionActionDtoSerializer extends StdSerializer<AlertDecisionActionDto> {

  private static final long serialVersionUID = 2494561902782496746L;

  public AlertDecisionActionDtoSerializer() {
    this(null);
  }

  public AlertDecisionActionDtoSerializer(Class<AlertDecisionActionDto> t) {
    super(t);
  }

  @Override
  public void serialize(
      AlertDecisionActionDto value, JsonGenerator jgen, SerializerProvider provider)
      throws IOException {

    jgen.writeStartObject();
    jgen.writeEndObject();
  }
}
