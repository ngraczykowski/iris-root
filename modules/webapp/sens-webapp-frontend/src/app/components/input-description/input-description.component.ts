import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-input-description',
  templateUrl: './input-description.component.html',
  styleUrls: ['./input-description.component.scss']
})
export class InputDescriptionComponent implements OnInit {

  @Input() inputDescription: string[];

  constructor() { }

  ngOnInit() {
  }

}
