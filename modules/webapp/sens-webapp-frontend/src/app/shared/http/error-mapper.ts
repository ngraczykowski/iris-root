export class ErrorMapper {
  constructor(private mapping, private prefix?) { }

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
