import { Component, Input, OnInit } from '@angular/core';
import { DynamicComponent } from '../../../../../components/dynamic-view/dynamic-view.component';

export interface SimpleViewData {
  className: string;
  text: string;
}

@Component({
  selector: 'app-simple-view',
  templateUrl: './simple-view.component.html',
  styleUrls: ['./simple-view.component.scss']
})
export class SimpleViewComponent implements DynamicComponent, OnInit {

  @Input() data: SimpleViewData;

  constructor() { }

  ngOnInit() {
  }
}
