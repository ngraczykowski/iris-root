import { async, ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { EMPTY, of, throwError } from 'rxjs';
import { Event, EventKey } from '../../../shared/event/event.service.model';
import { LocalEventService } from '../../../shared/event/local-event.service';
import { TestModule } from '../../../test/test.module';
import { InboxService } from '../../inbox/inbox.service';
import { InboxMessage } from '../../model/inbox.model';
import { DisabledBranchNotificationMapper } from './disabled-branch-notification-mapper';
import {
  DisabledBranchNotification,
  DisabledBranchNotificationComponent
} from './disabled-branch-notification.component';
import { DisabledBranchNotificationModule } from './disabled-branch-notification.module';

class ReasoningBranchNotificationMapperMock {

  map() {}
}

describe('ReasoningBranchNotificationComponent', () => {
  let component: DisabledBranchNotificationComponent;
  let fixture: ComponentFixture<DisabledBranchNotificationComponent>;

  let service: InboxService;
  let mapper: ReasoningBranchNotificationMapperMock;
  let eventService: LocalEventService;

  beforeEach(async(() => {
    mapper = new ReasoningBranchNotificationMapperMock();

    TestBed
        .configureTestingModule({
          imports: [TestModule, DisabledBranchNotificationModule]
        })
        .overrideComponent(DisabledBranchNotificationComponent, {
          set: {
            providers: [
              {provide: DisabledBranchNotificationMapper, useValue: mapper},
            ]
          }
        })
        .compileComponents();

    eventService = TestBed.get(LocalEventService);
    service = TestBed.get(InboxService);
  }));

  function initComponent(decisionTreeId: number, matchGroupId: number) {
    fixture = TestBed.createComponent(DisabledBranchNotificationComponent);
    component = fixture.componentInstance;
    component.decisionTreeId = decisionTreeId;
    component.matchGroupId = matchGroupId;
    fixture.detectChanges();
  }

  function buildNotification(): DisabledBranchNotification {
    return {
      messageKey: 'Description',
      aiDecision: 'aiDecision',
      alertInfos: [
        {
          analystDecision: 'analystDecision',
          link: {name: 'id1', url: 'url1'}
        },
        {
          analystDecision: 'analystDecision',
          link: {name: 'id2', url: 'url2'}
        }
      ],
      date: 'dateFormatted',
      solved: false
    };
  }

  function buildMessage(): InboxMessage {
    return <InboxMessage>{
      id: 1,
      type: 'REASONING_BRANCH_DISABLED',
      message: 'Description',
      date: new Date(),
      referenceId: '1-2',
      extra: {
        decisionTreeId: 1,
        matchGroupId: 2,
        decisionTreeName: 'Decision Tree Name',
        suspendingAlerts: [
          {externalId: 'externalId1', analystDecision: 'analystDecision'},
          {externalId: 'externalId2', analystDecision: 'analystDecision'}
        ]
      }
    };
  }

  it('should create', () => {
    spyOn(service, 'getInboxMessage').and.returnValue(EMPTY);

    initComponent(0, 0);

    expect(component).toBeTruthy();
  });

  it('should load data on init component', fakeAsync(() => {
    const message = buildMessage();
    const notification = buildNotification();
    spyOn(service, 'getInboxMessage').and.returnValue(of(message));
    spyOn(mapper, 'map').and.returnValue(notification);

    initComponent(1, 2);
    component.decisionTreeId = 1;
    component.matchGroupId = 2;
    tick();

    expect(component.notification).toEqual(notification);
    expect(service.getInboxMessage).toHaveBeenCalledWith('REASONING_BRANCH_DISABLED', '1-2');
    expect(mapper.map).toHaveBeenCalledWith(message);
  }));

  it('should send solve request and reload data when invoke markAsSolved', fakeAsync(() => {
    const notification = buildNotification();
    const getInboxMessageSpy = spyOn(service, 'getInboxMessage');
    const markAsSolvedSpy = spyOn(service, 'markAsSolved');
    const mapSpy = spyOn(mapper, 'map');
    getInboxMessageSpy.and.returnValue(of(buildMessage()));
    initComponent(1, 2);
    mapSpy.and.returnValue(notification);
    markAsSolvedSpy.and.returnValue(of({}));

    component.markAsSolved();
    tick();

    expect(component.notification).toEqual(notification);
    expect(service.getInboxMessage).toHaveBeenCalledWith('REASONING_BRANCH_DISABLED', '1-2');
    expect(service.markAsSolved).toHaveBeenCalledWith(1);
  }));

  it('should set notification to null when error EntityNotFoundException was thrown on loading', fakeAsync(() => {
    const getInboxMessageSpy = spyOn(service, 'getInboxMessage');
    getInboxMessageSpy.and.returnValue(of({}));
    spyOn(eventService, 'sendEvent');
    initComponent(1, 2);
    component.notification = buildNotification();

    getInboxMessageSpy.and.returnValue(throwError({error: {key: 'EntityNotFoundException'}}));
    component.ngOnInit();
    tick();

    expect(component.notification).toEqual(null);
    expect(eventService.sendEvent).not.toHaveBeenCalled();
  }));

  it('should send notification error when error other than EntityNotFoundException was thrown on loading', fakeAsync(() => {
    const notification = buildNotification();
    const getInboxMessageSpy = spyOn(service, 'getInboxMessage');
    getInboxMessageSpy.and.returnValue(of({}));
    spyOn(eventService, 'sendEvent');
    initComponent(1, 2);
    component.notification = notification;

    getInboxMessageSpy.and.returnValue(throwError({}));
    component.ngOnInit();
    tick();

    expect(component.notification).toEqual(notification);
    expect(eventService.sendEvent).toHaveBeenCalledWith(<Event>{
      key: EventKey.NOTIFICATION,
      data: {
        type: 'error',
        message: 'reasoningBranch.notification.message.error.load.UNKNOWN'
      }
    });
  }));

  it('should send notification error when error was thrown on solving', fakeAsync(() => {
    spyOn(service, 'getInboxMessage').and.returnValue(of({}));
    spyOn(service, 'markAsSolved').and.returnValue(throwError({}));
    spyOn(eventService, 'sendEvent');

    initComponent(1, 2);
    component.markAsSolved();
    tick();

    expect(eventService.sendEvent).toHaveBeenCalledWith(<Event>{
      key: EventKey.NOTIFICATION,
      data: {
        type: 'error',
        message: 'reasoningBranch.notification.message.error.solve.UNKNOWN'
      }
    });
  }));
});
