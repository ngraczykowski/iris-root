import { Component } from '@angular/core';
import { DynamicComponent, View } from '@app/components/dynamic-view/dynamic-view.component';

@Component({
  selector: 'app-title-view',
  templateUrl: './title-view.component.html',
  styleUrls: ['./title-view.component.scss']
})
export class TitleViewComponent implements DynamicComponent {

  data;

  constructor() { }

  public static of(text: string) {
    return new View(TitleViewComponent, text);
  }

}
