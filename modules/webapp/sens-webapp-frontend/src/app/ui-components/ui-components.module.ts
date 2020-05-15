import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { TranslateModule } from '@ngx-translate/core';
import { StateComponent } from './state/state.component';
import { TitleComponent } from './title/title.component';

@NgModule({
  declarations: [
    StateComponent,
    TitleComponent
  ],
  imports: [
    CommonModule,
    MatProgressSpinnerModule,
    TranslateModule
  ],
  exports: [
    StateComponent,
    TitleComponent
  ]
})
export class UiComponentsModule {}
