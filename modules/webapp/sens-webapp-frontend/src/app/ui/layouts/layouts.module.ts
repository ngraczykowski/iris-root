import { NgModule, Type } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule, MatProgressSpinnerModule } from '@angular/material';
import { WindowService } from '@ui/layouts/services/window.service';
import { CardLayoutComponent } from './components/card-layout/card-layout.component';
import { FullScreenLayoutComponent } from './components/full-screen-layout/full-screen-layout.component';
import { PerfectScrollbarModule } from 'ngx-perfect-scrollbar';
import { PageLayoutComponent } from './components/page-layout/page-layout.component';
import { PanelLayoutComponent } from './components/panel-layout/panel-layout.component';
import { LoadingButtonLayoutComponent } from './components/loading-button-layout/loading-button-layout.component';
const publicDeclarations: Type<any>[] = [CardLayoutComponent, FullScreenLayoutComponent,
  PageLayoutComponent, PanelLayoutComponent, LoadingButtonLayoutComponent];

@NgModule({
  declarations: [...publicDeclarations],
  imports: [
    CommonModule,
    PerfectScrollbarModule,
    MatProgressSpinnerModule,
    MatIconModule
  ],
  exports: [...publicDeclarations],
  providers: [WindowService]
})
export class LayoutsModule { }
