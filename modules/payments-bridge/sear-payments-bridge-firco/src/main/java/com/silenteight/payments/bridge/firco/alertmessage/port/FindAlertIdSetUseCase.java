package com.silenteight.payments.bridge.firco.alertmessage.port;

import com.silenteight.payments.bridge.firco.alertmessage.model.AlertFircoId;
import com.silenteight.payments.bridge.firco.alertmessage.model.AlertIdSet;

import java.util.List;

public interface FindAlertIdSetUseCase {

  List<AlertIdSet> find(List<AlertFircoId> ids);

}
