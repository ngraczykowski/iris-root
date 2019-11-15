import { DateFormatter } from '../../../shared/date/date-formatter';
import { DisabledBranchInboxMessageExtra, InboxMessage } from '../../model/inbox.model';
import {
  AlertInfo,
  AlertLink,
  DisabledBranchNotification
} from './disabled-branch-notification.component';

export class DisabledBranchNotificationMapper {

  private static buildLink(decisionTreeId: number, matchGroupId: number, alertId: string): AlertLink {
    return <AlertLink>{
      name: alertId,
      url: `/decision-tree/${decisionTreeId}/reasoning-branch/${matchGroupId}/alert/${alertId}`
    };
  }

  private static buildAlertInfos(extra: DisabledBranchInboxMessageExtra): AlertInfo[] {
    return extra.suspendingAlerts
        .map(alert => <AlertInfo>{
          analystDecision: alert.analystDecision,
          link: DisabledBranchNotificationMapper.buildLink(
              extra.decisionTreeId, extra.matchGroupId, alert.externalId
          )
        });
  }

  map(message: InboxMessage): DisabledBranchNotification {
    return <DisabledBranchNotification>{
      messageKey: `reasoningBranch.notification.rbDisabled.reason.${message.message}`,
      aiDecision: message.extra.aiDecision,
      alertInfos: DisabledBranchNotificationMapper.buildAlertInfos(message.extra),
      date: DateFormatter.format(message.date),
      solved: message.state === 'SOLVED'
    };
  }
}
