import { ObserversModule } from '@angular/cdk/observers';
import { OverlayModule } from '@angular/cdk/overlay';
import { NgModule, Type } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule, MatChipsModule } from '@angular/material';
import { BrowserModule } from '@core/browser/browser.module';
import { FootnoteModule } from '@ui/footnote/footnote.module';
import { LayoutsModule } from '@ui/layouts/layouts.module';
import { InlineChipsListComponent } from './components/inline-chips-list/inline-chips-list.component';

const publicDeclarations: Type<any>[] = [InlineChipsListComponent];

@NgModule({
  declarations: [...publicDeclarations],
  exports: [...publicDeclarations],
  imports: [
    CommonModule,
    MatChipsModule,
    ObserversModule,
    MatCardModule,
    OverlayModule,
    LayoutsModule,
    BrowserModule,
    FootnoteModule
  ]
})
export class ChipsModule { }
