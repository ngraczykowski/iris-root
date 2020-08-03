import { Observable, Subject } from 'rxjs';

export class LifecycleHelper {

  private destroySubject: Subject<void> = new Subject<void>();

  public get destroyed(): Observable<void> {
    return this.destroySubject.asObservable();
  }

  public destroy(): void {
    this.destroySubject.next();
    this.destroySubject.complete();
  }

}
