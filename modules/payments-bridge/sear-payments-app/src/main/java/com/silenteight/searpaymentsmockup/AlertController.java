package com.silenteight.searpaymentsmockup;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@RestController
@RequiredArgsConstructor
class AlertController {

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
