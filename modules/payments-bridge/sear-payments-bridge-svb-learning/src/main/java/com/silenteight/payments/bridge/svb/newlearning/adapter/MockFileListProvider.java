package com.silenteight.payments.bridge.svb.newlearning.adapter;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.newlearning.domain.ObjectPath;
import com.silenteight.payments.bridge.svb.newlearning.port.FileListPort;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Profile("mockaws")
class MockFileListProvider implements FileListPort {

  @Override
  public List<ObjectPath> getFilesList() {
    log.debug("Using mocked file list provider");
    var object = ObjectPath.builder().name("analystdecison-2-hits.csv").bucket("bucket").build();
    var object2 = ObjectPath.builder().name("analystdecison-2-hits2.csv").bucket("bucket").build();
    return List.of(object, object2);
  }
}
