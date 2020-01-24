import { NgModule } from '@angular/core';
import { SharedModule } from '@app/shared/shared.module';
import { DecisionTreeDecisionGroupsListComponent } from './decision-tree-info-activations/decision-tree-decision-groups-list/decision-tree-decision-groups-list.component';
import { DecisionTreeInfoActivationsComponent } from './decision-tree-info-activations/decision-tree-info-activations.component';
import { DecisionTreeInfoDetailsComponent } from './decision-tree-info-details/decision-tree-info-details.component';
import { DecisionTreeInfoFeaturesListComponent } from './decision-tree-info-features/decision-tree-info-features-list/decision-tree-info-features-list.component';
import { DecisionTreeInfoFeaturesComponent } from './decision-tree-info-features/decision-tree-info-features.component';
import { DecisionTreeInfoComponent } from './decision-tree-info.component';

@NgModule({
  imports: [
    SharedModule
  ],
  declarations: [
    DecisionTreeInfoComponent,
    DecisionTreeInfoDetailsComponent,
    DecisionTreeInfoFeaturesComponent,
    DecisionTreeInfoFeaturesListComponent,
    DecisionTreeInfoActivationsComponent,
    DecisionTreeDecisionGroupsListComponent
  ],
  exports: [
    DecisionTreeInfoComponent
  ]
})
export class DecisionTreeInfoModule {}
