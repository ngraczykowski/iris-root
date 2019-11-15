import { Subscription } from 'rxjs';

export interface Event {
  key: EventKey;
  data?: any;
}

export interface EventService {
  sendEvent(message: Event);

  subscribe(eventHandler: (ev: Event) => void, keyFilters?: Array<EventKey>): Subscription;
}

// TODO(iwnek) we should divide keys into categories
export const enum EventKey {
  KEY_PRESS = 'onUserKeyPress',
  CLICK = 'onUserClick',
  ACTIVITY = 'onUserActivity',
  OTHER_CONTEXT_ACTIVITY = 'onOtherContextActivity',
  NOTIFICATION = 'notification',
  AUTO_LOGOUT = 'autoLogout',
  SESSION_CLOSE_TO_EXPIRE = 'sessionCloseToExpire',
  SHOW_ERROR_WINDOW = 'showErrorWindow',
  HIDE_ERROR_WINDOW = 'hideErrorWindow',
  EXTEND_SESSION = 'extendSession',

  OPEN_EDIT_PROFILE = 'openEditProfile',
  OPEN_NEW_PROFILE = 'openNewProfile',
}
