import { Component, Input, OnInit } from '@angular/core';
import { AbstractControl } from '@angular/forms';
import { StatusChange } from '@app/templates/decision-tree/branch-change/branch-updater/change-request.model';

@Component({
  selector: 'app-change-request-status-option',
  templateUrl: './change-request-status-option.component.html',
  styleUrls: ['./change-request-status-option.component.scss']
})
export class ChangeRequestStatusOptionComponent implements OnInit {

  @Input() availableOptions: StatusChange[];
  @Input() control: AbstractControl;

  ngOnInit() {
  }

}
