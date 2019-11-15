import { NgModule } from '@angular/core';
import { SharedModule } from '@app/shared/shared.module';
import { DecisionTreesListTableComponent } from './decision-trees-list-table';
import { DecisionTreeTableViewsModule } from './views/decision-tree-table-views.module';

@NgModule({
  imports: [
    SharedModule,
    DecisionTreeTableViewsModule
  ],
  declarations: [
    DecisionTreesListTableComponent
  ],
  exports: [
    DecisionTreesListTableComponent
  ]
})
export class DecisionTreeTableModule {}
