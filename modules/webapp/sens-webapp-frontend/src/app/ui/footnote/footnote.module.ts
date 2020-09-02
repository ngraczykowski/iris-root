import { NgModule, Type } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material';
import { LayoutsModule } from '../layouts/layouts.module';
import { FootnoteComponent } from './components/footnote/footnote.component';

const publicDeclarations: Type<any>[] = [FootnoteComponent];

@NgModule({
  declarations: [...publicDeclarations],
  exports: [...publicDeclarations],
  imports: [
    CommonModule,
    MatCardModule,
    LayoutsModule
  ]
})
export class FootnoteModule { }
