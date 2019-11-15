import { InjectionToken } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { EventKey, EventService } from '../../event/event.service.model';
import { LastUserActivityLocalStorage } from './last-user-activity-local-storage';
import { SessionStorageKey } from './session-storage-key';
import arrayContaining = jasmine.arrayContaining;
import createSpyObj = jasmine.createSpyObj;

describe('LastUserActivityLocalStorage', () => {
  let underTest: LastUserActivityLocalStorage;
  let eventServiceMock;
  let sendEvent;
  const subscribeMethodMock = (eventHandler) => sendEvent = eventHandler;


  beforeEach(() => {
    const eventServiceMockToken = new InjectionToken<EventService>('mock');
    eventServiceMock = createSpyObj(['subscribe', 'sendEvent']);
    eventServiceMock.subscribe.and.callFake(subscribeMethodMock);

    TestBed.configureTestingModule({
      providers: [{
        provide: LastUserActivityLocalStorage,
        useClass: LastUserActivityLocalStorage,
        deps: [eventServiceMockToken]
      },
        {
          provide: eventServiceMockToken,
          useFactory: () => eventServiceMock
        }]
    });

    underTest = TestBed.get(LastUserActivityLocalStorage);
  });

  it('should be created', () => {
    expect(underTest).toBeTruthy();
  });

  it('should subscribe to Activity event', () => {
    expect(eventServiceMock.subscribe)
        .toHaveBeenCalledWith(
            jasmine.any(Function),
            arrayContaining([EventKey.ACTIVITY])
        );
  });

  describe('given activity time in storage', () => {
    const givenActivityTime = new Date();
    beforeEach(() => {
      localStorage.setItem(SessionStorageKey.LAST_ACTIVITY_TIME, givenActivityTime.toISOString());
    });

    it('should return given time, when getLastUserActivityDate called', () => {
      expect(underTest.getLastUserActivityDate()).toEqual(givenActivityTime);
    });

    it('should set new time to storage on event', () => {
      const currentDate = localStorage.getItem(SessionStorageKey.LAST_ACTIVITY_TIME);

      sendEvent();
      const newDate = localStorage.getItem(SessionStorageKey.LAST_ACTIVITY_TIME);

      expect(currentDate).not.toEqual(newDate);
    });

    it('should set new time to storage on event and return it, ' +
        'when getLastUserActivityDate called', () => {
      const mockedDate = new Date();
      jasmine.clock().mockDate(mockedDate);

      sendEvent();
      const actualDate = underTest.getLastUserActivityDate();

      expect(actualDate).toEqual(mockedDate);
    });
  });
});
