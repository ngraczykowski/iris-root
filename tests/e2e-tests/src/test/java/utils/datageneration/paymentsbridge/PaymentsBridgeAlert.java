/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package utils.datageneration.paymentsbridge;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentsBridgeAlert {

  private String id;
  private String payload;
}
