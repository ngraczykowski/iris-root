import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WindowService } from '@core/browser/services/window.service';

@NgModule({
  declarations: [],
  imports: [
    CommonModule
  ],
  providers: [WindowService]
})
export class BrowserModule { }
