import { NgModule, Type } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HighlightPipe } from './pipes/highlight.pipe';

const publicDeclarations: Type<any>[] = [HighlightPipe];

@NgModule({
  declarations: [...publicDeclarations],
  exports: [...publicDeclarations],
  imports: [
    CommonModule
  ]
})
export class HighlightModule { }
