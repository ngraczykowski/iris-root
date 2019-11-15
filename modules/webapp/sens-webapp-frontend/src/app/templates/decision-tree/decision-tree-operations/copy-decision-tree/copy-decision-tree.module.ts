import { NgModule } from '@angular/core';
import { SharedModule } from '../../../../shared/shared.module';
import { CopyDecisionTreeComponent } from './copy-decision-tree.component';
import { CopyDecisionTreeService } from './copy-decision-tree.service';

@NgModule({
  imports: [
    SharedModule
  ],
  providers: [
    CopyDecisionTreeService
  ],
  declarations: [
    CopyDecisionTreeComponent
  ],
  exports: [
    CopyDecisionTreeComponent
  ]
})
export class CopyDecisionTreeModule {}
