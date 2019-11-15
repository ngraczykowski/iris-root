import { EventKey } from '@app/shared/event/event.service.model';
import { InterAppEventService } from '@app/shared/event/inter-app-event.service';
import createSpyObj = jasmine.createSpyObj;

describe('InterAppEventService', () => {
  const channelMock = createSpyObj('channel', ['postMessage', 'close']);
  let underTest: InterAppEventService;

  beforeEach(() => {
    underTest = new InterAppEventService(channelMock);
  });

  it('should be created', () => {
    expect(underTest).toBeTruthy();
  });

  it('should invoke Broadcast channel\'s postMessage with valid data on sendEvent', () => {
    const givenMessage = {
      key: EventKey.CLICK, data: 'someData'
    };
    underTest.sendEvent(givenMessage);

    expect(channelMock.postMessage).toHaveBeenCalledWith(givenMessage);
  });

  it('should close channel in ngOnDestroy method', () => {
    underTest.ngOnDestroy();

    expect(channelMock.close).toHaveBeenCalled();
  });

  it('should bind channel.onmessage to some function', () => {
    expect(channelMock.onmessage).toEqual(jasmine.any(Function));
  });

  describe('when some message was received through broadcast channel', () => {
    it('subscriber should receive this message\'s data', () => {
      const exampleMessage = {key: EventKey.CLICK, data: 'someData'};
      let receivedMessage;
      underTest.subscribe(e => receivedMessage = e);

      channelMock.onmessage(exampleMessage);

      expect(receivedMessage).toEqual(exampleMessage.data);
    });
  });
});
