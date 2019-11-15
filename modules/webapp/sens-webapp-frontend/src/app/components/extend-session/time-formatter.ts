export class TimeFormatter {

    private static DAYS_DIVIDER = 1000 * 60 * 60 * 24;
    private static HOURS_DIVIDER = 1000 * 60 * 60;
    private static MIN_DIVIDER = 1000 * 60;
    private static SEC_DIVIDER = 1000;

    format(time) {
        time = time ? time : 0;
        const days = Math.floor(time / TimeFormatter.DAYS_DIVIDER);
        time = time % TimeFormatter.DAYS_DIVIDER;
        const hours = Math.floor(time / TimeFormatter.HOURS_DIVIDER);
        time = time % TimeFormatter.HOURS_DIVIDER;
        const minutes = Math.floor(time / TimeFormatter.MIN_DIVIDER);
        time = time % TimeFormatter.MIN_DIVIDER;
        const seconds = Math.floor(time / TimeFormatter.SEC_DIVIDER);
        return this.formatTime(days, hours, minutes, seconds);
    }

    private formatTime(days, hours, minutes, seconds) {
        return (days > 0 ? (days + 'd ') : '')
            + (hours > 0 ? (this.formatTimeNumber(hours) + ':') : '')
            + this.formatTimeNumber(minutes) + ':'
            + this.formatTimeNumber(seconds);
    }

    private formatTimeNumber(number) {
        return String(Math.floor(number)).padStart(2, '0');
    }
}
