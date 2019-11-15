import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { SharedModule } from '@app/shared/shared.module';
import { AlertsRestrictionsTableComponent } from '@app/templates/alert-restrictions-management/alerts-restrictions-table/alerts-restrictions-table.component';
import { AlertsRestrictionsViewsModule } from '@app/templates/alert-restrictions-management/alerts-restrictions-table/alerts-restrictions-views/alerts-restrictions-views.module';
import { AlertRestrictionTitleViewComponent } from './alerts-restrictions-views/alert-restriction-title-view/alert-restriction-title-view.component';

@NgModule({
  declarations: [
    AlertsRestrictionsTableComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    AlertsRestrictionsViewsModule
  ],
  exports: [
    AlertsRestrictionsTableComponent
  ],
  entryComponents: [
    AlertRestrictionTitleViewComponent
  ]
})
export class AlertsRestrictionsTableModule {}
