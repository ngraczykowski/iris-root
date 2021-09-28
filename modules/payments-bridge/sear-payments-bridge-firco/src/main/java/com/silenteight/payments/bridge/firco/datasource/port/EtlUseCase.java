package com.silenteight.payments.bridge.firco.datasource.port;

import com.silenteight.payments.bridge.event.AlertRegistered;

import java.util.function.Consumer;

public interface EtlUseCase extends Consumer<AlertRegistered> {

}
