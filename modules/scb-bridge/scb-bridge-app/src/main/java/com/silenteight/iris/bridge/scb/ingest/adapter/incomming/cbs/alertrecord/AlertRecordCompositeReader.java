/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertrecord;

import com.silenteight.proto.serp.scb.v1.ScbAlertIdContext;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertid.AlertId;

import java.util.List;

public interface AlertRecordCompositeReader {

  AlertRecordCompositeCollection read(ScbAlertIdContext context, List<AlertId> alertIds);
}
