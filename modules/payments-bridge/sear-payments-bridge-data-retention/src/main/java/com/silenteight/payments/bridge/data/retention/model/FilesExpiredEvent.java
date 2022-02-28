package com.silenteight.payments.bridge.data.retention.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class FilesExpiredEvent {

  List<String> fileNames;
}
