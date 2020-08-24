import { ObserversModule } from '@angular/cdk/observers';
import { NgModule, Type } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ExpanseAnimationDirective } from './directives/expanse-animation.directive';
import { DynamicHeightComponent } from './components/dynamic-height/dynamic-height.component';
import { FxDirective } from './directives/fx.directive';

const publicDeclarations: Type<any>[] = [ExpanseAnimationDirective, DynamicHeightComponent,
  FxDirective];

@NgModule({
  declarations: [...publicDeclarations],
  exports: [...publicDeclarations],
  imports: [
    CommonModule,
    ObserversModule
  ]
})
export class AnimationModule { }
