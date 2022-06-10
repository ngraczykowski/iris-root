# Callback Mock

## Purpose

To scoop up callbacks from various bridges and forward them via WS(STOMP) to verify during tests

## Usage

1. Run the app as iris component (should be configured in the chart)
2. Point callbacks towards the app. The endpoint for pb is prepared in `PbCmapiCallbackMockController.java`
3. Connect to ws endpoint using STOMP protocol (the endpoint is at `/ws`, defined in `WebSocketConfig.java`)
4. Subscribe to the topic corresponding to the callbacks. Currently there is no fancy contract for that. Just check the destination value in `PbCmapiCallbackMockController.java`
5. Read incoming messages as `CallbackDTO` json.

The app is used by e2e tests module. The necessary steps should be defined in `CallbackMockSteps.java` there.
