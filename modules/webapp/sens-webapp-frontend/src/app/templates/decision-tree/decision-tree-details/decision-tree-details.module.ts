import { NgModule } from '@angular/core';
import { SharedModule } from '@app/shared/shared.module';
import { BranchBrowserModule } from '../branch-browser/branch-browser.module';
import { BranchChangeModule } from '../branch-change/branch-change.module';
import { DecisionTreeOperationsModule } from '../decision-tree-operations/decision-tree-operations.module';
import { DecisionTreeDetailsClient } from './decision-tree-details-client';
import { DecisionTreeDetailsComponent } from './decision-tree-details.component';
import { DecisionTreeDetailsService } from './decision-tree-details.service';
import { DecisionTreeInfoModule } from './decision-tree-info/decision-tree-info.module';
import { DecisionTreeManagementComponent } from './decision-tree-management/decision-tree-management.component';
import { DecisionTreeReportsMenuComponent } from './decision-tree-reports-menu/decision-tree-reports-menu.component';

@NgModule({
  imports: [
    SharedModule,
    BranchBrowserModule,
    BranchChangeModule,
    DecisionTreeOperationsModule,
    DecisionTreeInfoModule
  ],
  providers: [
    DecisionTreeDetailsClient,
    DecisionTreeDetailsService
  ],
  declarations: [
    DecisionTreeDetailsComponent,
    DecisionTreeManagementComponent,
    DecisionTreeReportsMenuComponent
  ],
  exports: [
    DecisionTreeDetailsComponent
  ]
})
export class DecisionTreeDetailsModule {}
