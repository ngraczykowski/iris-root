import { Component, Input, OnInit } from '@angular/core';
import { AbstractControl } from '@angular/forms';

@Component({
  selector: 'app-input-control-feedback',
  templateUrl: './input-control-feedback.component.html',
  styleUrls: ['./input-control-feedback.component.scss']
})
export class InputControlFeedbackComponent implements OnInit {

  @Input() translatePrefix: string;
  @Input() show: boolean;
  @Input() control: AbstractControl;

  constructor() { }

  ngOnInit() {
  }

  getErrors() {
    if (this.control && this.control.errors) {
      const errors = this.control.errors;
      return Object.keys(errors)
          .filter(e => !!errors[e])
          .map(e => `${this.translatePrefix}.${e}`);
    }
    return [];
  }
}
