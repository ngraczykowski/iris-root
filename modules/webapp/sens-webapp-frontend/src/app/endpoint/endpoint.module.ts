import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DecisionTreesEndpointService } from '@endpoint/decision-trees/services/decision-trees-endpoint.service';

@NgModule({
  declarations: [],
  providers: [
    DecisionTreesEndpointService
  ],
  imports: [
    CommonModule
  ]
})
export class EndpointModule {}
