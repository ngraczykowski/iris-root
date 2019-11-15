import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-decision-tree-info-details',
  templateUrl: './decision-tree-info-details.component.html',
  styleUrls: ['./decision-tree-info-details.component.scss']
})
export class DecisionTreeInfoDetailsComponent implements OnInit {

  constructor() { }

  @Input() decisionTreeDetails;

  ngOnInit() {
  }

}
