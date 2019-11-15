import { Component} from '@angular/core';
import { TablePageLoader } from '@app/components/pageable-dynamic-table/simple-table-data-provider';
import { SelectableItemStore } from '@app/components/selectable-dynamic-table/selectable-item-store';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ChangeRequest } from './approver.model';
import { ApproverStore, ApprovingPanel, Changelog, ChangesQueue } from './approver.store';

@Component({
  selector: 'app-approver',
  templateUrl: './approver.component.html',
  styleUrls: ['./approver.component.scss']
})
export class ApproverComponent {
  constructor (
    private store: ApproverStore
  ) {
    store.fetchChangesQueue();
  }

  openChangelog({decisionTreeId, matchGroupId}) {
    this.store.openChangelog({decisionTreeId, matchGroupId});
  }

  closeChangelog() {
    this.store.closeChangelog();
  }

  getChangelog(): Observable<Changelog> {
    return this.store.changelog$;
  }

  changelogIsOpen(): Observable<boolean> {
    return this.store.changelog$.pipe(
        map(changelog => changelog.isOpen)
    );
  }

  getTablePageLoader(): TablePageLoader<ChangeRequest> {
    return {
      load: (page, size) => this.store.getChangesToApprovePage(page, size)
    };
  }

  changesApprovalPanelIsOpen(): Observable<boolean> {
    return this.store.changesApprovalPanel$.pipe(
        map(panel => panel.isOpen)
    );
  }

  getChangesApprovalPanel(): Observable<ApprovingPanel> {
    return this.store.changesApprovalPanel$;
  }

  getApprovalChangesQueue(): Observable<ChangesQueue> {
    return this.store.approvalChangesQueue$;
  }

  getSelectedApprovalsCount(): Number {
    return this.store.selectedApprovals.length();
  }

  getSelectedApprovalsStore(): SelectableItemStore<ChangeRequest> {
    return this.store.selectedApprovals;
  }

  openChangesApprovalPanel() {
    this.store.openChangesApprovalPanel(true);
  }

  openChangesRejectPanel() {
    this.store.openChangesApprovalPanel(false);
  }
}
