import { NgModule } from '@angular/core';
import { InputModule } from '@app/components/input/input.module';
import { SharedModule } from '@app/shared/shared.module';
import { BranchFilterClient } from './branch-filter-client';
import { BranchFilterComponent } from './branch-filter.component';
import { SavedFiltersComponent } from './saved-filters/saved-filters.component';

@NgModule({
  imports: [
    SharedModule,
    InputModule,
  ],
  providers: [
    BranchFilterClient
  ],
  declarations: [
    SavedFiltersComponent,
    BranchFilterComponent
  ],
  exports: [
    BranchFilterComponent
  ]
})
export class BranchFilterModule {}
