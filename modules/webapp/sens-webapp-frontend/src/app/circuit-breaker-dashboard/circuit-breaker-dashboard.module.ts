import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { MatTabsModule } from '@angular/material/tabs';
import { UiComponentsModule } from '@app/ui-components/ui-components.module';
import { TranslateModule } from '@ngx-translate/core';
import { DialogModule } from '@ui/dialog/dialog.module';
import { CircuitBreakerDashboardComponent } from './containers/circuit-breaker-dashboard/circuit-breaker-dashboard.component';
import { CircuitBreakerListComponent } from './containers/circuit-breaker-list/circuit-breaker-list.component';
import { CircuitBreakerAlertsTableComponent } from './components/circuit-breaker-alerts-table/circuit-breaker-alerts-table.component';

@NgModule({
  declarations: [
    CircuitBreakerDashboardComponent,
    CircuitBreakerListComponent,
    CircuitBreakerAlertsTableComponent
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
    DialogModule
  ]
})
export class CircuitBreakerDashboardModule {}
