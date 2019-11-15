import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { SharedModule } from '@app/shared/shared.module';
import { RestrictionPanelModule } from '@app/templates/alert-restrictions-management/restriction-panel/restriction-panel.module';
import { AlertRestrictionTitleViewComponent } from './alert-restriction-title-view/alert-restriction-title-view.component';
import { AlertsRestrictionsActionsViewComponent } from './alerts-restrictions-actions-view/alerts-restrictions-actions-view.component';
import { AlertsRestrictionsDetailsViewComponent } from './alerts-restrictions-details-view/alerts-restrictions-details-view.component';
import { AlertsRestrictionsNameViewComponent } from './alerts-restrictions-name-view/alerts-restrictions-name-view.component';

@NgModule({
  declarations: [
    AlertsRestrictionsNameViewComponent,
    AlertsRestrictionsDetailsViewComponent,
    AlertsRestrictionsActionsViewComponent,
    AlertRestrictionTitleViewComponent
  ],
  entryComponents: [
    AlertsRestrictionsNameViewComponent,
    AlertsRestrictionsDetailsViewComponent,
    AlertsRestrictionsActionsViewComponent
  ],
  imports: [
    CommonModule,
    RestrictionPanelModule,
    SharedModule
  ]
})
export class AlertsRestrictionsViewsModule {}
