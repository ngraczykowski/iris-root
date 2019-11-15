import { NgModule } from '@angular/core';
import { HintFeedbackComponent } from '@app/components/hint-feedback/hint-feedback.component';
import { SharedModule } from '@app/shared/shared.module';

@NgModule({
  imports: [
    SharedModule
  ],
  declarations: [
    HintFeedbackComponent
  ],
  exports: [
    HintFeedbackComponent
  ]
})
export class HintFeedbackModule { }
