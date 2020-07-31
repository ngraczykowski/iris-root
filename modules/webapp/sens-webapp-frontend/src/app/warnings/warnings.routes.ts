import { Routes } from '@angular/router';
import { WarningsPageComponent } from '@app/warnings/containers/warnings-page/warnings-page.component';

export const warningsRoutes: Routes = [{
  path: ':type',
  component: WarningsPageComponent
}];
