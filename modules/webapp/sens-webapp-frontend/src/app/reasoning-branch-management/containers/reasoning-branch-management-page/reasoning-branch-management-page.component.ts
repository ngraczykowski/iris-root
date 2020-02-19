import { Component, OnInit } from '@angular/core';
import { EventKey } from '@app/shared/event/event.service.model';
import { LocalEventService } from '@app/shared/event/local-event.service';

@Component({
  selector: 'app-reasoning-branch-management-page',
  templateUrl: './reasoning-branch-management-page.component.html',
  styleUrls: ['./reasoning-branch-management-page.component.scss']
})
export class ReasoningBranchManagementPageComponent implements OnInit {

  showConfirmWindow = false;

  showDetails = false;

  // Temporary
  branchDetails = {
    branchId: '1-546',
    statuses: [
      {label: 'Active', active: false},
      {label: 'Inactive', active: true}
    ],
    aiSolutions: [
      {label: 'False Positive', active: false},
      {label: 'Potential True Positive', active: true},
      {label: 'No Decision', active: false}
    ],
  };

  emptyStateMessage = {
    message: 'branch.emptyState.default.message',
    hint: 'branch.emptyState.default.description'
  };

  noResults = {
    message: 'branch.emptyState.noResults.message',
    hint: 'branch.emptyState.noResults.description'
  };

  constructor(private readonly eventService: LocalEventService) { }

  hideConfirm() {
    this.showConfirmWindow = false;
  }

  showConfirm() {
    this.showConfirmWindow = true;
  }

  applyChanges() {
    // TODO(kjaworowski): Send request (WA-349)
    this.hideConfirm();
    this.successfullyAppliedChanges(); // TODO(kjaworowski): Show feedback for success (WA-351)
    // this.failedAppliedChanges(); // TODO(kjaworowski): Show feedback when fail (WA-351)
  }

  successfullyAppliedChanges() {
    this.sendBriefMessage('branch.confirm.feedback.success');
  }

  failedAppliedChanges() {
    this.sendBriefMessage('branch.confirm.feedback.failed');
  }

  private sendBriefMessage(messageContent) {
    this.eventService.sendEvent({
      key: EventKey.NOTIFICATION,
      data: {
        message: messageContent
      }
    });
  }

  ngOnInit() {}
}
