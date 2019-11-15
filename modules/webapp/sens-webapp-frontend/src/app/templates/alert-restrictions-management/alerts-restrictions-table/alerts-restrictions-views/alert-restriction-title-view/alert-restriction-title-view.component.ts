import { Component } from '@angular/core';
import { DynamicComponent, View } from '@app/components/dynamic-view/dynamic-view.component';

@Component({
  selector: 'app-alert-restriction-title-view',
  templateUrl: './alert-restriction-title-view.component.html',
  styleUrls: ['./alert-restriction-title-view.component.scss']
})
export class AlertRestrictionTitleViewComponent implements DynamicComponent {
  data;
  constructor() { }

  public static of(text: string) {
    return new View(AlertRestrictionTitleViewComponent, text);
  }
}
