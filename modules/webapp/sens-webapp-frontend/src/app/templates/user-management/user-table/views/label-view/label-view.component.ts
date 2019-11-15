import { Component, Input, OnInit } from '@angular/core';
import { DynamicComponent } from '../../../../../components/dynamic-view/dynamic-view.component';

export interface LabelViewData {
  label: string;
}

@Component({
  selector: 'app-label-view',
  templateUrl: './label-view.component.html',
  styleUrls: ['./label-view.component.scss']
})
export class LabelViewComponent implements DynamicComponent, OnInit {

  @Input() data: LabelViewData;

  constructor() { }

  ngOnInit() {
  }

}
