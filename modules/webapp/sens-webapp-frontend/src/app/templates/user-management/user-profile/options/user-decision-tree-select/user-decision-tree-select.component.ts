import { Component, Input, OnInit } from '@angular/core';
import { FormArray, FormControl, FormGroup } from '@angular/forms';
import { DecisionTree } from '../../../../model/decision-tree.model';

@Component({
  selector: 'app-user-decision-tree-select',
  templateUrl: './user-decision-tree-select.component.html',
  styleUrls: ['./user-decision-tree-select.component.scss']
})
export class UserDecisionTreeSelectComponent implements OnInit {

  @Input() control: FormArray;
  @Input() decisionTrees: DecisionTree[];

  ngOnInit() {
    if (this.control.length === 0) {
      this.addNewOption();
    }
  }

  addNewOption() {
    this.control.push(new FormGroup({
      'decisionTreeId': new FormControl(null),
    }));
  }

  removeOption(index: number) {
    this.control.removeAt(index);
  }
}
