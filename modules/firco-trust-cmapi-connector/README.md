Firco Trust CMAPI Connector (FTCC)
===
Documentation
---
Whimsical [HLD](https://whimsical.com/bridge-x5mk356DKDpj15vBzH4zn) diagram.

Modules
---

- [ftcc-app](./ftcc-app) - entrypoint module,
- [ftcc-callback](./ftcc-callback) - callback module - handle `BatchCompleted` messages, request
  for `GetRecommendation` and
  generate [`ClientRequestDto`](ftcc-common/src/main/java/com/silenteight/connector/ftcc/common/dto/output/ClientRequestDto.java)
  for callback,
- [ftcc-common](./ftcc-common) - common classes,
- [ftcc-common-testing](./ftcc-common-testing) - common testing classes,
- [ftcc-db-changelog](./ftcc-db-changelog) - changelog for liquibase,
- [ftcc-ingest](./ftcc-ingest) - ingest module - accepting incoming requests, register batch.

Configuration
---
### AMQP
- `ftcc.core-bridge.inbound.batch-completed.exchange` - **BatchCompleted** exchange name,
- `ftcc.core-bridge.outbound.recommendations-delivered.exchange` - **RecommendationDelivered** exchange name,

### Callback `ftcc.cmapi.callback`
- `ftcc.cmapi.callback.endpoint` - callback endpoint (default: `http://localhost:8080/rest/ftcc/callback`)
- `ftcc.cmapi.callback.login` - login placed in CallbackRequestDto (default: `user`)
- `ftcc.cmapi.callback.password` - password placed in CallbackRequestDto (default: `password`)
- `ftcc.cmapi.callback.connectionTimeout` - (default: `10s`)
- `ftcc.cmapi.callback.readTimeout` - (default: `10s`)

### DecisionMapper `ftcc.decision.resource-location`

- `ftcc.decision.resource-location` - location of the decision mapping (
  default: [`classpath:decision/decision.csv`](ftcc-callback/src/main/resources/decision/decision.csv)):  
  - `SourceState` - state from request `Body.msg_SendMessage.Messages[n].Message.CurrentStatus.Name`
  - `Recommendation` - recommendation generated by **AE**,
  - should be mapped to `DestinationState` - from
    request: `Body.msg_SendMessage.Messages[n].Message.NextStatuses[i].Status.Name`, example:

| SourceState | Recommendation                 | DestinationState            |
|-------------|--------------------------------|-----------------------------|
| NEW         | ACTION_FALSE_POSITIVE          | "Level 2-, FALSE1"          |
| NEW         | ACTION_POTENTIAL_TRUE_POSITIVE | "Level 2-, TRUE1"           |
| NEW         | ACTION_INVESTIGATE             | "Level 1-, SEAR_UNRESOLVED" |

### Alert state mappings `ftcc.alert.state.mappings`

- `ftcc.alert.state.mappings` - defines alert states (NEW, SOLVED_FALSE_POSITIVE, SOLVED_TRUE_POSITIVE)
  with list of corresponding statuses from message json (Message -> CurrentStatus -> Name). Alert state
  should map to one of the enum values from `AlertMessageStored.State` from Connector API. If mapper
  does not find state for given message status it returns `STATE_UNSPECIFIED` state which postpones
  request processing.

#### Troubleshooting

`java.lang.IllegalStateException: No 'DestinationState' in configuration for currentStatusName=COMMHUB and S8 recommendedAction=PTP`
add to configuration (`ftcc.decision.resource-location`) entry:

| SourceState | Recommendation | DestinationState                                                                           |
|-------------|----------------|--------------------------------------------------------------------------------------------|
| COMMHUB     | PTP            | `value from request: Body.msg_SendMessage.Messages[n].Message.NextStatuses[i].Status.Name` |

## Spring Profiles:

- **mockcallback** - expose `/callback` endpoint to handle `ClientRequestDto`,
- **mockcorebridge** - mock `core-bridge` service,

## Development

- Java: **11**,
- Spring Boot: **2.6.6**
