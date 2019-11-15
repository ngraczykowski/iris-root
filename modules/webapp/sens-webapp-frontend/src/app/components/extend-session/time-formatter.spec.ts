import { TimeFormatter } from './time-formatter';

describe('TimeFormatter', () => {

    let timeFormatter: TimeFormatter;

    function assertTime(time, expected) {
        expect(timeFormatter.format(time)).toBe(expected);
    }

    beforeEach(() => {
        timeFormatter = new TimeFormatter();
    });

    it('should format valid time',
        () => {
            assertTime(null, '00:00');
            assertTime(0, '00:00');
            assertTime(1000 * 60 * 20, '20:00');
            assertTime((1000 * 60 * 20) + (1000 * 30), '20:30');
            assertTime((1000 * 60 * 2) + (1000 * 31), '02:31');
            assertTime((1000 * 60 * 2) + (1000 * 3), '02:03');
            assertTime((1000 * 60 * 60 * 3) + (1000 * 60 * 20) + (1000 * 3), '03:20:03');
            assertTime((1000 * 60 * 60 * 24 * 4) + (1000 * 60 * 60 * 3) + (1000 * 60 * 20) + (1000 * 3), '4d 03:20:03');
        });
});
