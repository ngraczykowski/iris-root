export abstract class LastUserActivityStorage {

  abstract getLastUserActivityDate(): Date;

  abstract getLastUserActivityTime(): number;
}
