package com.silenteight.payments.bridge;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

class AlertControllerTest {

  @Mock
  private SubmitRequest submitRequest;

  @Test
  void throwsNullPointerExceptionIfParametersAreNull() {
    AlertController alertController = new AlertController(submitRequest);

    assertThrows(
        NullPointerException.class,
        () -> alertController.foo(null, null));
  }

}
