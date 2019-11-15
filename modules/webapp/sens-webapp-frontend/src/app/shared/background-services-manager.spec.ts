import {
  BackgroundService,
  BackgroundServicesManager
} from '@app/shared/background-services-manager';

describe('BackgroundServicesManager', () => {
    let manager: BackgroundServicesManager;
    let actions: string[];

  class BackgroundServiceStub implements BackgroundService {

        constructor(private name: string) {}

        startService() {
            actions.push('start service: ' + this.name);
        }

        stopService() {
            actions.push('stop service: ' + this.name);
        }
    }

    beforeEach(() => {
        if (manager) {
            manager.stopService();
        }
        actions = [];
    });

    it('should have no actions when service manager has no services', () => {
        manager = new BackgroundServicesManager();
        manager.startService();
        manager.stopService();
        expect(actions).toEqual([]);
    });

    it('should have valid actions when background manager has 1 service', () => {
      manager = new BackgroundServicesManager([new BackgroundServiceStub('service1')]);
        manager.startService();
        manager.stopService();
        expect(actions).toEqual(['start service: service1', 'stop service: service1']);
    });

    it('should have valid actions when background manager has multiple services', () => {
      manager = new BackgroundServicesManager([new BackgroundServiceStub('service1'), new BackgroundServiceStub('service2')]);
        manager.startService();
        manager.stopService();
        expect(actions).toEqual(['start service: service1', 'start service: service2', 'stop service: service1', 'stop service: service2']);
    });
});
