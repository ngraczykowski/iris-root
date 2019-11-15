import { EventKey } from '@app/shared/event/event.service.model';

export const rejectSuccessfulEvent = {
  key: EventKey.NOTIFICATION,
  data: {
    type: 'success',
    message: 'approver.rejectSelected.events.rejectSuccessful'
  }
};

export const rejectFailedEvent = {
  key: EventKey.NOTIFICATION,
  data: {
    type: 'error',
    message: 'approver.rejectSelected.events.rejectFailed'
  }
};

export const approvalSuccessfulEvent = {
  key: EventKey.NOTIFICATION,
  data: {
    type: 'success',
    message: 'approver.approveSelected.events.approvalSuccessful'
  }
};

export const approvalFailedEvent = {
  key: EventKey.NOTIFICATION,
  data: {
    type: 'error',
    message: 'approver.approveSelected.events.approvalFailed'
  }
};
