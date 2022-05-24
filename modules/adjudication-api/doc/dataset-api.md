## Dataset

Dataset is an immutable collection of alerts.
Alerts are associated with the Dataset by filters specified during creation.

## Use cases

### Create Dataset with Alerts filtered by names

    POST datasets {
        alert_names=["alerts/100", "alerts/101"]
    } -> {
        name="datasets/e572ca87-e93f-4e17-8954-d2c9892055ad"
        create_time="2021-02-17T07:14:48Z",
        alert_count=3400
    }

### Create Dataset with Alerts filtered by alert create time

    POST datasets {
        alert_time_range={
            start_time="2021-01-01T07:14:48Z",
            end_time="2021-02-17T07:14:48Z"
        }
    } -> {
        name="datasets/e572ca87-e93f-4e17-8954-d2c9892055ad"
        create_time="2021-02-17T07:14:48Z",
        alert_count=3400
    }

### Create Dataset with Alerts filtered by alert labels

    POST datasets {
        alert_labels={
            labels={
                "country": "UK",
                "isDeny": "NO"
            }
        }
    } -> {
        name="datasets/e572ca87-e93f-4e17-8954-d2c9892055ad"
        create_time="2021-02-17T07:14:48Z",
        alert_count=3400
    }

### Get Dataset

    GET datasets/10 -> {
        name="datasets/10",
        create_time="2021-02-16T21:25:34Z",
        alert_count=12354
    }

### List Datasets

    GET datasets {
        page_size=1000
        page_token=""
    } -> {
        datasets=[
            "datasets/10",
            "datasets/11"
        ],
        next_page_token=""
    }

### List Alerts in Dataset

    GET datasets/10/alerts {
        page_size=1000
        page_token=""
    } -> {
        dataset_alert_names=[
            "datasets/10/alerts/100",
            "datasets/10/alerts/101"
        ],
        next_page_token=""
    }

## Open questions

1. What are logical operators between all conditions: labels, alert_time_start, alert_time_end.

   Current status: Only single selected condition is supported.

2. Is it be possible to delete Dataset?

   Current status: No.
