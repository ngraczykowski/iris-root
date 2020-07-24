import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PendingChange, PendingChangesStatus } from '@app/pending-changes/models/pending-changes';
import { PendingChangesService } from '@app/pending-changes/services/pending-changes.service';
import { StateContent } from '@app/ui-components/state/state';

@Component({
  selector: 'app-pending-changes',
  templateUrl: './pending-changes.component.html'
})
export class PendingChangesComponent implements OnInit {
  pendingChangesLoading: boolean;
  pendingChangesTable: boolean;
  pendingChangesEmptyState: boolean;
  pendingChangesError: boolean;

  pendingChangesData: PendingChange[];

  changeRequestDetails: PendingChange;

  translatePrefix = 'pendingChanges.';

  listEmptyState: StateContent = {
    centered: true,
    title: this.translatePrefix + 'emptyState.title.queue',
  };

  listLoading: StateContent = {
    centered: true,
    inProgress: true,
    title: this.translatePrefix + 'updatingList.title',
  };

  listError: StateContent = {
    centered: true,
    title: this.translatePrefix + 'errorState.title',
    description: this.translatePrefix + 'errorState.description',
    button: this.translatePrefix + 'errorState.button'
  };

  private changeRequestStatuses: PendingChangesStatus[];
  isArchiveTab: boolean;

  constructor(
      private pendingChangesService: PendingChangesService,
      private route: ActivatedRoute
  ) {
    this.changeRequestStatuses = route.snapshot.data.changeRequestStatuses;
    if (this.changeRequestStatuses.includes(PendingChangesStatus.CLOSED)) {
      this.configureArchiveTab();
    }
  }

  ngOnInit() {
    this.loadPendingChanges();
  }

  loadPendingChanges() {
    this.resetChangeRequestDetails();
    this.resetView();
    this.pendingChangesLoading = true;
    this.pendingChangesService.getPendingChangesDetails(this.changeRequestStatuses)
        .subscribe(data => {
          this.pendingChangesData = data;
          this.resetView();
          if (data.length > 0) {
            this.pendingChangesTable = true;
          } else {
            this.pendingChangesEmptyState = true;
          }
        }, error => {
          this.resetView();
          this.pendingChangesError = true;
        });
  }

  resetView() {
    this.pendingChangesLoading = false;
    this.pendingChangesTable = false;
    this.pendingChangesEmptyState = false;
    this.pendingChangesError = false;
  }

  loadChangeRequestDetails(details) {
    this.changeRequestDetails = details;
  }

  resetChangeRequestDetails() {
    this.changeRequestDetails = null;
  }

  private configureArchiveTab() {
    this.isArchiveTab = true;
    this.listEmptyState.title = this.translatePrefix + 'emptyState.title.closed';
  }
}
