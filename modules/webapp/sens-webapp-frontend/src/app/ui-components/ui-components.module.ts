import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { TranslateModule } from '@ngx-translate/core';
import { StateComponent } from './state/state.component';
import { ErrorCardComponent } from './error-card/error-card.component';
import { HeaderComponent } from './header/header.component';

@NgModule({
  declarations: [
    StateComponent,
    ErrorCardComponent,
    HeaderComponent
  ],
  imports: [
    CommonModule,
    MatProgressSpinnerModule,
    TranslateModule,
    MatCardModule,
    MatIconModule,
    MatListModule,
    MatExpansionModule,
    MatButtonModule,
  ],
  exports: [
    StateComponent,
    ErrorCardComponent,
    HeaderComponent
  ]
})
export class UiComponentsModule {}
