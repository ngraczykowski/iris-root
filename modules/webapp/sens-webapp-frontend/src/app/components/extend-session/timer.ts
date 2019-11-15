import { TimerObservable } from 'rxjs/observable/TimerObservable';
import { Subscription } from 'rxjs/Subscription';

export class Timer {

    private expirationTime;
    private timerSubscription: Subscription;

    constructor(private refreshTime = 500) { }

    start(expirationTime, refreshCallback, finishCallback) {
        this.cancel();
        this.expirationTime = expirationTime;
        this.timerSubscription = TimerObservable.create(0, this.refreshTime).subscribe(
            () => {
                const timeLeft = this.calculateTimeLeft();
                refreshCallback(timeLeft);
                if (timeLeft <= 0) {
                    this.cancel();
                    finishCallback();
                }
            }
        );
    }

    cancel() {
        if (this.timerSubscription) {
            this.timerSubscription.unsubscribe();
        }
        this.expirationTime = null;
    }

    private calculateTimeLeft() {
        const timeLeft = this.expirationTime - new Date().getTime();
        return timeLeft >= 0 ? timeLeft : 0;
    }
}
