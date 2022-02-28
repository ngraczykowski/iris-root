package com.silenteight.payments.bridge.data.retention.adapter;

import java.time.OffsetDateTime;
import java.util.List;

public interface FileDataRetentionAccessPort {

  List<String> findFileNameBefore(OffsetDateTime dateTime);
}
