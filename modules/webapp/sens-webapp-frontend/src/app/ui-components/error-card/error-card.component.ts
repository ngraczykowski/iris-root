import { Component, Input, OnInit } from '@angular/core';
import { ErrorCard } from '@app/ui-components/error-card/error-card';

@Component({
  selector: 'app-error-card',
  templateUrl: './error-card.component.html',
  styleUrls: ['./error-card.component.scss']
})
export class ErrorCardComponent implements OnInit {

  @Input() errorContent: ErrorCard;

  constructor() { }

  ngOnInit() {
  }

}
