import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatDialogModule } from '@angular/material/dialog';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { TranslateModule } from '@ngx-translate/core';
import { StateComponent } from './state/state.component';
import { ErrorCardComponent } from './error-card/error-card.component';
import { HeaderComponent } from './header/header.component';
import { DialogComponent } from './dialog/dialog.component';
import { AiSolutionComponent } from './ai-solution/ai-solution.component';

@NgModule({
  declarations: [
    StateComponent,
    ErrorCardComponent,
    HeaderComponent,
    DialogComponent,
    AiSolutionComponent
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
    MatDialogModule,
  ],
  exports: [
    StateComponent,
    ErrorCardComponent,
    HeaderComponent,
    DialogComponent,
    AiSolutionComponent
  ],
  entryComponents: [
    DialogComponent
  ]
})
export class UiComponentsModule {}
