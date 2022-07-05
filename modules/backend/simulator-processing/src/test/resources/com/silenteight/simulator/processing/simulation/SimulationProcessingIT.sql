/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

insert into simulator_simulation(simulation_id, name, description, model_name, analysis_name, state,
                                 created_by, created_at, started_at, finished_at, solved_alerts,
                                 updated_at)
values ('a9b45451-6fde-4832-8dc0-d17b4708d8ca', 'symulacja 1', null, 'asd', 'analysis/1', 'RUNNING',
        'ppietrucha', now(), now(), null, 1,
        null);

insert into simulator_indexed_alert(analysis_name, state, alert_count, created_at, request_id,
                                    updated_at)
values ('analysis/1', 'SENT', 1, now(), 'b4708d8c-4832-6fde-8dc0-d17b4708d8ca', null);


--  ANALYSIS_FINISHED
insert into simulator_simulation(simulation_id, name, description, model_name, analysis_name, state,
                                 created_by, created_at, started_at, finished_at, solved_alerts,
                                 updated_at)
values ('b9b45451-6fde-4832-8dc0-d17b4708d8ca', 'symulacja 2', null, 'asd', 'analysis/2', 'RUNNING',
        'ppietrucha', now(), now(), null, 1,
        null);

insert into simulator_indexed_alert(analysis_name, state, alert_count, created_at, request_id,
                                    updated_at)
values ('analysis/2', 'ACKED', 1, now(), 'c4708d8c-4832-6fde-8dc0-d17b4708d8ca', null);

insert into simulator_indexed_alert(analysis_name, state, alert_count, created_at, request_id,
                                    updated_at)
values ('analysis/2', 'ACKED', 1, now(), 'd4708d8c-4832-6fde-8dc0-d17b4708d8ca', null);

-- ANALYSIS not
insert into simulator_simulation(simulation_id, name, description, model_name, analysis_name, state,
                                 created_by, created_at, started_at, finished_at, solved_alerts,
                                 updated_at)
values ('c9b45451-6fde-4832-8dc0-d17b4708d8ca', 'symulacja 3', null, 'asd', 'analysis/3',
        'FINISHED',
        'ppietrucha', now(), now(), null, 1,
        null);
