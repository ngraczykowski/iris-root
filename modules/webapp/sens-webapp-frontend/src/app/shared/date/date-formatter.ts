export class ParseDateException {

}

export class DateFormatter {
  static format(dateString): string {
    const timestamp = DateFormatter.parse(dateString);
    return new Date(timestamp).toLocaleString();
  }

  private static parse(dateString) {
    const parsed = Date.parse(dateString);
    if (!parsed) {
      throw new ParseDateException();
    }
    return parsed;
  }
}
