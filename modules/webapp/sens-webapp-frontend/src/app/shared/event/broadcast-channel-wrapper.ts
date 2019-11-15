const enum BroadcastChannelKey {
  INTER_APP_MESSAGING_KEY = 'interappMessagingKey',
}

export class BroadcastChannelWrapper {
  private readonly channel = new BroadcastChannel(BroadcastChannelKey.INTER_APP_MESSAGING_KEY);

  set onmessage(onmessage: any) {
    this.channel.onmessage = onmessage;
  }

  get onmessage() {
    return this.channel.onmessage;
  }

  postMessage(message: any) {
    this.channel.postMessage(message);
  }

  close() {
    this.channel.close();
  }
}
