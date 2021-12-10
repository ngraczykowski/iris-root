import math
from datetime import datetime, timedelta


class ProgressTick:
    def __init__(self, tick: int):
        self.ref_time = datetime.now()
        self.tick = tick

    def elapsed_since(self, referenced: "ProgressTick") -> timedelta:
        return self.ref_time - referenced.ref_time

    def speed_since(self, referenced: "ProgressTick") -> float:
        chunk_count = self.tick - referenced.tick
        return chunk_count / self.elapsed_since(referenced).total_seconds()


class ProgressLogger:
    def __init__(self, step: int, size=None):
        self.started = None
        self.referenced = None
        self.last = None
        self.finished = None

        self.step = step
        self.size = size

    def start(self):
        self.started = self.referenced = self.last = ProgressTick(0)

        suffix = " {} items.".format(self.size) if self.size is not None else "."
        print("Started processing{}".format(suffix))
        print()

    def tick(self, count):
        self.last = ProgressTick(self.last.tick + count)

        before = math.floor(self.referenced.tick / self.step)
        after = math.floor(self.last.tick / self.step)

        if after > before:
            print(f"Processed {self._get_current_progress()} items.")

            current_speed = self.last.speed_since(self.referenced)
            print(f"Current speed: {round(current_speed, 2)} records/s.")

            average_speed = self.last.speed_since(self.started)
            print(f"Average speed: {round(average_speed, 2)} records/s.")

            if self.size is not None and self.last.tick < self.size:
                left_seconds = self._calculate_left_seconds(average_speed)
                print(f"Estimated time left: {timedelta(seconds=left_seconds)}.")

            print()

            self.referenced = self.last

    def finish(self):
        self.finished = ProgressTick(self.last.tick)

        print("Finished processing.")
        print(f"Processed {self._get_current_progress()} items.")
        average_speed = self.finished.speed_since(self.started)
        print(f"Average speed: {round(average_speed, 2)} records/s.")
        elapsed = self.finished.elapsed_since(self.started)
        print(f"Elapsed: {elapsed}.")

    def _get_current_progress(self) -> str:
        return str(self.last.tick) + ("/" + str(self.size) if self.size is not None else "")

    def _calculate_left_seconds(self, average_speed: float) -> float:
        left_records = self.size - self.last.tick
        return left_records / average_speed
