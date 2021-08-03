# ADR 0003: CMAPI alerts processing

## Goal

Determine how to process alerts received via CMAPI.

## Receiving alerts from CMAPI

1. Receive alerts in CMAPI Controller (`FircoCaseManagerController`).
2. Authenticate the request.
3. Send messages from the request (`RequestMessageDto`) to rabbit exchange `pb.TBD`.
4. Respond with `msg_Acknowledge`.

## Queues in RabbitMQ

The alert is sent to exchange, that distributes it to two queues:
  - accepting queue
  - delaying queue


Queues:
  - queue for accepting alerts - no TTL
  - queue for delaying alerts - TTL, connected to queue for outdated alerts as its DLQ
  - queue for outdated alerts - no TTL

## Accepting alerts

1. Check whether the alert is outdated
   * if it is, just drop the alert
   * if not, proceed
2. Check how many alerts are in flight (Accepted, but not Recommended)
   * if it exceeds configured number of alerts:
     * Mark the alert as exceeding threshold in the DB
     * Send back the MI to CMAPI
   * if not, proceed
3. Register the alert in the AE
4. Extract features, categories, and comment inputs from the alert
5. Store features, categories, and comment inputs in the Data Source
6. Trigger alert processing in the AE
7. Mark the alert as accepted in the DB

## Recommending alerts

1. Receive the `RecommendationsGenerated` notification from AE
2. Get the recommendation from AE
3. Store the recommendation in the DB
4. Check whether the alert is outdated
   * if it is, just drop the alert
   * if not, proceed
5. Mark the alert as recommended in the DB
6. Send back the recommendation to CMAPI

## Outdating alerts

1. Check whether the alert is already recommended
   * if it is, just drop the alert
   * if not, proceed
2. Mark the alert as outdated in the DB
3. Send back the Manual Investigation to CMAPI

## Alert states

- accepted
- recommended
- outdated
- threshold exceeded

## Things to do

- Add deadline to `AgentExchangeRequest`
- Ask MI6 to implement deadline with `AGENT_TIMEOUT` response
- Add support for deadline in AE
- Add timeout of agent requests in AE
- Design `sear-payments-bridge-ae` module API for:
  - storing features
  - triggering processing
- Design how to extract features, categories and comment inputs.
