package com.silenteight.customerbridge.cbs.alertrecord;

import com.silenteight.customerbridge.cbs.alertid.AlertId;
import com.silenteight.customerbridge.cbs.domain.CbsHitDetails;

import java.util.Collection;
import java.util.List;

interface CbsHitDetailsReader extends OracleReader {

  List<CbsHitDetails> read(String dbRelationName, Collection<AlertId> alertIds);
}
