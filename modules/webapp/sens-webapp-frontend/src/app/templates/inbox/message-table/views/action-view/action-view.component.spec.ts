import { async, ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { asyncScheduler, of, throwError } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Event, EventKey } from '../../../../../shared/event/event.service.model';
import { LocalEventService } from '../../../../../shared/event/local-event.service';
import { TestModule } from '../../../../../test/test.module';
import { InboxModule } from '../../../inbox.module';
import { InboxService } from '../../../inbox.service';

import { ActionViewComponent, ActionViewData } from './action-view.component';

describe('ActionViewComponent', () => {
  let component: ActionViewComponent;
  let fixture: ComponentFixture<ActionViewComponent>;

  let eventService: LocalEventService;
  let inboxService: InboxService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      providers: [LocalEventService],
      imports: [TestModule, InboxModule]
    }).compileComponents();

    eventService = TestBed.get(LocalEventService);
    inboxService = TestBed.get(InboxService);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ActionViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should invoke inboxService.markAsSolved when strategy', () => {
    spyOn(inboxService, 'markAsSolved').and.returnValue(of({}));
    component.data = <ActionViewData> {inboxMessageId: 1};

    component.solve();

    expect(inboxService.markAsSolved).toHaveBeenCalledWith(1);
  });

  it('should set solved true after solving success', fakeAsync(() => {
    spyOn(inboxService, 'markAsSolved').and.returnValue(of({}));
    component.data = <ActionViewData> {inboxMessageId: 1};

    component.solve();
    tick();

    expect(component.solved).toBeTruthy();
  }));

  it('should keep solved false after solving error', fakeAsync(() => {
    spyOn(inboxService, 'markAsSolved').and.returnValue(throwError({}));
    component.data = <ActionViewData> {inboxMessageId: 1};

    component.solve();
    tick();

    expect(component.solved).toBeFalsy();
  }));

  it('should send success event after solving success', fakeAsync(() => {
    spyOn(inboxService, 'markAsSolved').and.returnValue(of({}));
    spyOn(eventService, 'sendEvent');
    component.data = <ActionViewData> {inboxMessageId: 1};

    component.solve();
    tick();

    expect(eventService.sendEvent).toHaveBeenCalledWith(<Event> {
      key: EventKey.NOTIFICATION,
      data: {
        type: 'success',
        message: 'inbox.notification.success.solve'
      }
    });
  }));

  it('should send error event after solving error', fakeAsync(() => {
    spyOn(inboxService, 'markAsSolved').and.returnValue(throwError({}));
    spyOn(eventService, 'sendEvent');
    component.data = <ActionViewData> {inboxMessageId: 1};

    component.solve();
    tick();

    expect(eventService.sendEvent).toHaveBeenCalledWith(<Event> {
      key: EventKey.NOTIFICATION,
      data: {
        type: 'error',
        message: 'inbox.notification.error.solve.UNKNOWN'
      }
    });
  }));

  it('should set inProgress while solving', fakeAsync(() => {
    spyOn(inboxService, 'markAsSolved')
        .and.returnValue(of({}, asyncScheduler).pipe(tap(() => tick(100))));
    component.data = <ActionViewData> {inboxMessageId: 1};

    component.solve();

    expect(component.inProgress).toBeTruthy();

    tick(50);
    expect(component.inProgress).toBeTruthy();

    tick(50);
    expect(component.inProgress).toBeFalsy();
  }));
});
