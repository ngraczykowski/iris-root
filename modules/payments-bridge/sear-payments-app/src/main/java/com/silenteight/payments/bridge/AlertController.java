package com.silenteight.payments.bridge;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.dto.input.RequestDto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@RestController
@RequiredArgsConstructor
public class AlertController {

  private final SubmitRequest submitRequest;

  @PostMapping("/foo")
  @Valid
  public String foo(
      @RequestBody RequestDto requestDto,
      @NotNull @RequestParam String dc) {

    submitRequest.invoke(requestDto);
    return "OK";
  }

  @ExceptionHandler(MessageDeliveryException.class)
  public ResponseEntity<String> handleMessageDeliveryException() {
    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
  }
}
