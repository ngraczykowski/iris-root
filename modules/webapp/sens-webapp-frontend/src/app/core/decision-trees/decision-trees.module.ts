import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DecisionTreesService } from '@app/core/decision-trees/services/decision-trees.service';
import { DecisionTreeExistsValidator } from '@core/decision-trees/validators/decision-tree-exists.validator';
import { EndpointModule } from '@endpoint/endpoint.module';

@NgModule({
  declarations: [],
  providers: [
    DecisionTreesService,
    DecisionTreeExistsValidator,
  ],
  imports: [
    CommonModule,
    EndpointModule,
  ]
})
export class DecisionTreesModule {}
