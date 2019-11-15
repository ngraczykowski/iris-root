import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-feedback-message',
  templateUrl: './feedback-message.component.html',
  styleUrls: ['./feedback-message.component.scss']
})
export class FeedbackMessageComponent implements OnInit {

  @Input() message;

  constructor() { }

  ngOnInit() {
  }
}
