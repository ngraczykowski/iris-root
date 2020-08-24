import { NgModule, Type } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material';
import { RouterModule } from '@angular/router';
import { warningsRoutes } from 'app/warnings/warnings.routes';
import { TranslateModule } from '@ngx-translate/core';
import { LayoutsModule } from '../ui/layouts/layouts.module';
import { WarningsPageComponent } from './containers/warnings-page/warnings-page.component';


const publicDeclarations: Type<any>[] = [WarningsPageComponent];

@NgModule({
  declarations: [...publicDeclarations],
  imports: [
    CommonModule,
    RouterModule.forChild(warningsRoutes),
    TranslateModule,
    LayoutsModule,
    MatButtonModule,
  ],
  exports: [...publicDeclarations]
})
export class WarningsModule { }
