import { Component, OnInit } from '@angular/core';
import { AlertRestrictionsManagementService } from './alert-restrictions-management.service';

@Component({
  selector: 'app-alert-restrictions-management',
  templateUrl: './alert-restrictions-management.component.html',
  styleUrls: ['./alert-restrictions-management.component.scss']
})
export class AlertRestrictionsManagementComponent implements OnInit {
  constructor(
      private alertRestrictionsService: AlertRestrictionsManagementService
  ) { }

  restrictionPanelData = {
    'status': true,
  };

  ngOnInit() {
  }

  openNewRestrictionPanel() {
    this.alertRestrictionsService.restrictionPanelData(this.restrictionPanelData);
  }
}
