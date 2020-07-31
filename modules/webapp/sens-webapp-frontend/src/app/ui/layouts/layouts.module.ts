import { NgModule, Type } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CardLayoutComponent } from './components/card-layout/card-layout.component';
import { FullScreenLayoutComponent } from './components/full-screen-layout/full-screen-layout.component';
import { PerfectScrollbarModule } from 'ngx-perfect-scrollbar';

const publicDeclarations: Type<any>[] = [CardLayoutComponent, FullScreenLayoutComponent];

@NgModule({
  declarations: [...publicDeclarations],
  imports: [
    CommonModule,
    PerfectScrollbarModule,
  ],
  exports: [...publicDeclarations]
})
export class LayoutsModule { }
