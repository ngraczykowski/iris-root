import { InboxMessage } from '../../../../model/inbox.model';
import { DisabledBranchDescriptionMapper } from './disabled-branch-description-mapper';
import { DisabledBranchDescription } from './disabled-branch-description-view.component';

describe('DisabledBranchDescriptionMapper', () => {
  let mapper: DisabledBranchDescriptionMapper;

  beforeEach(() => {
    mapper = new DisabledBranchDescriptionMapper();
  });

  it('should map response correctly', () => {
    const message = <InboxMessage> {
      id: 1,
      type: 'REASONING_BRANCH_DISABLED',
      referenceId: '1-2',
      message: 'messageKey',
      extra: {
        decisionTreeId: 1,
        matchGroupId: 2,
        decisionTreeName: 'name',
        suspendingAlerts: [
          {externalId: 'externalId1', analystDecision: 'analystDecision1'},
          {externalId: 'externalId2', analystDecision: 'analystDecision2'}
        ],
        aiDecision: 'aiDecision'
      }
    };
    const expectedDescription = <DisabledBranchDescription> {
      messageKey: 'inbox.message.REASONING_BRANCH_DISABLED.messageKey',
      aiDecision: 'aiDecision',
      branchLink: {
        name: 'Reasoning Branch 1-2',
        url: '/decision-tree/1/reasoning-branch/2'
      },
      alertInfos: [
        {
          analystDecision: 'analystDecision1',
          link: {
            name: 'externalId1',
            url: '/decision-tree/1/reasoning-branch/2/alert/externalId1'
          }
        },
        {
          analystDecision: 'analystDecision2',
          link: {
            name: 'externalId2',
            url: '/decision-tree/1/reasoning-branch/2/alert/externalId2'
          }
        }
      ]
    };

    expect(mapper.map(message)).toEqual(expectedDescription);
  });
});
