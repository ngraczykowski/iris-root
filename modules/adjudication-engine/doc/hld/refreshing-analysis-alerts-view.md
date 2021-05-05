# Refreshing analysis alerts view

The `ae_analysis_alert_query` is a materialized view that lists all alerts that belong to the analysis.
The only way to add or remove an alert from an analysis is by adding (or removing) analysis datasets.
Therefore, currently there is a trigger `ae_analysis_alert_dataset_trigger` on `ae_analysis_dataset` table, which refreshes the materialized view.

## Future improvements (TODO)

The refresh, due to being in a trigger, is synchronous, i.e, when client adds a dataset to an analysis, it has to wait for the refresh of the materialized view, which might take long for large datasets.

This refresh might not be required at the time of this writing.
We return the AnalysisDataset from the API, and this message does not contain anything that cannot be calculated on-the-fly.
This refresh therefore might be a leftover from the time when adding a dataset to an analysis returned the Analysis message, which have to return a number of alerts total and pending in an analysis.
