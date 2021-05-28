package com.silenteight.hsbc.bridge.alert;

import lombok.Value;

import com.silenteight.hsbc.bridge.alert.dto.AlertDataComposite;
import com.silenteight.hsbc.bridge.json.external.model.AlertData;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;

public interface AlertPayloadConverter {

  AlertData convertAlertData(byte[] payload) throws AlertConversionException;

  void convertAndConsumeAlertData(InputCommand command, Consumer<AlertDataComposite> consumer);

  @Value
  class InputCommand {

    String bulkId;
    InputStream inputStream;
  }

}
