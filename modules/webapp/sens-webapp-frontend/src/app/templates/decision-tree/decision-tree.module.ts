import { NgModule } from '@angular/core';
import { DecisionTreeDetailsModule } from './decision-tree-details/decision-tree-details.module';
import { DecisionTreeListModule } from './decision-tree-list/decision-tree-list.module';

@NgModule({
  imports: [
    DecisionTreeDetailsModule,
    DecisionTreeListModule
  ],
  exports: [
    DecisionTreeDetailsModule,
    DecisionTreeListModule
  ]
})
export class DecisionTreeModule {}
