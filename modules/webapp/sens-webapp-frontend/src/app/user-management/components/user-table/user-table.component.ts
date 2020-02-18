import { Component, OnInit, Input, OnChanges, ChangeDetectorRef } from '@angular/core';
import { Users } from '@app/user-management/models/users';

@Component({
  selector: 'app-user-table',
  templateUrl: './user-table.component.html',
  styleUrls: ['./user-table.component.scss']
})
export class UserTableComponent implements OnInit, OnChanges {
  @Input() users: Users[];

  constructor(
    private readonly changeDetectionRef: ChangeDetectorRef
  ) { }

  ngOnInit() {
  }

  ngOnChanges() {
    this.changeDetectionRef.detectChanges();
  }

}
