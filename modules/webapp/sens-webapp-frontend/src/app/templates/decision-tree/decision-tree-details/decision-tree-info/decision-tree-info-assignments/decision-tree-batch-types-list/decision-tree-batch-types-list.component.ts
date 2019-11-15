import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-decision-tree-batch-types-list',
  templateUrl: './decision-tree-batch-types-list.component.html',
  styleUrls: ['./decision-tree-batch-types-list.component.scss']
})
export class DecisionTreeBatchTypesListComponent implements OnInit {

  constructor() { }

  @Input() batchTypeList;
  @Input() batchTypeStatus;

  ngOnInit() {
    this.sortAlphabetically();
  }

  sortAlphabetically() {
    this.batchTypeList.sort();
  }
}
