import { Component, Inject } from '@angular/core';
import { Event, EventKey } from '@app/shared/event/event.service.model';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { WINDOW } from '@app/shared/window.service';

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.scss']
})
export class UserManagementComponent {
  private readonly downloadUserListUrl = '/rest/webapp/api/users/export';


  constructor(
    private eventService: LocalEventService,
    @Inject(WINDOW) public window: Window
  ) { }

  onAddNewUser() {
    this.eventService.sendEvent(<Event> {
      key: EventKey.OPEN_NEW_PROFILE
    });
  }

  onDownloadUserList() {
    this.window.location.assign(this.downloadUserListUrl);
    this.sendBriefMessage('user-management.options.downloadCSV.briefMessage');
  }

  private sendBriefMessage(messageContent) {
    this.eventService.sendEvent({
      key: EventKey.NOTIFICATION,
      data: {
        message: messageContent
      }
    });
  }
}
