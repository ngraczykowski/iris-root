import createSpyObj = jasmine.createSpyObj;
import { TestBed } from '@angular/core/testing';
import { EventKey } from '../../event/event.service.model';
import { InterAppEventService } from '../../event/inter-app-event.service';
import { LocalEventService } from '../../event/local-event.service';
import { UserActivityMonitorService } from './user-activity-monitor.service';

describe('UserActivityMonitorService', () => {
  const localEventServiceMock = createSpyObj(['sendEvent', 'subscribe']);
  const interAppEventServiceMock = createSpyObj(['sendEvent', 'subscribe']);
  let underTest: UserActivityMonitorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
          providers: [
            {
              provide: UserActivityMonitorService,
              useClass: UserActivityMonitorService
            },
            {
              provide: LocalEventService,
              useFactory: () => localEventServiceMock
            },
            {
              provide: InterAppEventService,
              useFactory: () => interAppEventServiceMock
            }]
        }
    );

    underTest = TestBed.get(UserActivityMonitorService);
  });

  it('should be created', () => {
    expect(underTest).toBeTruthy();
  });

  it('should subscribe to local events Key and Mouse events on start', () => {
    underTest.startService();

    expect(localEventServiceMock.subscribe).toHaveBeenCalledWith(
        jasmine.any(Function),
        jasmine.arrayContaining([EventKey.KEY_PRESS, EventKey.CLICK]));
  });

  it('should unsubscribe to previously created subscription on stop', () => {
    const subscriptionMock = createSpyObj(['unsubscribe']);
    localEventServiceMock.subscribe.and.returnValue(subscriptionMock);
    interAppEventServiceMock.subscribe.and.returnValue(subscriptionMock);

    underTest.startService();
    underTest.stopService();

    expect(subscriptionMock.unsubscribe).toHaveBeenCalled();
  });
});
