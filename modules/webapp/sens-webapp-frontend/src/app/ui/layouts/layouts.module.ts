import { NgModule, Type } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule, MatProgressSpinnerModule } from '@angular/material';
import { BottomSheetLayoutComponent } from '@ui/layouts/components/bottom-sheet-layout/bottom-sheet-layout.component';
import { WindowService } from '@core/browser/services/window.service';
import { CardLayoutComponent } from './components/card-layout/card-layout.component';
import { FullScreenLayoutComponent } from './components/full-screen-layout/full-screen-layout.component';
import { PerfectScrollbarModule } from 'ngx-perfect-scrollbar';
import { PageLayoutComponent } from './components/page-layout/page-layout.component';
import { PanelLayoutComponent } from './components/panel-layout/panel-layout.component';
import { LoadingButtonLayoutComponent } from './components/loading-button-layout/loading-button-layout.component';
import { SectionLayoutComponent } from './components/section-layout/section-layout.component';
import { SectionsGroupLayoutComponent } from './components/sections-group-layout/sections-group-layout.component';
import { ListLayoutComponent } from './components/list-layout/list-layout.component';

const publicDeclarations: Type<any>[] = [CardLayoutComponent, FullScreenLayoutComponent,
  PageLayoutComponent, PanelLayoutComponent, LoadingButtonLayoutComponent,
  BottomSheetLayoutComponent, SectionLayoutComponent, SectionsGroupLayoutComponent,
  ListLayoutComponent];

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
