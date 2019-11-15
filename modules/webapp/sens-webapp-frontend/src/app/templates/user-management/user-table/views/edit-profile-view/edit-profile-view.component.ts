import { Component, Input, OnInit } from '@angular/core';
import { DynamicComponent } from '../../../../../components/dynamic-view/dynamic-view.component';
import { EventKey } from '../../../../../shared/event/event.service.model';
import { LocalEventService } from '../../../../../shared/event/local-event.service';
import { User } from '../../../../model/user.model';
import { UserCellViewData } from '../user-view-factories';

export interface EditProfileViewData extends UserCellViewData {
  user: User;
}

@Component({
  selector: 'app-edit-profile-view',
  templateUrl: './edit-profile-view.component.html',
  styleUrls: ['./edit-profile-view.component.scss']
})
export class EditProfileViewComponent implements DynamicComponent, OnInit {

  @Input() data: EditProfileViewData;

  constructor(private eventService: LocalEventService) { }

  ngOnInit() {
  }

  onEdit() {
    this.eventService.sendEvent({
      key: EventKey.OPEN_EDIT_PROFILE, data: {
        'userId': this.data.user.id
      }
    });
  }

}
