import { Component, Input, OnInit } from '@angular/core';
import { DynamicComponent } from '../../../dynamic-view/dynamic-view.component';
import { SelectableTablePageService } from '../../selectable-table-page-service';

export interface SelectOneViewData {
  service: SelectableTablePageService<any>;
  index: number;
}

@Component({
  selector: 'app-branch-select-one-view',
  templateUrl: './select-one-view.component.html',
  styleUrls: ['./select-one-view.component.scss']
})
export class SelectOneViewComponent implements DynamicComponent, OnInit {

  @Input() data: SelectOneViewData;

  constructor() { }

  ngOnInit() {
  }

  isChecked() {
    return this.data.service.isSelected(this.data.index);
  }

  toggle() {
    return this.data.service.toggleOne(this.data.index);
  }
}
