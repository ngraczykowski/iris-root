import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-input-feedback',
  templateUrl: './input-feedback.component.html',
  styleUrls: ['./input-feedback.component.scss']
})
export class InputFeedbackComponent implements OnInit {

  @Input() feedbackErrors: string[];

  constructor() { }

  ngOnInit() {
  }
}
