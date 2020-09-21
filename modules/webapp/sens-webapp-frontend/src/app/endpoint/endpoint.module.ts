import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConfigurationEndpointService } from '@endpoint/configuration/services/configuration-endpoint.service';
import { DecisionTreesEndpointService } from '@endpoint/decision-trees/services/decision-trees-endpoint.service';
import { ExternalAppsEndpointService } from '@endpoint/external-apps/services/external-apps-endpoint.service';
import { ReasoningBranchesEndpointService } from '@endpoint/reasoning-branches/services/reasoning-branches-endpoint.service';

@NgModule({
  declarations: [],
  providers: [
    DecisionTreesEndpointService,
    ReasoningBranchesEndpointService,
    ConfigurationEndpointService,
    ExternalAppsEndpointService
  ],
  imports: [
    CommonModule
  ]
})
export class EndpointModule {}
