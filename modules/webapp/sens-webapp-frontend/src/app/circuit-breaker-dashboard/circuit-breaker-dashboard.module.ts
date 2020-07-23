import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { MatTabsModule } from '@angular/material/tabs';
import { RouterModule } from '@angular/router';
import { UiComponentsModule } from '@app/ui-components/ui-components.module';
import { TranslateModule } from '@ngx-translate/core';
import { DialogModule } from '@ui/dialog/dialog.module';
import { CircuitBreakerAlertsTableComponent } from './components/circuit-breaker-alerts-table/circuit-breaker-alerts-table.component';
import { DiscrepantBranchActionComponent } from './components/discrepant-branch-action/discrepant-branch-action.component';
import { DiscrepantBranchComponent } from './components/discrepant-branch/discrepant-branch.component';
import { CircuitBreakerBranchListComponent } from './containers/circuit-breaker-branch-list/circuit-breaker-branch-list.component';
import { CircuitBreakerDashboardComponent } from './containers/circuit-breaker-dashboard/circuit-breaker-dashboard.component';

@NgModule({
  declarations: [
    CircuitBreakerDashboardComponent,
    CircuitBreakerBranchListComponent,
    CircuitBreakerAlertsTableComponent,
    DiscrepantBranchComponent,
    DiscrepantBranchActionComponent
  ],
  imports: [
    CommonModule,
    UiComponentsModule,
    MatExpansionModule,
    MatTabsModule,
    MatButtonModule,
    MatTableModule,
    MatPaginatorModule,
    TranslateModule,
    MatSortModule,
    MatProgressSpinnerModule,
    DialogModule,
    RouterModule
  ]
})
export class CircuitBreakerDashboardModule {}
