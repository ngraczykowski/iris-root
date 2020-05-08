import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatMenuModule } from '@angular/material/menu';
import { MatSidenavModule } from '@angular/material/sidenav';
import { RouterModule } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { SidenavComponent } from './containers/sidenav/sidenav.component';
import { NavigationElementComponent } from './components/navigation-element/navigation-element.component';
import { NavigationSectionComponent } from './components/navigation-section/navigation-section.component';

@NgModule({
  declarations: [SidenavComponent, NavigationElementComponent, NavigationSectionComponent],
  exports: [
    SidenavComponent
  ],
  imports: [
    CommonModule,
    MatSidenavModule,
    MatListModule,
    MatIconModule,
    MatMenuModule,
    MatButtonModule,
    RouterModule,
    TranslateModule
  ]
})
export class SidenavModule { }
