import {
  async,
  ComponentFixture,
  discardPeriodicTasks,
  fakeAsync,
  TestBed,
  tick
} from '@angular/core/testing';
import { asyncScheduler, EMPTY, of, throwError } from 'rxjs';
import { tap } from 'rxjs/operators';
import { environment } from '../../../../environments/environment';
import { EventKey } from '../../../shared/event/event.service.model';
import { LocalEventService } from '../../../shared/event/local-event.service';
import { TestModule } from '../../../test/test.module';
import { InboxStatistics } from '../../model/inbox.model';
import { InboxModule } from '../inbox.module';
import { InboxService } from '../inbox.service';

import { InboxTabComponent } from './inbox-tab.component';

describe('InboxTabComponent', () => {
  let component: InboxTabComponent;
  let fixture: ComponentFixture<InboxTabComponent>;

  let inboxService: InboxService;
  let eventService: LocalEventService;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [TestModule, InboxModule]
        })
        .compileComponents();

    inboxService = TestBed.get(InboxService);
    eventService = TestBed.get(LocalEventService);
  }));

  function initComponent() {
    fixture = TestBed.createComponent(InboxTabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }

  it('should create', () => {
    spyOn(inboxService, 'getStats').and.returnValue(EMPTY);

    initComponent();

    expect(component).toBeTruthy();
  });

  it('should update unsolvedInboxMessageCount after init immediately', fakeAsync(() => {
    spyOn(inboxService, 'getStats').and.returnValue(
        of(<InboxStatistics>{total: 20, stats: {'UNSOLVED': 10}}));

    initComponent();

    tick();
    expect(component.unsolvedInboxMessageCount).toEqual(10);

    discardPeriodicTasks();
  }));

  it('should update unsolvedInboxMessageCount after specified pool time', fakeAsync(() => {
    const messageCountSpy = spyOn(inboxService, 'getStats');
    messageCountSpy.and.returnValue(
        of(<InboxStatistics>{total: 20, stats: {'UNSOLVED': 10}}));

    initComponent();

    tick();
    expect(component.unsolvedInboxMessageCount).toEqual(10);

    messageCountSpy.and.returnValue(
        of(<InboxStatistics>{total: 40, stats: {'UNSOLVED': 20}}));

    tick(environment.mainNavigation.infoboxTab.pollInboxStatsInMs);
    expect(component.unsolvedInboxMessageCount).toEqual(20);

    discardPeriodicTasks();
  }));

  it('should try again after poll time when error occured', fakeAsync(() => {
    const messageCountSpy = spyOn(inboxService, 'getStats');
    messageCountSpy.and.returnValue(throwError({error: {key: 'key'}}));

    initComponent();
    tick();

    messageCountSpy.and.returnValue(
        of(<InboxStatistics>{total: 40, stats: {'UNSOLVED': 20}}));

    tick(environment.mainNavigation.infoboxTab.pollInboxStatsInMs);
    expect(component.unsolvedInboxMessageCount).toEqual(20);

    discardPeriodicTasks();
  }));

  it('should send event when error occured', fakeAsync(() => {
    spyOn(inboxService, 'getStats').and.returnValue(throwError({error: {key: 'key'}}));
    spyOn(eventService, 'sendEvent');

    initComponent();
    tick();

    expect(eventService.sendEvent).toHaveBeenCalledWith({
      key: EventKey.NOTIFICATION,
      data: {
        type: 'error',
        message: 'navigation.infoboxTab.error.UNKNOWN'
      }
    });

    discardPeriodicTasks();
  }));

  it('getStats should be called once if it lasts a long time', fakeAsync(() => {
    spyOn(inboxService, 'getStats').and
        .returnValue(
            of(<InboxStatistics>  {stats: {}}, asyncScheduler)
                .pipe(tap(() => tick(10000)))
        );

    initComponent();
    tick(10000);

    expect(inboxService.getStats).toHaveBeenCalledTimes(1);

    discardPeriodicTasks();
  }));
});
