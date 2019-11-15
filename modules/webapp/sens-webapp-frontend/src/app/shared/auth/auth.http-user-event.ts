import { HttpEventType } from '@angular/common/http';
import { HttpUserEvent } from '@angular/common/http';

export class UnauthorizedHttpUserEvent implements HttpUserEvent<UnauthorizedHttpUserEvent> {
  type: HttpEventType.User;
  eventType: 'unauthorized';
}
