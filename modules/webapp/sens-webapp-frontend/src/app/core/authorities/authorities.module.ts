import { NgModule, Type } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthoritiesService } from '@core/authorities/services/authorities.service';
import { IfAuthorityDirective } from './directives/if-authority.directive';

const publicDirectives: Type<any>[] = [IfAuthorityDirective];

@NgModule({
  declarations: [...publicDirectives],
  exports: [...publicDirectives],
  providers: [AuthoritiesService],
  imports: [
    CommonModule
  ]
})
export class AuthoritiesModule { }
