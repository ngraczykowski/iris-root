import { Component } from '@angular/core';
import { TablePageLoader } from '@app/components/pageable-dynamic-table/simple-table-data-provider';
import { LocalEventService } from '@app/shared/event/local-event.service';
import {
  approvalFailedEvent,
  approvalSuccessfulEvent,
  rejectFailedEvent,
  rejectSuccessfulEvent
} from '@app/templates/approver/approver-panel/approver-panel.events';
import { ChangeRequest } from '@app/templates/approver/approver.model';
import { TranslateService } from '@ngx-translate/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ApproverStore } from '../approver.store';

@Component({
  selector: 'app-approver-panel',
  templateUrl: './approver-panel.html',
  styleUrls: ['./approver-panel.scss'],
})
export class ApproverPanelComponent {

  comment: string;

  constructor(private store: ApproverStore, private translateService: TranslateService,
              private eventService: LocalEventService) {

  }

  openChangelog({decisionTreeId, matchGroupId}) {
    this.store.openChangelog({decisionTreeId, matchGroupId});
  }

  getTablePageLoader(): TablePageLoader<ChangeRequest> {
    return {
      load: (page, size) => this.store.getSelectedChangesPage(page, size)
    };
  }

  close() {
    this.store.closeChangesApprovalPanel();
  }

  get errors() {
    return this.store.changesApprovalPanel$.pipe(map(panel => panel.error));
  }

  get inProgress(): Observable<boolean> {
    return this.store.changesApprovalPanel$.pipe(map(panel => panel.isProcessing));
  }

  reject() {
    const confimationMessage = this.translateService.instant(
        'approver.rejectSelected.confirmations.reject');

    if (confirm(confimationMessage)) {
      this.store.rejectSelectedChanges(this.comment)
          .subscribe(
              () => {
                this.store.closeChangesApprovalPanel();
                this.store.fetchChangesQueue();
                this.eventService.sendEvent(rejectSuccessfulEvent);
              },
              () => this.eventService.sendEvent(rejectFailedEvent)
          );
    }
  }

  commentIsEmpty() {
    return !this.comment || this.comment === '';
  }

  approve() {
    const confimationMessage = this.translateService.instant(
        'approver.approveSelected.confirmations.approve');

    if (confirm(confimationMessage)) {
      this.store.approveSelectedChanges(this.comment)
          .subscribe(
              () => {
                this.store.closeChangesApprovalPanel();
                this.store.fetchChangesQueue();
                this.eventService.sendEvent(approvalSuccessfulEvent);
              },
              () => this.eventService.sendEvent(approvalFailedEvent)
          );
    }
  }

  isReject() {
    return this.store.isReject();
  }

  getTranslationPath(suffix): string {
    const prefix = 'approver';
    const value = this.isReject() ? 'rejectSelected' : 'approveSelected';
    return `${prefix}.${value}.${suffix}`;
  }
}
