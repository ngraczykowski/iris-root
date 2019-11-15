import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-decision-tree-info-assignments',
  templateUrl: './decision-tree-info-assignments.component.html',
  styleUrls: ['./decision-tree-info-assignments.component.scss']
})
export class DecisionTreeInfoAssignmentsComponent implements OnInit {

  constructor() { }

  @Input() activations;
  @Input() assignments;

  ngOnInit() {
  }

}
