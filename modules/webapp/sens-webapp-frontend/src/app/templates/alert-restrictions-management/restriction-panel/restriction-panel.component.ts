import { Component } from '@angular/core';
import { AlertRestrictionsManagementService } from '@app/templates/alert-restrictions-management/alert-restrictions-management.service';

@Component({
  selector: 'app-restriction-panel',
  templateUrl: './restriction-panel.component.html',
  styleUrls: ['./restriction-panel.component.scss']
})
export class RestrictionPanelComponent {

  alertRestrictionStatus = false;
  alertRestrictionId: number;

  constructor(private alertRestrictionsService: AlertRestrictionsManagementService) {
    this.alertRestrictionsService.change.subscribe(value => {
      this.alertRestrictionStatus = value.status;
      this.alertRestrictionId = value.id;
    });
  }
}
