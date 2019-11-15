import { Component, Input, OnInit } from '@angular/core';
import { Change } from '@app/components/changelog/changelog.model';

@Component({
  selector: 'app-solution-change',
  templateUrl: './solution-change.component.html',
  styleUrls: ['./solution-change.component.scss']
})
export class SolutionChangeComponent implements OnInit {

  @Input() solutionChange: Change<string>;

  constructor() {}

  ngOnInit() {
  }
}
