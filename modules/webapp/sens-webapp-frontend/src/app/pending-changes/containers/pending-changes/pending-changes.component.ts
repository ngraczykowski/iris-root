import { Component, OnInit } from '@angular/core';
import { PendingChange } from '@app/pending-changes/models/pending-changes';
import { PendingChangesService } from '@app/pending-changes/services/pending-changes.service';
import { Header } from '@app/ui-components/header/header';
import { StateContent } from '@app/ui-components/state/state';
import { TranslateService } from '@ngx-translate/core';

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
    title: this.translatePrefix + 'emptyState.title',
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

  header: Header;

  constructor(
      private pendingChangesService: PendingChangesService
  ) { }

  ngOnInit() {
    this.loadPendingChanges();
  }

  loadPendingChanges() {
    this.resetChangeRequestDetails();
    this.resetView();
    this.pendingChangesLoading = true;
    this.pendingChangesService.getPendingChangesDetails().subscribe(data => {
      this.pendingChangesData = data;
      this.resetView();
      if (data.length > 0) {
        this.pendingChangesTable = true;
      } else {
        this.pendingChangesEmptyState = true;
      }
      this.generateHeader();
    }, error => {
      this.resetView();
      this.pendingChangesError = true;
    });
  }

  generateHeader() {
    if (this.pendingChangesData.length > 0) {
      this.header = {
        title: this.translatePrefix + 'title',
        count: this.pendingChangesData.length.toString(),
      };
    } else {
      this.header = {
        title: this.translatePrefix + 'title',
      };
    }
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
}
