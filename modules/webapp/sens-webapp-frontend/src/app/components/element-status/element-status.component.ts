import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-element-status',
  templateUrl: './element-status.component.html',
  styleUrls: ['./element-status.component.scss']
})
export class ElementStatusComponent implements OnInit {

  @Input() translatePrefix: string;
  @Input() status: string;

  constructor() { }

  ngOnInit() {
  }

}
