# Alert API

The resources available in Alert API:

-  A collection of alerts: `alerts/*`. Each alert has the following resources:
   -  A collection of matches: `alerts/*/matches/*`. Each match has the following resources:

## Use Cases

### 1. Creating an Alert with a Match

    POST alerts {
        alert_id="AE_BTCH_PEPL!1391FFFD-2E8A4AC7-B0E596E6-2FE1CEC1", 
        alert_time="2021-02-16T21:25:34Z",
        labels={ 
            "batch"="AE_BTCH_PEPL", 
            "country"="AE"        
        }
    } -> {
        name="alerts/1342",
        alert_id="AE_BTCH_PEPL!1391FFFD-2E8A4AC7-B0E596E6-2FE1CEC1",
        alert_time="2021-02-16T21:25:34Z",
        create_time="2021-02-16T23:02:45Z",
        labels={
            "batch"="AE_BTCH_PEPL",
            "country"="AE"
        }
    }
    
    POST alerts/1342/matches {
        match_id="DB00013388"
    } -> {
        name="alerts/1342/matches/32", 
        match_id="DB00013388",
        create_time="2021-02-17T23:05:45Z"
    }


### 2. Creating Alerts in a batch

    POST alerts:batchCreate [
        {
            alert_id="AE_BTCH_PEPL!1391FFFD-2E8A4AC7-B0E596E6-2FE1CEC1", 
            alert_time="2021-02-16T21:25:34Z",
            labels={ 
                "batch"="AE_BTCH_PEPL" 
            }
        },
        {
            alert_id="AE_BTCH_DENY!284DB66D-09CC-4174-A541-A983E061770C", 
            alert_time="2021-02-17T21:25:34Z",
            labels={ 
                "batch"="AE_BTCH_DENY" 
            }
        },
    ] -> [
        {
            name="alerts/1342",
            alert_id="AE_BTCH_PEPL!1391FFFD-2E8A4AC7-B0E596E6-2FE1CEC1",
            alert_time="2021-02-16T21:25:34Z",
            create_time="2021-02-17T23:05:45Z",
            labels={
                "batch"="AE_BTCH_PEPL"
            }
        },
        {
            name="alerts/97143",
            alert_id="AE_BTCH_DENY!284DB66D-09CC-4174-A541-A983E061770C", 
            alert_time="2021-02-17T21:25:34Z",
            create_time="2021-02-17T23:05:45Z",
            labels={ 
                "batch"="AE_BTCH_DENY"
            }
        }
    ]


### 3. Creating Matches for a single Alert in a batch

    POST alerts/1343/matches:batchCreate [
        {
            match_id="DB00013388"
        },
        {
            match_id="AS00079876"
        }
    ] -> [
        {
            name="alerts/1342/matches/32", 
            match_id="DB00013388",
            create_time="2021-02-17T23:05:45Z"
        },
        {
            name="alerts/1342/matches/33", 
            match_id="AS00079876",
            create_time="2021-02-17T23:05:45Z"
        }
    ]


### 4. Creating Matches for multiple Alerts in a batch

    POST alerts/-/matches:batchCreate [
        {
            alert="alerts/1342",
            matches=[
                {
                    match_id="DB00013388"
                },
                {
                    match_id="AS00079876"
                }
            ]
        },
        {
            alert="alerts/97143",
            matches=[
                {
                    match_id="AN00013211"
                },
                {
                    match_id="GN00131324"
                }
            ]
        }                            
    ] -> [
        {
            name="alerts/1342/matches/32", 
            match_id="DB00013388",
            create_time="2021-02-17T23:05:45Z"
        },
        {
            name="alerts/1342/matches/33", 
            match_id="AS00079876",
            create_time="2021-02-17T23:05:45Z"
        },
        {
            name="alerts/97143/matches/34", 
            match_id="AN00013211",
            create_time="2021-02-17T23:05:45Z"
        },
        {
            name="alerts/97143/matches/35", 
            match_id="GN00131324",
            create_time="2021-02-17T23:05:45Z"
        }
    ]

