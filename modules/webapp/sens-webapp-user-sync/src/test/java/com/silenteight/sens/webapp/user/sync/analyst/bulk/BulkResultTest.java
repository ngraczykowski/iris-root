package com.silenteight.sens.webapp.user.sync.analyst.bulk;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.silenteight.sens.webapp.user.sync.analyst.bulk.SingleResult.failure;
import static com.silenteight.sens.webapp.user.sync.analyst.bulk.SingleResult.success;
import static org.assertj.core.api.Assertions.*;

class BulkResultTest {

  @Test
  void returnErrorMessagesWithMaxSizeConstraint() {
    String errMsg1 = "err msg1";
    String errMsg2 = "err msg2";
    String errMsg3 = "err msg3";
    BulkResult bulkResult = new BulkResult(
        List.of(
            success(),
            failure(errMsg1),
            success(),
            failure(errMsg2),
            failure(errMsg3)));

    assertThat(bulkResult.errorMessagesWithMaxSizeOf(10))
        .containsExactly(errMsg1, errMsg2, errMsg3);
    assertThat(bulkResult.errorMessagesWithMaxSizeOf(2))
        .containsExactly(errMsg1, errMsg2);
  }

  @Test
  void returnEmptyListIfNoErrors() {
    BulkResult bulkResult = new BulkResult(List.of(success()));

    assertThat(bulkResult.errorMessagesWithMaxSizeOf(10)).isEmpty();
  }
}
