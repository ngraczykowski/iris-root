# Adjudication API

## Resources

### The resources available in Data Source

-  A collection of categories: `categories/*`. Each category has the following resources:
  -  A resource representing possible values: `categories/*/values`.
-  A collection of agent inputs: `agent-inputs/*`.
  -  **TODO** How to ask for inputs for given match/alert? Add batch as well.

           GET [transforms/v1/]agent-inputs -> [
               "name-v1",
               "name-v2"  
           ]

           GET [transforms/v1/]agent-inputs/name-v2/alerts/1/matches/3

           POST [transforms/v1/]agent-inputs/name-v2/alerts/1/matches:batchGet  

-  A collection of comment inputs for specific alert(s): `comment-input/alerts/*`.

        GET [transforms/v1/]comment-input/alerts/1 {} -> {
            field_1: ...
            field_2: ...
        }

        POST [transforms/v1/]comment-input/alerts:batchGet [
            "alerts/1", "alerts/2"
        ] -> [
            {
                field_1: ...
                field_2: ...
            }, ...
        ]


### The resources available in Governance

- collection of all agents registered in Governance

        GET agents -> [
            {
                "name": "agents/12",
                "type": "country",
                "api-version": "v1",
                "version": "1.4.42"
                 solutions: ["NO_DATA", "MATCH", "NO_MATCH]
             },
             {
                 "name": "agents/13",
                 "type": "country",
                 "api-version": "v1",
                 "version": "1.4.43",
                 solutions: ["NO_DATA", "MATCH", "NO_MATCH]
              }
        ]  


    NOTES: a policy may or may not depend on an exact version of agent, depending
    on expected behavior during config-agent-sets selection

            config-agents-sets
                policy:
                   Step 1:
                      NAME-v4 iS EXACT MATCH vs NAME-4_2_42 is EXACT MATCH

- collection of all agent configurations sets

        GET config-agent-sets -> [
            {
                config-agent-set-name: "config-agent-sets/1"
                agents: [
                    "name-4_2_42",
                    "date-2_2_1"
                ]  
            }  
        ]

- create agent configuration set

        POST config-agent-sets [
            "name-4_2_42",
            "date-2_2_1"
        ] -> {}



### The resources available in Commentator

-  A collection of analyst comments: `comments/*`.

        POST comments/ {
            matches: [{
                match: "alerts/1342/matches/32"
                solution: "PTP"
                reasons: []
            }, ...],
            alert:
            alertSolution:
        } -> string


### The resources available in Adjudication Engine

-  A collection of alerts: `project/*/alerts/*`. Each alert has the following resources:
  -  A collection of matches: `alerts/*/matches/*`. Each match has the following resources:
  -  A collection of analysis: `alerts/*/analysis/*`.
  -  A collection of datasets: `alerts/*/datasets/*`.
-  A collection of datasets: `datasets/*`. Each dataset has the following resources:
  -  A collection of alerts: `datasets/*/alerts/*`.
  -  A collection of analysis: `datasets/*/analysis/*`.

-  A collection of analysis: `analysis/*`. Each analysis has the following resources:
  -  A collection of datasets this analysis runs on: `analysis/*/datasets/*`.
  -  A collection of recommendations: `analysis/*/recommendations/*`.
  -  `GET analysis/123/recommendations`
  -  `GET analysis/123/recommendations/41414`
  -  `GET analysis/123/match-solutions`
  -  `GET analysis/123/match-solutions/143123`
  -  A collection of match solutions: `analysis/*/match-solutions/*`.
  -  A collection of feature vectors: `analysis/*/vectors/*`. *(do we need this?)*
  -  A collection of metrics: `analysis/*/metrics/*`.
     *(It is possible to create a collection of metrics, extending available ones in the future, presenting in UI in generic way)*

To do when thinking about POV use cases:

-  A resource representing data availability: `data-availability/`.
-  A resource representing data availability at match level: `agent-inputs/*/matches/*/data-availability`.
-  A resource representing data availability: `analysis/*/agents/*/data-availability`.
-  A resource representing data availability: `analysis/*/datasets/*/data-availability`.
-  `GET alerts/*/matches/*/agent-outputs/name_3_4_42_config_3` - output of `name_3_4_42_config_3` agent from given match
-  `GET alerts/-/matches/-/agent-outputs/name_3_4_42_config_3` - all `name_3_4_42_config_3` agent outputs across all alerts and matches
-  `GET alerts/*/matches/*/agent-outputs` - outputs of all agents for given match
-  `GET agent-outputs/name_3_4_42_config_3/alerts/*/matches/*`

### Diagram

adjudication-api/src/main/images/Screenshot from 2021-02-24 11-07-31.png

### Bridge Use Cases

#### 1. On start:

    POST categories {name="source_system"}
    POST categories {name="country", multi_value=true}
    POST categories {name="customer_type"}
    POST categories {name="hit_category"}
    PUT categories/customer_type/values ["I", "C"]
    PUT categories/hit_category/values ["SAN", "PEP", "AM"]

Categories do not have to have values POSTed up-front, as each category will receive a value for each alert.
Values for these categories that can be "primed" might come directly from Bridge configuration, or by SQL query.

#### 2. For each batch of alerts:

For every alert:

    POST alerts {
        alert_id="AE_BTCH_PEPL!1391FFFD-2E8A4AC7-B0E596E6-2FE1CEC1",
        alert_time="2021-02-16T21:25:34Z"
        lables={
            "batch"="2021-02-16T21:24:13Z",
            "country"="AE"  
        }
    } -> {
        name="alerts/1342",
        alert_id="AE_BTCH_PEPL!1391FFFD-2E8A4AC7-B0E596E6-2FE1CEC1",
        alert_time="2021-02-16T21:25:34Z",
        create_time="2021-02-16T23:02:45Z"
    }

    POST alerts/1342/matches {
        match_id="DB00013388"
    } -> {
        name="alerts/1342/matches/32",
        match_id="DB00013388"
    }

    PUT alerts/1342/matches/32/categories {
        source_system="ECDD",
        country=["PL", "US"],
        hit_category="SAN"
    }

    bridge -> agent facade
    POST agent-apis/name_v4/inputs {
        match="alerts/1342/matches/32",
        instance_name="advname_indv",
        alerted_names=["John Smith"],
        matched_names=["Jon Schmit"]
    } -> {
        name="agents/name_v4/inputs/858372",
        match="alerts/1342/matches/32"
    }

    POST agent-apis/name_4_2_42/inputs {
        match="alerts/1342/matches/32",
        instance_name="advname_indv",
        alerted_names=["John Smith"],
        matched_names=["Jon Schmit"]
    } -> {
        name="agents/name_4_2_42/inputs/858372",
        match="alerts/1342/matches/32"
    }

    bridge -> agent facade
    POST agent-apis/county_v5/inputs {
        match="alerts/1342/matches/32",
        alerted_countries=["PL"],
        matched_countries=["Poland"]
    } -> {
        name="agents/country_5/inputs/858372",
        match="alerts/1342/matches/32"
    }

It should be easy to add a bulk API for creating all alerts at once.

> NOTE
>
> Labels will not be used in the solving/analyzing process but will be used for filtering alerts
> during the presentation (Chinese alerts for Chinese operators) or for separate reports.

Let's assume that there are two alerts with five matches to add. All matches are described by
seven agents and have their own category. It will require following API calls:
- add alert
- add first match
- add second match
- add category for first match
- add category for second match
- add seven agent values

There were 12 calls for one alerts, which means 24 calls are required to add two similar alerts.

With bulk API number of calls can be reduced. As alerts and matches will always exist together,
they can be created at once. First all alerts can be created:

    POST alerts:batchCreate [{
        alert_id="AE_BTCH_PEPL!1391FFFD-2E8A4AC7-B0E596E6-2FE1CEC1",
        alert_time="2021-02-16T21:25:34Z"
        lables={
            "batch"="2021-02-16T21:24:13Z",
            "country"="AE",
        }
    }, {
        alert_id="SG_BTCH_DUDL!1391FFFD-2E8A4AC7-B0E596E6-2FE1CEC1",
        alert_time="2021-02-08T21:25:34Z"
        lables={
            "batch"="2021-02-08T21:24:13Z",
            "country"="AE",
        }
    }] -> [{
        name="alerts/1342",
        alert_id="AE_BTCH_PEPL!1391FFFD-2E8A4AC7-B0E596E6-2FE1CEC1",
        alert_time="2021-02-16T21:25:34Z",
        create_time="2021-02-16T23:02:45Z"
    }, {
        name="alerts/1343",
        alert_id="SG_BTCH_DUDL!1391FFFD-2E8A4AC7-B0E596E6-2FE1CEC1",
        alert_time="2021-02-08T21:25:34Z",
        create_time="2021-02-16T23:02:48Z"
    }]

Then matches can be assigned to alerts with one request:

    POST alerts/-/matches:batchCreate [{
        alert=alerts/1342
        match_id="DB00013388"
    },
    {
        alert=alerts/1343
        match_id="DB00024731"
    }]-> [{
        name="alerts/1342/matches/32",
        match_id="DB00013388"
    }, {
        name="alerts/1343/matches/33",
        match_id="DB00024731"
    }]

Also categories can be assigned to matches with just one request:

    PUT alerts/-/matches/-/categories:batchUpdate [{
        match="alerts/1342/matches/32",
        source_system="ECDD",
        country=["PL", "US"],
        hit_category="SAN"
    }, {
        match="alerts/1343/matches/33",
        source_system="ECDD",
        country=["SG", "US"],
        hit_category="SAN"
    }]

It is also possible to set agent inputs with batch call. The limitation is that only given agent
in specific version can be updated at once (because proto between versions can differ):

    POST agents/name_4_2_42/inputs:batchCreate [{
        match="alerts/1342/matches/32",
        instance_name="advname_indv",
        alerted_names=["John Smith"],
        matched_names=["Jon Schmit"]
    }, {
        match="alerts/1343/matches/33",
        instance_name="advname_indv",
        alerted_names=["Adam Doe"],
        matched_names=["Adam S. Doe"]
    }] -> [{
        name="agents/name_4_2_42/inputs/858372",
        match="alerts/1342/matches/32"
    }, {
        name="agents/name_4_2_42/inputs/858373",
        match="alerts/1343/matches/33"
    }]

    POST agents/county_5_4_32/inputs:batchCreate [{
        match="alerts/1342/matches/32",
        alerted_countries=["PL"],
        matched_countries=["Poland"]
    }, {
        match="alerts/1343/matches/33",
        alerted_countries=["SG"],
        matched_countries=["Singapore"]
    }] -> [{
        name="agents/county_5_4_32/inputs/858372",
        match="alerts/1342/matches/32"
    }, {
        name="agents/county_5_4_32/inputs/858373",
        match="alerts/1343/matches/33"
    }]

Following changes helped to reduce 3 calls as alerts, matches and categories are assigned at once.
Then it does not matter how many alerts and matches will be posted, still it will require just
three calls to send them all to engine. Also number of agent input calls will be reduced to seven.
Summarizing it gives just 10 calls to insert the same data.

> NOTE
>
> Labels will not be used in the solving/analyzing process but will be used for filtering alerts
> during the presentation (Chinese alerts for Chinese operators) or for separate reports.

> NOTE
>
> The result of `POST agents/name_4_2_42/inputs` call does not return sensitive personal information.
> That is intentional.

**Questions**
1. From where AE will know what version of Serp-protocol should use?
1. What if agent input changes? Like in example: Name-agent has changed matched_values -> watchlist_values.
1. In this case HSBC-Brigde and AE will have to have dependencies to serp-protocol for creating InputAgentObject.
   or request to AE will have to have proto object created and serialized:
   ```protobuf
   POST agents/name_4_2_42/inputs {
       match="alerts/1342/matches/32",
       flags=ALERTED_AVAILABLE | MATCHED_AVAILABLE
       instance_name="advname_indv",
       alerted_names=["John Smith"],
       matched_names=["Jon Schmit"],

       input_agent_proto = byte[]
   }
   ```
1. GNS-RT case for example case with 3 Alerts and 20 matches for each.
1. What about priorities?
1. What about learning? In HSBC learning only appears in Simulator environment, what about SCB case?
1. What in case when AE will have version of name-agent input in version for example '42' and simulation
   will requires input for version '43' which was not previously processed? It should go to HSBC-Bridge for input?
 ```
 name_42: John Jon Smith
 name_43: John Smith
 ```


#### 3. Auditing:

    GET alerts/1342/matches/32/agents {
        agents = [
            name_v4,
            county_v5,
            ...
        ]
    }

    GET agents/name_4_2_42/inputs {
        match="alerts/1342/matches/32"
    } -> {
        match="alerts/1342/matches/32",
        alerted_names=["John Smith"],
        matched_names=["Jon Schmit"]
    }

    GET analysis/829/alerts/1342/audit {
        strategy_name='strategy/5',
        ...
    }

    GET analysis/829/alerts/1342/match/23434/audit {
        step_name='steps/4334',
        ...
    }

#### 4. Creating data sets:

    POST datasets {
        alerts=[...]
    } -> {
        name="datasets/e572ca87-e93f-4e17-8954-d2c9892055ad"
        create_time="2021-02-17T07:14:48Z",
        alert_count=3400
    }

Each batch should create his own dataset.

#### 5. Creating analysis

First step is to create Analysis:

    POST analysis {
        policy="policy/54317992-a203-4e95-b145-b47d6a3aef0a",
        strategy="strategy/back_test"
        agent_configuration_set="configurations/3544" // contains all agents configuration, agent_version (name=4.4.42, coutry=5.4.3, ...)
    } -> {
        name="analysis/829",
        policy="policy/54317992-a203-4e95-b145-b47d6a3aef0a",
        strategy="strategy/back_test",
        create_time="2021-02-17T07:18:48Z",
        state=NEW
    }

Then add datasets to analysis:

    POST analysis/829/datasets {
        dataset="datasets/e572ca87-e93f-4e17-8954-d2c9892055ad"
    } -> {
        name="analysis/829",
        policy="policy/54317992-a203-4e95-b145-b47d6a3aef0a",
        strategy="strategy/back_test",
        create_time="2021-02-17T07:18:48Z",
        state=PLANNING,
        alert_pending=63125,
        alert_count=63125
    }

    POST analysis/829/datasets:batchAdd {
        datasets = [
            "datasets/e572ca87-e93f-4e17-8954-d2c9892055ad",
            "datasets/d2c9892e-4e17-8954-e93f-572ca87d2989"
        ]
    } -> {
        name="analysis/829",
        policy="policy/54317992-a203-4e95-b145-b47d6a3aef0a",
        strategy="strategy/back_test",
        create_time="2021-02-17T07:18:48Z",
        state=PLANNING,
        alert_pending=40125,
        alert_count=63125
    }

Adding dataset to analysis will trigger the solving process.
This should be requested at the end of the process.

Each batch should create its own analysis, even if the analysis will be the same as the previous
one, with only a difference in deteset_id.

#### 6. Processing


    ae -> agent facade(solving)
    GET agents/name/result {
        match="alerts/1342/matches/32",
        agent_configuration_set="configurations/3544"
    } -> {
        result="NO_MATCH"
        reason={...}
    }

Store result and reason in AE.

    ae -> governance
    GET governance/solve_features {
        features_map = {...}
    } -> {
        solution="POTENTIAL_TRUE_POSITIVE"
    }

    ae -> strategist
    GET strategist/solve {
        matches=[
            "alerts/1342/matches/32" - NEW
            "alerts/1342/matches/35" - SOLVED BY ANALYST
        ],
        strategy="strategy/back_test"
    } -> {
        solution="POTENTIAL_TRUE_POSITIVE"
        matches= [
            "alerts/1342/matches/32"
        ]
    }

    ae -> commentator
    GET commentator/comment {
        alert="alerts/1342"
        analysis="analysis/829",
        solution="POTENTIAL_TRUE_POSITIVE"
        agent_configuration_set="configurations/3544"
        matches= [
            "alerts/1342/matches/32"
        ]
    } -> {
        comment="string"
    }

    commentator -> agent facade
    GET agents/name/inputs {
        match="alerts/1342/matches/32",
        agent_configuration_set="configurations/3544"
    } -> {
        match="alerts/1342/matches/32",
        instance_name="advname_indv",
        alerted_names=["John Smith"],
        matched_names=["Jon Schmit"]
    }

    commentator -> agent facade
    GET agents/name/reasons {
        match="alerts/1342/matches/32",
        agent_configuration_set="configurations/3544"
    } -> {
        resons={...}
    }

#### 7. Viewing analysis state

    GET analysis/829 -> {
        name="analysis/829",
        policy="policy/54317992-a203-4e95-b145-b47d6a3aef0a",
        strategy="strategy/back_test",
        create_time="2021-02-17T07:18:48Z",
        state=RUNNING,
        alert_pending=54321,
        alert_count=63125
    }

A few minutes later...

    GET analysis/829 -> {
        name="analysis/829",
        policy="policy/54317992-a203-4e95-b145-b47d6a3aef0a",
        strategy="strategy/back_test",
        create_time="2021-02-17T07:18:48Z",
        state=DONE,
        alert_count=63125
    }

#### 8. Viewing recommendations

    GET analysis/829/recommendations -> [...]


### Simulation Use Cases

#### 1. Creating policy

    GET categories
    GET categories/source_system/values
    GET categories/country/values
    GET categories/customer_type/values
    GET categories/hit_category/values
    GET agents (from agent facade)
    GET agents/name_4/values (from agent facade)

Above calls introspect available values of each category and agent.
Those values are used for auto-completion in policy editor.

That also suggests that agents, and their versions might need to be coupled to policy.

#### 2. Creating data sets:

    POST datasets {name="training", start_time="...", end_time="..."}
    POST datasets {name="validation", start_time="...", end_time="..."}

> NOTE
>
> A dataset contains alerts, which - for the PoV case - are static, i.e., there is no constant inflow of alerts.
> Once all alerts are in the system for PoV, the dataset does not need to change.
> Hence, all there is to do is to `POST alerts` (via gRPC) and then `POST datasets` (via Web Frontend).

    GET datasets/453/alerts:count -> {
        alert_count=53464
    }


#### 3. Viewing analysis metrics

    GET analysis/829/metrics -> [
        "analysis/829/metrics/efficiency",
        "analysis/829/metrics/effectiveness",
    ]

    GET analysis/829/metrics/efficiency -> {
        efficiency=0.363,
        solved_alerts=3632,
        alert_count=10000,
    }

### PoV Use Cases

> NOTE
>
> Some cases below have no example data for the sake of brevity.

#### 1. Initial alert import

    POST alerts:batchCreate
    POST alerts/*/matches:batchCreate
    PUT alerts/*/matches/*/categories:batchUpdate

#### 2. ETLing agent inputs

    POST agents/*/inputs:batchCreate

#### 3. Viewing data availability

    GET alerts:count -> {
        alert_count=63125
    }
    GET agents/date_1_2_34/data-availability -> {
        alert_count=49238, percentage=0.78
    }

#### 4. ETLing analyst solutions

    POST solutions {
        alert_id="AE_BTCH_PEPL!1391FFFD-2E8A4AC7-B0E596E6-2FE1CEC1"
        solution=ESCALATE,
        solve_time="2021-02-17T07:13:47Z"
    }

or

    POST solutions {
        alert="alerts/1342",
        solution=ESCALATE,
        solve_time="2021-02-17T07:13:47Z",
    }

The `POST solutions` can be extended with `POST solutions:batchCreate`.

#### 5. Creating analysis

First step is to create Analysis:

    POST analysis {
        policy="policy/54317992-a203-4e95-b145-b47d6a3aef0a",
        strategy="strategy/back_test"
    } -> {
        name="analysis/829",
        policy="policy/54317992-a203-4e95-b145-b47d6a3aef0a",
        strategy="strategy/back_test",
        create_time="2021-02-17T07:18:48Z",
        state=NEW
    }

Then add datasets to analysis:

    POST analysis/829/datasets {
        dataset="datasets/e572ca87-e93f-4e17-8954-d2c9892055ad"
    } -> {
        name="analysis/829",
        policy="policy/54317992-a203-4e95-b145-b47d6a3aef0a",
        strategy="strategy/back_test",
        create_time="2021-02-17T07:18:48Z",
        state=PLANNING,
        alert_pending=40125,
        alert_count=63125
    }

Adding dataset to analysis will trigger the solving process.

#### 6. Viewing analysis state

    GET analysis/829 -> {
        name="analysis/829",
        policy="policy/54317992-a203-4e95-b145-b47d6a3aef0a",
        strategy="strategy/back_test",
        create_time="2021-02-17T07:18:48Z",
        state=RUNNING,
        alert_pending=54321,
        alert_count=63125
    }

A few minutes later...

    GET analysis/829 -> {
        name="analysis/829",
        policy="policy/54317992-a203-4e95-b145-b47d6a3aef0a",
        strategy="strategy/back_test",
        create_time="2021-02-17T07:18:48Z",
        state=DONE,
        alert_count=63125
    }

#### 7. Viewing recommendations

    GET analysis/829/recommendations -> [...]


#### 8. Adding a new prototype agent (a.k.a. custom feature)

    POST agents {agent_name="custom_agent"}
    POST agents/custom_agent/values {
        match="alerts/1342/matches/32",
        result="CUSTOM_VALUE",
        reason=struct.Value
    }  
    POST analysis

#### 9. Re-ETLing input data for an agent

    POST agents/country_2_3_4/input:batchCreate

- No need to store previous versions of overwritten input data
- Do not remove agent results when input changes, just invalidate it


#### 10. Discrepancies between analysis

It will not be implemented in the first place.



### Reports

Report file MUST contain all fields described in a spreadsheet: https://docs.google.com/spreadsheets/d/1jstrLwYZV9QnRbZaaaCqfLngtJyeswlYPwrfdXkioII/edit#gid=181237116&range=A2

#### 1. DAD

    POST datasets {name="past_year_dataset", start_time="...", end_time="..."} -> {
        name="datasets/e572ca87-e93f-4e17-8954-d2c9892055ad"
        create_time="2021-02-17T07:14:48Z",
        alert_count=40030450
    }

    GET datasets/e572ca87-e93f-4e17-8954-d2c9892055ad/reports -> [
        "dad",
    ]

    GET datasets/e572ca87-e93f-4e17-8954-d2c9892055ad/reports/dad -> {
        name = "datasets/e572ca87-e93f-4e17-8954-d2c9892055ad/reports/dad",
        dataset = "datasets/e572ca87-e93f-4e17-8954-d2c9892055ad"
        report_name = "data_availability_diagnostic_report_dataset_e572ca87-e93f-4e17-8954-d2c9892055ad.csv"
        url = "https://some.domain/path/to/report/through/service/to/check/permission"
    }  

#### 2. rb_scorer

    GET reports -> [
        "rbscorer", (maybe we should rename it to "feature_vectors_report")
        "ai_reasoning_al",
        "ai_reasoning_wl",
        "efficiency",
        "accuracy_alert",
        "accuracy_watchlist",
        "analysis_360",
        "backtest",
        "recommendation"
    ]

    GET reports/rbscorer {
        analysis = [
            "analysis/829",
            "analysis/4543"
        ]
    } -> {
        name = "reports/rbscorer/3442",
        analysis = [
            "analysis/829",
            "analysis/4543"
        ]
        report_name = "rb_scorer_3442.csv"
        url = "https://some.domain/path/to/report/through/service/to/check/permission"
    }






### Agents configuration


#### 1. Creating agent configuration set

    POST /agent-config-sets {
        agents=[
            {
                agent="agents/name_4_2_42"
                config="agents/name_4_2_42/configs/3"
            },  
            {
                agent="agents/date_1_2_34"
                config="agents/date_1_2_34/configs/2"
            }  
        ]  
    }


### Analytics comments


### Generating comments


### Batch API

Some APIs may accept batch requests for client convenience.

In order to invoke bulk operation use custom `batch<method>` operator applied to collection,
where `<method>` is the corresponding singular operation, e.g.:
- `GET alerts/<id>` -> `GET alerts:batchGet`
- `POST alerts` -> `POST alerts:batchPost`
- `GET alerts:search` -> `GET alerts:batchSearch`

Each batch request contains a collection of singular requests and returns collection
of singular responses. A successful request indicates that all singular requests have been
successfully accepted for further processing. If at least one of the singular requests is rejected,
an entire request is considered to be rejected.

Example:

    POST alerts:batchPost {
        createRequests: [
            {
                 alert_id="AE_BTCH_PEPL!1391FFFD-2E8A4AC7-B0E596E6-2FE1CEC1",
                 alert_time="2021-02-16T21:25:34Z"  
            },
            {
                 ...
            }
        ]
    } -> {
        alerts: [
            {
                name="alerts/1342",
                alert_id="AE_BTCH_PEPL!1391FFFD-2E8A4AC7-B0E596E6-2FE1CEC1",
                alert_time="2021-02-16T21:25:34Z",
                create_time="2021-02-16T23:02:45Z"
            },
            {
                ...
            }
       ]
    }

### To do

- RBScore
- Analyst Reasoning
- Report - feature vector + analyst reasoning
- Analysis - real_time_switch - should an analysis continuously recalculate the results? state=OUTDATED
- Comment generation loops until memory runs out.
- It is hard to debug the comment template problems.
- Multiple alerted parties per match issue from MS.
- Strategies (reduction logic) to be configurable.
  - Invoke external service for reducing matches.
  - Two ways to do it: provide minimal data to strategy, like IDs of matches as well as their state etc, but allow the service to query the match from AE.

### Category

    categories/*
    categories/*/values

Categories can be used to create data sets, i.e., group alerts (matches as well?) in such a way that aggregates can be calculated over such a group.

Categories can be also treated like labels or tags, e.g., having source system named ECDD, category Source system and value ECDD can be turned into tag like this: `source_system_ECDD`.

Category has:
- Unique identifier
- Target resource (type of target resource this cate, i.e., enum; e.g., alert category, match/hit category, watchlist party category)
- Name - short, unique name of a category (e.g., branch, account country, cust_nat, etc.)
- Description - optional, human-readable description (e.g. Bank branch, Account country, Customer nationality)
- Multi-valued - whether there might be multiple values for the same category in the same resource. E.g., "source system" shall not be multi-valued, but "customer nationality" or "customer country of residence" for sure can.

Category examples:
- Bank branch
- Account country
- Source system
- Customer country of residence
- Customer nationality
- Company country of registration
- Customer type (individual, organization, etc.)
- Segment (corporate, consumer, wealth management)
- Watchlist party category (Sanctions, PEP)
- Watchlist party type (individual, organization, etc.)

#### Remarks

- The idea is to have a way to set multiple values in the same category to a single resource type, i.e., an alert can be triggered on a customer which has 2 or more countries of residence.
  This translates to "labels" like `residence_PL` and `residence_UK` for the same resource. As that might be harder on data processing side, shall we consider limiting it to a single value per category? That is to remove the "multi-valued" property of a category.

- The categories serve as a way to dynamically create groupings of alerts, and there is no need to limit possible values in given category.
  But, given that the data might be dirty, after importing resources, we might end up with the category "customer nationality" having values like PL, Poland, and Republic of Poland, all pointing the same country.
  Having category values easily updateable, the plan is to have a way to request "update category /customer nationality/ values /Poland/ and /Republic of Poland/ to /PL/" without having to ETL the resources again.
  This leads to another feature: once such an "update" is invoked, the service could remember it and convert all "Polands" to "PL" in a given category for all future resources, and such a conversion-table can be something that is "learned" during PoV and transferred to production.
  Just an idea though.

### Alert

Mixed data.

Alert has:
- External id
- Alerted date
- Matches

### Match

Mixed data.

Match has:
- Hit id

### Agent

### Agent Input

Mixed data.

### Agent Result

Categorical data.

### Agent Reason

Non-categorical data.

### Customer?


### Data Set?


## Use cases

### PoV ETL

1. Import alerts and matches
2. Add categories (for alerts and matches)
3. Add agent inputs
4. Iterate: adding new categories, new agent inputs, inject agent results

- Crucial artifacts (ceilings)
- Additional ceiling: agent performance ceiling, takes into account how agents perform. Need to consider that the agents are not ideal and the ETL is not ideal as well.
- Previous versions of artifacts (ceilings) to compare.
- MD5 from CSV
- Observing alerts in time
- Data points did not change - no need to reevaluate, maybe can reapply previous solution.

### Real-time


### Batch

(SCB)

Additional extracts

### Simulation


## Notes from meeting

### Agenda

- How does an Agent input looks like? Is it proto from AE, or directly from Agent?

- Agent configurations and their relation to policies - there is a circular dependency that needs to be solved.

- Analysts' comments and solutions - is the current proposal OK?

- Reports - think where they are gonna sit in as (collection?) of resources and how they'll get generated

- Generating comments - CommentInput resource

- List of missing use cases - there are plenty of use cases we're using SERP today for, let's draw from that experience and list them. Another source of inspiration could be Cloud API, POV flow, SCB TSaaS Bridge.

### Too much work

1. Bridge dostaje z HSBC 100 alertów (zapisując je sobie do bazy)
2. Bridge wysyła do AE 100 Alertów (batchCreate)
3. Bridge wysyła do AE xxx Matchy (batchCreate)
4. Bridge wysyła do AE inputy agentów (batchCreate)
5. Bridge tworzy w AE dataset z ww. 100 Alertów
6. Bridge tworzy w AE analizę z policy i strategy
7. Bridge wrzuca dataset do analizy
8. Bridge czeka na zakończenie analizy
9. Bridge ściąga rekomendacje
10. Bridge odsyła rekomendację do HSBC


### Less work

1. Bridge dostaje z HSBC 100 alertów (zapisując je sobie do bazy)
2. Bridge wysyła do AE 100 Alertów (batchCreate)
3. Bridge wysyła do AE xxx Matchy (batchCreate)
4. Bridge tworzy w AE dataset z ww. 100 Alertów
5. Bridge tworzy w AE analizę z policy, strategy i agent config+versions(service names?):
   ```
    policy="policy/54317992-a203-4e95-b145-b47d6a3aef0a",
    strategy="strategy/back_test"
    agent_config_set="agent-config-sets/3544" // contains all agents configuration, agent_version (name=4.4.42, country=5.4.3, ...)
   ```
6. Bridge wrzuca dataset do analizy
  1. AE prosi fasadę agentów o rozwiązanie
   ```
   POST ...:solve {
       match="alerts/1234/matches/5678"
       agent_config_set="agent-config-sets/3544",
       agents=[
           "agents/name_4_2_42",
           "agents/date_1_2_34"
       ]  
   } -> [
        {
            agent="agents/name_4_2_42"
            result="NO_MATCH"
            reason=google.Value
        },  
        {
            agent="agents/date_1_2_34"
            result="MATCH"
            reason=google.Value
        }  
   ]
   ```
  2. Name Agent zapyta DataSource API o input
   ```
   POST ... {
       match="alerts/1234/matches/5678"
       fields=[
           "alert-fields/666",  
           "match-fields/21",
           "match-fields/22"  
       ]  
   } -> {

   }
   ```
7. Bridge czeka na zakończenie analizy
8. Bridge ściąga rekomendacje
9. Bridge odsyła rekomendację do HSBC


## Data Source API

The resources available in Data Source:

-  A collection of alert fields: `alert-fields/*`.
-  A collection of match fields: `match-fields/*`.


### Data Source Transformation

#### INPUT:

JSON via REST API:

    {
        "alert": {
            "fullName": "Global Corporation Pte. Ltd."
            "matches": [
                {
                    "id": "1273481",
                    "matchedText": "Global Co Limited"
                }  
            ]  
        }  
    }

#### DATABASE:

    INSERT INTO alerts VALUE json

#### AGENT REQUEST TO DS:

    GET ???

    SELECT json FROM alerts

    -> {
        instance_name="advname-org",
        alerted_names=["Global Corporation Pte. Ltd."],
        matched_names=["Global Co Limited"],
    }


### DATA SOURCE API

Name Agent Input v1:

        alerted_names=["John Smith"],
        matched_names=["Jon Schmit"],
        instance_name=""  

Name Agent Input v2:

        alerted_names=["John Smith"],
        matched_names=["Jon Schmit"],
        shoe_size=42,
        instance_name=""  

Commentator Input v1:

    GET alerts/*/matches/*/comment-input/v1 -> {
        ...
    }


### NAME AGENT API:

Name Agent Request:

        match="alerts/1234/matches/5678"

Name Agent Response:

        result="NO_MATCH"
        reason=google.protobuf.Value  


### RECOMMENDATION METADATA

An example of recommendation metadata:

    {
      name="analysis/123/recommendations/456/metadata",
      alert="alerts/789",
      matches=[
        {
            match="alerts/789/matches/1234",
            solution="SOLUTION_FALSE_POSITIVE",
            reason={
                feature_vector_signature="J4VGkp1+FaNsaGDtBXgQsWpUYDo=",
                policy="policies/5afc2f12-85c0-4fb3-992e-1552ac843ceb",
                step="steps/e6ceb774-ab56-4576-b653-1cdceb2d25e7"
            },
            categories={
                categories/isDeny="YES",
                categories/partyType="I"
            },
            features={
                features/name={
                    agentConfig="agents/name/versions/1.0.0/configs/1",
                    solution="EXACT_MATCH",
                    reason={}
                },
                features/dateOfBirth={
                    agentConfig="agents/date/versions/1.0.0/configs/1",
                    solution="NO_MATCH",
                    reason={}
                }
            }
        }
      ]
    }



## Discussion with MW

- Data Source API udostępnia listy pól w alercie i matchu do Web UI
- W Web UI użytkownik konfiguruje agenta: na przykład ustawia Name Agentowi, że "alerted_name" (które jest wymaganym wejściem deklarowanym przez Name Agenta) znajduje się w "match-field/22"
- Taka konfiguracja jest zapisywana w Name Agencie
- Gdy AE pyta Name Agent'a o rozwiązanie dla matcha "alerts/1234/matches/5678", Name Agent odpytuje Data Source API między innymi o "match-fields/21", aby wygenerować rozwiązanie

Michael Wilkowski  7:55 PM
w rozdziale less work nie widzę, gdzie podawane są inputy agentów

Andrzej Haczewski:spiral_calendar_pad:  7:56 PM
Bo nie ma - agent sam pyta o swój input na podstawie konfiguracji

Michael Wilkowski  7:56 PM
no, to nie wygląda to na razie na PoV friendly :slightly_smiling_face:
7:56
chyba, że ten Data Source to będzie komponent, który będzie PoV friendly i jego będzie można zasilić konkretnym scenariuszem i on sprawi, że PoV będzie zadowolone, bo alert zostanie rozwiązany tak, jak PoV chce

Andrzej Haczewski:spiral_calendar_pad:  7:57 PM
Niesie to ze sobą jeden bardzo pożądany skutek jeżeli chodzi o HSBC Bridge - transformacja z danych wejściowych jest stała i testowalna
7:57
No właśnie dlatego o tym wyżej pisałem - o ile można zdefiniować Data Source API, to musiałoby mieć wiele implementacji - jedno w HSBC Bridge'u, ale drugą implementację specjalną na potrzeby POV (edited)

Michael Wilkowski  7:58 PM
rozumiem, że Data Source API to w praktyce data platform, które martwi się transformacjami, albo podaje dane "surowo" tak jak dostało, w zależności od naszych potrzeb, tak?

Andrzej Haczewski:spiral_calendar_pad:  7:58 PM
dane są też pobierane w modelu "pull", co wymusza jakąś szczątkową indeksację tych danych, dlatego flat CSV pewnie musiałyby w pierwszej kolejności wjechać do bazy


5 replies
Last reply today at 12:17 AMView thread

Andrzej Haczewski:spiral_calendar_pad:  7:59 PM
Ekhem... padło dzisiaj na callu stwierdzenie, że to Data Platform :wink:

Michael Wilkowski  7:59 PM
chcę zrozumieć wartość posiadania takiego bytu
7:59
przewagę tego nad po prostu załadowaniem danych directowo do AE
8:00
no i druga sprawa - czy Data Source zapewnia, że dana w ramach danego Alert ID jest stała?
8:00
pewnie niekoniecznie
8:00
czyli AE musi ZAWSZE odpytać Data Source, czy tam się coś nie zmieniło, aby wiedziało, czy odpalać agenta, czy tak?

Andrzej Haczewski:spiral_calendar_pad:  8:00 PM
jest kilka:
1. AE nie musi w ogóle nic wiedzieć o inputach agentów - nie przetrzymuje ich w żaden sposób, nie musi mieć też mechanizmu ich transportowania do agentów

Michael Wilkowski  8:01 PM
jeśli AE ich nie przetrzymuje to kto cache'uje inputy/outputy agentów, aby ich nie wykonywać wielokrotnie?

Andrzej Haczewski:spiral_calendar_pad:  8:01 PM
2. Agenty, zmieniając wewnętrzną implementację, nie narzucają problematycznych zmian w AE.
   8:01
   w tym scenariuszu AE przetrzymuje tylko outputy agentów: result, i reason

Michael Wilkowski  8:02 PM
a kiedy AE wie, czy input agenta się zmienił, tzn. że data source powinno podać nową wartość?
8:02
trzyma hasha inputu i tym odpytuje Data Source (lub coś w stylu Last-Modified itd.)

Andrzej Haczewski:spiral_calendar_pad:  8:03 PM
tutaj - podobnie jak w przypadku, gdy AE trzymałoby inputy agentów - output musiałby być inwalidowany z zewnątrz, albo - co chyba jest bardziej naturalnym rozwiązaniem - output automatycznie musiałby być wygenerowany, ergo agent odpytany, gdyby coś w Analysis się zmieniło: polityka, strategia, albo konfiguracje / wersje agentów

Michael Wilkowski  8:04 PM
ok, rozumiem, dzięki
8:04
i widzę, że solve odpalamy na żądanie, gdy ktoś chce otrzymać dane, a nie "z automatu"?
8:04
proszę, abyście przewidzieli jeszcze jakiś cancel
8:04
bo to na pewno będzie potrzebne :slightly_smiling_face:

Andrzej Haczewski:spiral_calendar_pad:  8:05 PM
tak, odpalany solve tylko gdy  potrzeba danych

Michael Wilkowski  8:05 PM
będzie podgląd outputu "na bieżąco", czyli zobaczę output pierwszych 1000 alertów bez potrzeby czekania na cały set (np. 100k)?
8:06
stały scenariusz (na POV), który widzę jest taki:
setup danych
odpalamy solve (100k)
patrzymy kontrolnie pierwsze 1000 alertów
k...a, ziutek, zapomniałeś ustawić drzewo! cancel
jeszcze raz setup
solve
itd.

Andrzej Haczewski:spiral_calendar_pad:  8:07 PM
oczywiście, AE będzie odpytywało pojedynczo  lub w batchach o rozwiązania agentów - tutaj też było to przedmiotem dyskusji, natomiast zgodziliśmy się, że może to się odbywać asynchronicznie, i jak tylko będą do AE spływać outputy agentów, to na bieżąco można te dane oglądać
