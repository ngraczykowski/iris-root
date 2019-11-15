import { Component, Input, OnInit } from '@angular/core';
import { DynamicComponent } from '@app/components/dynamic-view/dynamic-view.component';
import { SelectableTablePageService } from '@app/components/selectable-dynamic-table/selectable-table-page-service';

export class SelectAllViewData {
  service: SelectableTablePageService<any>;
}

@Component({
  selector: 'app-select-all-view',
  templateUrl: './select-all-view.component.html',
  styleUrls: ['./select-all-view.component.scss']
})
export class SelectAllViewComponent implements DynamicComponent, OnInit {

  @Input() data: SelectAllViewData;

  constructor() {}

  ngOnInit() {
  }

  isChecked() {
    return this.data.service.areAllSelected();
  }

  toggle() {
    return this.data.service.toggleAll();
  }
}
