package com.silenteight.customerbridge.common.recommendation;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/v1/recommendation")
@RequiredArgsConstructor
public class RecommendationRestController {

  private final RecommendationService recommendationService;

  @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RecommendationDto> getRecommendation(
      @RequestBody @Valid RecommendationRequest request) {
    RecommendationDto recommendationDto = recommendationService.getRecommendation(request);
    return ok(recommendationDto);
  }

  @PostMapping(
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE,
      path = "ondemand")
  public ResponseEntity<RecommendationDto> getExistingRecommendationOrOnDemand(
      @RequestBody @Valid RecommendationRequest request) {
    RecommendationDto recommendationDto =
        recommendationService.getExistingRecommendationOrOnDemand(request);
    return ok(recommendationDto);
  }

  @ExceptionHandler(SystemIdNotFoundException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "System identifier has not been found")
  public void handleSystemIdNotFoundException() {
    //do nothing
  }

  @ExceptionHandler(AlertNotFoundException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Alert has not been found")
  public void handleAlertNotFoundException() {
    //do nothing
  }

  @ExceptionHandler(DiscriminatorNotFoundException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Discriminator has not been found")
  public void handleDiscriminatorNotFoundException() {
    //do nothing
  }

  @ExceptionHandler(AlertAlreadySolvedException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Alert has already been solved")
  public void handleAlertAlreadySolvedException() {
    //do nothing
  }

  @ExceptionHandler(AlertDamagedException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Alert is damaged")
  public void handleAlertDamagedException() {
    //do nothing
  }

  @ExceptionHandler(RecommendationTimeoutException.class)
  @ResponseStatus(
      value = HttpStatus.REQUEST_TIMEOUT,
      reason = "Recommendation has not been provided within provided time")
  public void handleRecommendationTimeoutException() {
    //do nothing
  }
}
