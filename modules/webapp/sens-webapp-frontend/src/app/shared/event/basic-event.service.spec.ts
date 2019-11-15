import { EventKey } from '@app/shared/event/event.service.model';
import { LocalEventService } from '@app/shared/event/local-event.service';

describe('LocalEventService', () => {
  let eventService: LocalEventService;
  let actions: Array<String>;
  const testEventKey = EventKey.CLICK;
  const testEventKey2 = EventKey.ACTIVITY;

  function createEventHandlerStub(name?) {
    return event => actions.push((name ? name + ' ' : '') + 'handled event: "' + event.key + '", data: "' + event.data + '"');
  }

  beforeEach(() => {
    actions = [];
    eventService = new LocalEventService();
  });

  it('no actions when sent event to event service without subscriptions', () => {
    eventService.sendEvent({key: testEventKey, data: 'data'});
    expect(actions).toEqual([]);
  });

  it('added action when sent event to event service with all event subscription', () => {
    eventService.subscribe(createEventHandlerStub());
    eventService.sendEvent({key: testEventKey, data: 'data'});
    expect(actions).toEqual(['handled event: "' + testEventKey + '", data: "data"']);
  });

  it('after sending dedicated events to event service with filtered event subscription, '
      + ' actions should contain only dedicated events', () => {
    eventService.subscribe(createEventHandlerStub(), [testEventKey]);
    eventService.sendEvent({key: testEventKey, data: 'data1'});
    eventService.sendEvent({key: testEventKey2, data: 'data2'});
    expect(actions).toEqual(['handled event: "' + testEventKey + '", data: "data1"']);
  });

  it('after sending dedicated events to event service with multiple subscription, actions should be valid', () => {
    eventService.subscribe(createEventHandlerStub('handler1'));
    eventService.subscribe(createEventHandlerStub('handler2'), [testEventKey]);
    eventService.subscribe(createEventHandlerStub('handler3'), [testEventKey2]);

    eventService.sendEvent({key: testEventKey, data: 'data1'});
    eventService.sendEvent({key: testEventKey2, data: 'data2'});

    expect(actions).toContain('handler1 handled event: "' + testEventKey + '", data: "data1"');
    expect(actions).toContain('handler1 handled event: "' + testEventKey2 + '", data: "data2"');
    expect(actions).toContain('handler2 handled event: "' + testEventKey + '", data: "data1"');
    expect(actions).toContain('handler3 handled event: "' + testEventKey2 + '", data: "data2"');
  });
});
