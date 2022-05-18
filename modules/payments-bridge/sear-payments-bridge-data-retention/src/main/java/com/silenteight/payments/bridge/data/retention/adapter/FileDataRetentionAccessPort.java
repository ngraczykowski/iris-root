package com.silenteight.payments.bridge.data.retention.adapter;

import com.silenteight.payments.bridge.data.retention.model.FileDataRetention;

import java.time.OffsetDateTime;
import java.util.List;

public interface FileDataRetentionAccessPort {

  List<String> findFileNameBefore(OffsetDateTime dateTime);

  void create(Iterable<FileDataRetention> fileDataRetentions);

  void update(List<String> fileNames);
}
