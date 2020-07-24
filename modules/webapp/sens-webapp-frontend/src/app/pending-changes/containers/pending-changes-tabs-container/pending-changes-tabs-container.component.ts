import { Component, OnInit } from '@angular/core';
import { PendingChangesService } from '@app/pending-changes/services/pending-changes.service';
import { Header } from '@app/ui-components/header/header';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-pending-changes-tabs-container',
  templateUrl: './pending-changes-tabs-container.component.html',
  styleUrls: ['./pending-changes-tabs-container.component.scss']
})
export class PendingChangesTabsContainerComponent implements OnInit {

  tabNavigationLinks = [
    {
      link: 'queue',
      name: 'queue',
    },
    {
      link: 'archived',
      name: 'archived'
    }
  ];

  header: Header = {
    title: 'pendingChanges.title',
  };

  tabTranslatePrefix = 'pendingChanges.tabs.';
  queuedPendingChangesCount$: Observable<any>;

  constructor(private pendingChangesService: PendingChangesService) {
    this.queuedPendingChangesCount$ = this.pendingChangesService.getQueuedPendingChangesCount();
  }

  ngOnInit() {
  }

}
