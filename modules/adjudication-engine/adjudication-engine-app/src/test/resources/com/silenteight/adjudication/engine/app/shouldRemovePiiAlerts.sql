/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

INSERT INTO ae_analysis
VALUES (888, 'policies/1', 'strategies/test', NOW())
ON CONFLICT DO NOTHING;

INSERT INTO ae_alert
VALUES (666, 'id', NOW(), NOW(), 1)
ON CONFLICT DO NOTHING;

INSERT INTO ae_match
VALUES (777, 666, NOW(), 'blablabla', 1)
ON CONFLICT DO NOTHING;

INSERT INTO ae_recommendation (analysis_id, alert_id, created_at, recommended_action,
                               match_ids, match_contexts, match_comments)
VALUES (888, 666, NOW(), 'TO_THE_MOON', ARRAY[]::integer[],
        '[{"reason": "bo tak"}]',
        '{"SIZADA4086(59, #1)": ""}')
ON CONFLICT DO NOTHING;

INSERT INTO ae_alert_comment_input(alert_id, created_at, value)
VALUES (666, now(), '{"asd":1}')
ON CONFLICT DO NOTHING;
