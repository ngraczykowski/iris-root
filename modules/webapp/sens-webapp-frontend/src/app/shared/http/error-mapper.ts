export class ErrorMapper {
  constructor(private mapping, private prefix?) { }

  public static hasExceptionCode(error: any, code: any): boolean {
    return error && error.exception && error.exception === code;
  }

  public static hasErrorKey(error: any, key: any): boolean {
    return error && error.key && error.key === key;
  }

  get(error) {
    return (this.prefix || '') + this.getName(error);
  }

  private getName(error) {
    if (error && error.error && error.error.key) {
      const e = this.mapping[error.error.key];
      if (e) {
        return e;
      }
    }
    return 'UNKNOWN';
  }
}
