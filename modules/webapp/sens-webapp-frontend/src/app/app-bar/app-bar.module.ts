import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatMenuModule } from '@angular/material/menu';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterModule } from '@angular/router';
import { AnimationModule } from '@ui/animation/animation.module';
import { UserMenuComponent } from './components/user-menu/user-menu.component';
import { AppBarComponent } from './containers/app-bar/app-bar.component';
import { BrandHeroComponent } from './components/brand-hero/brand-hero.component';
import { SharedModule } from '@app/shared/shared.module';

@NgModule({
  declarations: [UserMenuComponent, AppBarComponent, BrandHeroComponent],
  exports: [
    AppBarComponent
  ],
  imports: [
    CommonModule,
    MatToolbarModule,
    MatMenuModule,
    MatButtonModule,
    MatListModule,
    MatIconModule,
    RouterModule,
    SharedModule,
    AnimationModule
  ]
})
export class AppBarModule { }
