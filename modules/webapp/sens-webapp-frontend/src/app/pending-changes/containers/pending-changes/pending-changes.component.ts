import { Component, OnInit } from '@angular/core';
import { Header } from '@app/ui-components/header/header';
import { StateContent } from '@app/ui-components/state/state';

@Component({
  selector: 'app-pending-changes',
  templateUrl: './pending-changes.component.html'
})
export class PendingChangesComponent implements OnInit {

  translatePrefix = 'pendingChanges.';

  listEmptyState: StateContent = {
    centered: true,
    image: null,
    inProgress: false,
    title: this.translatePrefix + 'emptyState.title',
    description: null,
  };

  listLoading: StateContent = {
    centered: true,
    image: null,
    inProgress: true,
    title: this.translatePrefix + 'updatingList.title',
    description: null,
  };

  header: Header = {
    title: this.translatePrefix + 'title',
    parameter: '14'
  };

  constructor() { }

  ngOnInit() {
  }

}
