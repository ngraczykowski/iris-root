package com.silenteight.hsbc.bridge.bulk;

import com.silenteight.hsbc.bridge.json.external.model.AlertData;

import java.util.Collection;

public interface BulkPayloadConverter {

  Collection<AlertData> convert(byte[] payload);
}
