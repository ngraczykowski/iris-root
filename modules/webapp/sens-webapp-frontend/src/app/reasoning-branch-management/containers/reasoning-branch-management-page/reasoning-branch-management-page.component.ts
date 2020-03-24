import { Location } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BranchDetailsComponent } from '@app/reasoning-branch-management/components/branch-details/branch-details.component';
import { LoadBranchComponent } from '@app/reasoning-branch-management/components/load-branch/load-branch.component';
import {
  ReasoningBranchDetails,
  ReasoningBranchEmptyStates
} from '@app/reasoning-branch-management/models/reasoning-branch-management';
import { ReasoningBranchManagementService } from '@app/reasoning-branch-management/services/reasoning-branch-management.service';
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
  failedAppliedChanges = false;
  emptyState;
  branchDetails: ReasoningBranchDetails;
  fullId: string;
  inProgress = false;

  @ViewChild('branchDetailsForm', { static: false }) branchDetailsForm: BranchDetailsComponent;
  @ViewChild('loadBranch', { static: true }) loadBranch: LoadBranchComponent;

  constructor(
    private readonly eventService: LocalEventService,
    private readonly reasoningBranchManagementService: ReasoningBranchManagementService,
    private activatedRoute: ActivatedRoute,
    private location: Location
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
    this.hideConfirm();
    this.failedAppliedChanges = false;
    this.inProgress = false;
  }

  reasoningBranchUpdateError() {
    this.hideConfirm();
    this.failedAppliedChanges = true;
    this.inProgress = false;
  }

  onLoadBranchSubmitClicked(id: string) {
    this.inProgress = true;
    this.failedAppliedChanges = false;
    this.reasoningBranchManagementService.getReasoningBranch(id).subscribe(response => {
      this.branchDetails = response;
      this.showDetails = true;
      this.fullId = id;
      this.inProgress = false;
      this.updateUrl(id);
    }, (error) => {
      this.showDetails = false;
      this.inProgress = false;
      if (error.status === 404) {
        this.emptyState = ReasoningBranchEmptyStates.NORESULTS;
      } else {
        this.emptyState = ReasoningBranchEmptyStates.TIMEOUT;
      }
    });
  }

  updateReasoningBranch(id: string) {
    this.inProgress = true;
    this.reasoningBranchManagementService
      .updateReasoningBranch(id, this.branchDetailsForm.branchForm.value)
      .subscribe(() => {
        this.successfullyAppliedChanges();
      }, () => {
        this.reasoningBranchUpdateError();
      });
  }

  private updateUrl(id: string): void {
    this.location.go(`/reasoning-branch/${id}`);
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
