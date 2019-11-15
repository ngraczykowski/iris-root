import { DisabledBranchInboxMessageExtra, InboxMessage } from '../../../../model/inbox.model';
import {
  AlertInfo,
  AlertLink,
  DisabledBranchDescription
} from './disabled-branch-description-view.component';


export class DisabledBranchDescriptionMapper {

  private static createBranchLink(
      referenceId: string, extra: DisabledBranchInboxMessageExtra): AlertLink {

    return {
      name: `Reasoning Branch ${referenceId}`,
      url: `/decision-tree/${extra.decisionTreeId}/reasoning-branch/${extra.matchGroupId}`
    };
  }

  private static createAlertInfos(extra: DisabledBranchInboxMessageExtra): AlertInfo[] {
    return extra.suspendingAlerts.map(alert => <AlertInfo>{
      analystDecision: alert.analystDecision,
      link: {
        name: alert.externalId,
        url: `/decision-tree/${extra.decisionTreeId}/reasoning-branch/${extra.matchGroupId}/alert/${alert.externalId}`
      }
    });
  }

  public map(inboxMessage: InboxMessage): DisabledBranchDescription {
    return <DisabledBranchDescription> {
      messageKey: `inbox.message.${inboxMessage.type}.${inboxMessage.message}`,
      aiDecision: inboxMessage.extra.aiDecision,
      branchLink: DisabledBranchDescriptionMapper.createBranchLink(inboxMessage.referenceId, inboxMessage.extra),
      alertInfos: DisabledBranchDescriptionMapper.createAlertInfos(inboxMessage.extra)
    };
  }
}
