import { NgModule } from '@angular/core';
import { SharedModule } from '../../../shared/shared.module';
import { DecisionTreeOperationsModule } from '../decision-tree-operations/decision-tree-operations.module';
import { DecisionTreeListClient } from './decision-tree-list-client';
import { DecisionTreeListComponent } from './decision-tree-list.component';
import { DecisionTreeListService } from './decision-tree-list.service';
import { DecisionTreeTableModule } from './decision-tree-table/decision-tree-table.module';

@NgModule({
  imports: [
    SharedModule,
    DecisionTreeOperationsModule,
    DecisionTreeTableModule
  ],
  providers: [
    DecisionTreeListClient,
    DecisionTreeListService
  ],
  declarations: [
    DecisionTreeListComponent
  ],
  exports: [
    DecisionTreeListComponent
  ]
})
export class DecisionTreeListModule {}
