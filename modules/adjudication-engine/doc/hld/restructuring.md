# Restructuring AE

## Agent Exchange Request handling

- Create GetMatchFeatureValuesCommand and queue is in RMQ
- The command handler should:
  - Use asynchronous, best reactor-driven gateway to send the request to Agent via RMQ, then wait for the response
  - If response does not arrive - NACK
  - If AE dies - NACK
  - On response, insert result based on data in the command
