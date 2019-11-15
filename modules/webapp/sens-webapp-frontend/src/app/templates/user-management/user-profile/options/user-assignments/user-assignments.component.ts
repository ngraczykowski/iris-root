import { Component, Input, OnInit } from '@angular/core';
import { AssignmentView, } from '../../../../model/user.model';

@Component({
  selector: 'app-user-assignments',
  templateUrl: './user-assignments.component.html',
  styleUrls: ['./user-assignments.component.scss']
})
export class UserAssignmentsComponent implements OnInit {

  @Input() assignments: AssignmentView[];

  ngOnInit() {
  }
}
