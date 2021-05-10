package com.silenteight.hsbc.bridge.alert;

import com.silenteight.hsbc.bridge.json.external.model.AlertData;

import java.util.List;

public interface AlertPayloadConverter {

  List<AlertData> convert(byte[] payload);
}
