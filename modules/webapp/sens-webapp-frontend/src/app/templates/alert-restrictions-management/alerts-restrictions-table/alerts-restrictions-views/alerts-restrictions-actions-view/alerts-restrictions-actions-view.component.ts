import { Component, Input, OnInit } from '@angular/core';
import { EventKey } from '@app/shared/event/event.service.model';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { AlertRestrictionsManagementService } from '@app/templates/alert-restrictions-management/alert-restrictions-management.service';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-alerts-restrictions-actions-view',
  templateUrl: './alerts-restrictions-actions-view.component.html',
  styleUrls: ['./alerts-restrictions-actions-view.component.scss']
})
export class AlertsRestrictionsActionsViewComponent implements OnInit {
  @Input() data;

  constructor(
      private alertRestrictionsService: AlertRestrictionsManagementService,
      private eventService: LocalEventService,
      private translate: TranslateService
  ) { }

  ngOnInit() {
  }

  delete() {
    if (confirm(this.translate.instant('alertRestrictionsManagement.table.data.actions.delete.confirm'))) {
      this.alertRestrictionsService.delete(this.data.data).subscribe(() => {
        this.refreshData();
        this.confirmOperation('alertRestrictionsManagement.table.data.actions.delete.confirmation');
      });
    }
  }

  private confirmOperation(message) {
    this.eventService.sendEvent({
      key: EventKey.NOTIFICATION,
      data: {
        type: 'success',
        message: message
      }
    });
  }

  refreshData() {
    this.alertRestrictionsService.get();
  }

  editRestriction() {
    this.alertRestrictionsService.restrictionPanelData(
        {'status': true, 'id': this.data.data});
  }
}
