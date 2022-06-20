/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch;

import java.util.List;

public interface MultipleAlertCompositeFetcher {

  List<AlertComposite> fetch(List<String> systemIds);
}
