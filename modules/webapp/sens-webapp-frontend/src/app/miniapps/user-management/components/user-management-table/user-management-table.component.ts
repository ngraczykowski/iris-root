import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  Output
} from '@angular/core';
import { UserManagementListItem } from '@endpoint/user-management/model/user-management-list-item';
import { GrowAnimation } from '@ui/animation/triggers/grow.animation';

@Component({
  selector: 'app-user-management-table',
  templateUrl: './user-management-table.component.html',
  styleUrls: ['./user-management-table.component.scss'],
  animations: [GrowAnimation.hide()],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class UserManagementTableComponent {

  @Input() list: UserManagementListItem[];
  @Input() searchQuery: string;

  @Output() edit: EventEmitter<UserManagementListItem> = new EventEmitter<UserManagementListItem>();

  hoveredRow: string;

  displayedColumns: string[] = [
      'userName',
      'displayName',
      'roles',
      'origin',
      'profile'
  ];

}
