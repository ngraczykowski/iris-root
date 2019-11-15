import { NgModule } from '@angular/core';
import { FilterPipe } from '@app/shared/pipes/filter.pipe';
import { FriendlyPipe } from '@app/shared/pipes/friendly/friendly.pipe';
import { TranslateOrFriendlyValuePipe } from '@app/shared/pipes/friendly/translate-or-friendly-value.pipe';
import { ReplacePipe } from '@app/shared/pipes/replace/replace.pipe';
import { SortPipe } from '@app/shared/pipes/sort/sort.pipe';
import { TranslateValuePipe } from '@app/shared/pipes/translateValue/translate-value.pipe';
import { FilterMultipleValuesPipe } from './filterMultipleValues/filter-multiple-values.pipe';

@NgModule({
  declarations: [
    FilterPipe,
    ReplacePipe,
    FriendlyPipe,
    TranslateOrFriendlyValuePipe,
    TranslateValuePipe,
    SortPipe,
    FilterMultipleValuesPipe
  ],
  exports: [
    FilterPipe,
    ReplacePipe,
    FriendlyPipe,
    TranslateOrFriendlyValuePipe,
    TranslateValuePipe,
    SortPipe,
    FilterMultipleValuesPipe
  ]
})
export class PipeModule { }
