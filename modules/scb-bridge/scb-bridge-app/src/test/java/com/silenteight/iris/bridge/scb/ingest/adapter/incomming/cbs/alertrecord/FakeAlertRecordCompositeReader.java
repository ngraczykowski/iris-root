/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertrecord;

import lombok.Setter;

import com.silenteight.proto.serp.scb.v1.ScbAlertIdContext;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertid.AlertId;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.alertrecord.AlertRecord;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.alertrecord.DecisionRecord;

import org.springframework.dao.NonTransientDataAccessResourceException;
import org.springframework.dao.TransientDataAccessResourceException;

import java.util.List;

import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toList;

class FakeAlertRecordCompositeReader implements AlertRecordCompositeReader {

  @Setter
  private FakeReaderMode mode = FakeReaderMode.DUMMY;

  @Override
  public AlertRecordCompositeCollection read(
      ScbAlertIdContext context, List<AlertId> alertIds) {
    return fakeRead(alertIds);
  }

  AlertRecordCompositeCollection fakeRead(List<AlertId> alertIds) {
    switch (mode) {
      case DUMMY:
        return readDummy(alertIds);
      case INVALID_TEMPORARILY:
        throw new TransientDataAccessResourceException("Fake transient error");
      case INVALID_FAILED:
      default:
        throw new NonTransientDataAccessResourceException("Fake persistent error");
    }
  }

  private AlertRecordCompositeCollection readDummy(List<AlertId> alertIds) {
    List<AlertRecord> records =
        alertIds.stream().map(FakeAlertRecordCompositeReader::idToRecord).collect(toList());
    List<DecisionRecord> decisions =
        alertIds.stream().map(FakeAlertRecordCompositeReader::idToDecision).collect(toList());

    return new AlertRecordCompositeCollection(
        alertIds,
        records,
        decisions,
        emptyMap());
  }

  private static AlertRecord idToRecord(AlertId id) {
    return AlertRecord.builder().systemId(id.getSystemId()).batchId(id.getBatchId()).build();
  }

  private static DecisionRecord idToDecision(AlertId id) {
    return DecisionRecord.builder().systemId(id.getSystemId()).type(0).operator("OPER").build();
  }

  enum FakeReaderMode {
    DUMMY,
    INVALID_TEMPORARILY,
    INVALID_FAILED
  }
}
