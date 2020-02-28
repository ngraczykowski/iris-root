import { Component, OnInit, ViewChild, ChangeDetectorRef } from '@angular/core';
import { EventKey } from '@app/shared/event/event.service.model';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { ReasoningBranchManagementService } from '@app/reasoning-branch-management/services/reasoning-branch-management.service';
import { ReasoningBranchDetails } from '@app/reasoning-branch-management/models/reasoning-branch-management';
import { BranchDetailsComponent } from '@app/reasoning-branch-management/components/branch-details/branch-details.component';
import { LoadBranchComponent } from '@app/reasoning-branch-management/components/load-branch/load-branch.component';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-reasoning-branch-management-page',
  templateUrl: './reasoning-branch-management-page.component.html',
  styleUrls: ['./reasoning-branch-management-page.component.scss']
})
export class ReasoningBranchManagementPageComponent implements OnInit {
  showConfirmWindow = false;
  showDetails = false;
  showNoResults = false;
  branchDetails: ReasoningBranchDetails;

  @ViewChild('branchDetailsForm', { static: false }) branchDetailsForm: BranchDetailsComponent;
  @ViewChild('loadBranch', { static: true }) loadBranch: LoadBranchComponent;

  emptyStateMessage = {
    message: 'branch.emptyState.default.message',
    hint: 'branch.emptyState.default.description'
  };

  noResults = {
    message: 'branch.emptyState.noResults.message',
    hint: 'branch.emptyState.noResults.description'
  };

  constructor(
    private readonly eventService: LocalEventService,
    private readonly reasoningBranchManagementService: ReasoningBranchManagementService,
    private activatedRoute: ActivatedRoute
  ) { }

  ngOnInit() {
    this.activatedRoute.params.subscribe(params => {
      if (params.id) {
        this.onLoadBranchSubmitClicked(params.id);
        this.loadBranch.enteredID = params.id;
        this.loadBranch.validateInput(this.loadBranch.fullIdCheck);
      }
    });
  }

  hideConfirm() {
    this.showConfirmWindow = false;
  }

  showConfirm() {
    this.showConfirmWindow = true;
  }

  applyChanges() {
    this.updateReasoningBranch(this.loadBranch.enteredID);
  }

  successfullyAppliedChanges() {
    this.sendBriefMessage('branch.confirm.feedback.success');
  }

  failedAppliedChanges() {
    this.sendBriefMessage('branch.confirm.feedback.failed');
  }

  onLoadBranchSubmitClicked(id: string) {
    this.reasoningBranchManagementService.getReasoningBranch(id).subscribe(response => {
      this.branchDetails = response;
      this.showDetails = true;
      this.showNoResults = false;
    }, () => {
      this.showNoResults = true;
      this.showDetails = false;
    });
  }

  updateReasoningBranch(id: string) {
    this.reasoningBranchManagementService
      .updateReasoningBranch(id, this.branchDetailsForm.branchForm.value)
      .subscribe(() => {
        this.successfullyAppliedChanges();
        this.hideConfirm();
      }, () => {
        this.failedAppliedChanges();
      });
  }

  private sendBriefMessage(messageContent) {
    this.eventService.sendEvent({
      key: EventKey.NOTIFICATION,
      data: {
        message: messageContent
      }
    });
  }

}
