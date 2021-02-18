package com.silenteight.hsbc.bridge.bulk.command;

import lombok.NonNull;
import lombok.Value;

import com.silenteight.hsbc.bridge.rest.model.input.Alerts;

@Value
public class CreateBulkCommand {
  @NonNull Alerts alerts;
}
