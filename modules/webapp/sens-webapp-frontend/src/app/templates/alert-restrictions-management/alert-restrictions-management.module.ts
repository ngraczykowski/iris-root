import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { SharedModule } from '@app/shared/shared.module';
import { AlertsRestrictionsTableModule } from '@app/templates/alert-restrictions-management/alerts-restrictions-table/alerts-restrictions-table.module';
import { RestrictionPanelModule } from '@app/templates/alert-restrictions-management/restriction-panel/restriction-panel.module';
import { AlertRestrictionsManagementComponent } from './alert-restrictions-management.component';
import { AlertRestrictionsManagementService } from './alert-restrictions-management.service';

@NgModule({
  declarations: [
    AlertRestrictionsManagementComponent,
  ],
  imports: [
    CommonModule,
    SharedModule,
    AlertsRestrictionsTableModule,
    RestrictionPanelModule,
    HttpClientModule
  ],
  providers: [
    AlertRestrictionsManagementService
  ]
})
export class AlertRestrictionsManagementModule {}
