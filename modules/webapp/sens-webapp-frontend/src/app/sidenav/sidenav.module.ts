import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatMenuModule } from '@angular/material/menu';
import { MatSidenavModule } from '@angular/material/sidenav';
import { RouterModule } from '@angular/router';
import { AuthoritiesModule } from '@core/authorities/authorities.module';
import { TranslateModule } from '@ngx-translate/core';
import { AnimationModule } from '@ui/animation/animation.module';
import { SidenavComponent } from './containers/sidenav/sidenav.component';
import { NavigationElementComponent } from './components/navigation-element/navigation-element.component';
import { NavigationSectionComponent } from './components/navigation-section/navigation-section.component';

@NgModule({
  declarations: [SidenavComponent, NavigationElementComponent, NavigationSectionComponent],
  exports: [
    SidenavComponent
  ],
  imports: [
    AuthoritiesModule,
    CommonModule,
    MatSidenavModule,
    MatListModule,
    MatIconModule,
    MatMenuModule,
    MatButtonModule,
    RouterModule,
    TranslateModule,
    AnimationModule
  ]
})
export class SidenavModule { }
