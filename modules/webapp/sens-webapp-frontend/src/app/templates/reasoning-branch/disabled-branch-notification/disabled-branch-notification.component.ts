import { Component, Input, OnInit } from '@angular/core';
import { AuthService } from '@app/shared/auth/auth.service';
import { finalize } from 'rxjs/operators';
import { Event, EventKey } from '../../../shared/event/event.service.model';
import { LocalEventService } from '../../../shared/event/local-event.service';
import { ErrorMapper } from '../../../shared/http/error-mapper';
import { InboxService } from '../../inbox/inbox.service';
import { InboxMessage } from '../../model/inbox.model';
import { DisabledBranchNotificationMapper } from './disabled-branch-notification-mapper';

export interface DisabledBranchNotification {
  messageKey: string;
  aiDecision: string;
  alertInfos: AlertInfo[];
  date: string;
  solved: boolean;
}

export interface AlertInfo {
  analystDecision: string;
  link: AlertLink;
}

export interface AlertLink {
  name: string;
  url: string;
}

@Component({
  selector: 'app-disabled-branch-notification',
  templateUrl: './disabled-branch-notification.component.html',
  styleUrls: ['./disabled-branch-notification.component.scss'],
  providers: [DisabledBranchNotificationMapper]
})
export class DisabledBranchNotificationComponent implements OnInit {

  private readonly type = 'REASONING_BRANCH_DISABLED';

  private readonly loadingErrorMapper = new ErrorMapper(
      {},
      'reasoningBranch.notification.message.error.load.');
  private readonly solvingErrorMapper = new ErrorMapper(
      {},
      'reasoningBranch.notification.message.error.solve.');

  @Input() decisionTreeId: number;
  @Input() matchGroupId: number;

  inProgressLoadingInboxMessage;
  inProgressMarkAsSolved;

  inboxMessageId: number;
  notification: DisabledBranchNotification;
  private hasInboxAuthorities: boolean;

  constructor(
      private inboxService: InboxService,
      private notificationMapper: DisabledBranchNotificationMapper,
      private eventService: LocalEventService,
      private authService: AuthService
  ) {
  }

  ngOnInit() {
    this.load();
  }

  markAsSolved() {
    this.inProgressMarkAsSolved = true;
    this.inboxService.markAsSolved(this.inboxMessageId)
        .pipe(finalize(() => this.inProgressMarkAsSolved = false))
        .subscribe(
            () => this.load(),
            e => this.handleSolvingError(e));
  }

  private getReferenceId() {
    return `${this.decisionTreeId}-${this.matchGroupId}`;
  }

  private load() {
    this.hasInboxAuthorities = this.authService.hasAccessToUrl('/inbox');
    this.inProgressLoadingInboxMessage = true;
    this.inboxService.getInboxMessage(this.type, this.getReferenceId())
        .pipe(finalize(() => this.inProgressLoadingInboxMessage = false))
        .subscribe(
            m => this.handleMessage(m),
            e => this.handleLoadingError(e));
  }

  private handleMessage(message: InboxMessage) {
    this.inboxMessageId = message.id;
    this.notification = this.notificationMapper.map(message);
  }

  private handleLoadingError(e) {
    if (e && e.error && e.error.key === 'EntityNotFoundException') {
      this.notification = null;
    } else {
      this.sendErrorEvent(this.loadingErrorMapper.get(e));
    }
  }

  private handleSolvingError(e) {
    this.sendErrorEvent(this.solvingErrorMapper.get(e));
  }

  private sendErrorEvent(message) {
    this.eventService.sendEvent(<Event>{
      key: EventKey.NOTIFICATION,
      data: {
        type: 'error',
        message: message
      }
    });
  }
}
