import { NgModule } from '@angular/core';
import { SharedModule } from '@app/shared/shared.module';
import { BranchReviewButtonComponent } from './branch-review-button.component';
import { BranchReviewButtonService } from './branch-review-button.service';

@NgModule({
  imports: [
    SharedModule
  ],
  declarations: [
    BranchReviewButtonComponent
  ],
  providers: [
    BranchReviewButtonService
  ],
  exports: [
    BranchReviewButtonComponent
  ]
})
export class BranchReviewButtonModule {}
