#!/bin/bash
# export_ecm_learning.sh -- will export contents of view all_ecm_data_v
# DON'T USE "select *" because it could cause error if the view's structure is changed, causing a mismatch with the SERP table.
hive -e "use ecm_dat;select hit_unique_id, analyst_decision, analyst_actions, action_date, analyst_comments, system_id, ecm_batch_run_id, gns_batch_id, case_owner from all_ecm_data_v limit 100" >fff_decisions_wl.csv

result=`echo $?`
if [ $result -ne 0 ]; then
    echo "Hive error: $result">fff_decisions_wl.log
    exit 1
else
    PGPASSWORD=<password>  psql -h <host> -p <port> -d <database> -U <user> -c "\copy fff_decisions_wl(hit_unique_id,analyst_decision,analyst_actions,action_date,analyst_comments,system_id,ecm_batch_run_id,gns_batch_id,case_owner) from '/<path_to_file>/fff_decisions_wl.csv' with delimiter as E'\t'"
fi
