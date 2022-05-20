Adjudication Engine Trace
===

Handle Trace

Configuration
===
---

- `ae.solving.journal.enabled` - default value: `false`, options `[false, true]` 

Hash calculation
===
---
To calculate hash of the payload You need to pass `recommendation` property in Message `payload` properties.
Hash calculation is based on the `recommendation` properties.
When You don't pass `recommendation` the hash will be NULL.
