package com.silenteight.payments.bridge.etl.processing.port;

public interface EtlProcess {

  void execute(EtlHandler context);
}
