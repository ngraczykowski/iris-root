import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-decision-tree-info-activations',
  templateUrl: './decision-tree-info-activations.component.html',
  styleUrls: ['./decision-tree-info-activations.component.scss']
})
export class DecisionTreeInfoActivationsComponent implements OnInit {

  constructor() { }

  @Input() activations;

  ngOnInit() {
  }
}
