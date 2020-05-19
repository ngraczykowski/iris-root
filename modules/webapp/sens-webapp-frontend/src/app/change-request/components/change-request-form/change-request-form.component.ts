import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-change-request-form',
  templateUrl: './change-request-form.component.html',
  styleUrls: ['./change-request-form.component.scss']
})
export class ChangeRequestFormComponent implements OnInit {

  translatePrefix = 'changeRequest.configureForm.';

  constructor() { }

  ngOnInit() {
  }

}
