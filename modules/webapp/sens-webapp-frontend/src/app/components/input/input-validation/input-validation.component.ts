import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-input-validation',
  templateUrl: './input-validation.component.html',
  styleUrls: ['./input-validation.component.scss']
})
export class InputValidationComponent implements OnInit {

  @Input() listOfErrors: string[];

  constructor() { }

  ngOnInit() {
  }

}
