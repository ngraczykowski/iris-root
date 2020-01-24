import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-decision-tree-decision-groups-list',
  templateUrl: './decision-tree-decision-groups-list.component.html',
  styleUrls: ['./decision-tree-decision-groups-list.component.scss']
})
export class DecisionTreeDecisionGroupsListComponent implements OnInit {

  constructor() { }

  @Input() decisionGroups;

  ngOnInit() {
    this.sortAlphabetically();
  }

  sortAlphabetically() {
    this.decisionGroups.sort((a, b) => a.name.localeCompare(b.name));
  }

  trackByFn(index) {
    return index;
  }
}
