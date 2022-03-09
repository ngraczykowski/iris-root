package com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord;

import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertId;

class Fixtures {

  static final class DummyAlert {

    static final AlertId ID = AlertId.builder().systemId("systemId").batchId("batchId").build();
  }

  static final class RealVnDenyAlert {

    static final AlertId ID = AlertId.builder()
        .systemId("VN_EMPL_DENY!DA851ED8-426747D5-AC4A8333-3BEFB8C4")
        .batchId("2019/01/25_0002_VN_EMPL_DENY")
        .build();
  }

  static final class RealInPeplAlert {

    static final AlertId ID = AlertId.builder()
        .systemId("IN_BTCH_PEPL!EE3063A6-AA6E4BA4-A9045263-1828966F")
        .batchId("2019/01/24_0001_IN_BTCH_PEPL")
        .build();
  }
}
