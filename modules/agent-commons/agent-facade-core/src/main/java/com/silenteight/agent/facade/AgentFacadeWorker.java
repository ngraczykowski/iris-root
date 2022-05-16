package com.silenteight.agent.facade;

import lombok.SneakyThrows;

import com.silenteight.agents.v1.api.exchange.AgentOutput;

import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

class AgentFacadeWorker<T> {

  private final Function<T, AgentOutput> mapper;
  private int parallelism;
  private ExecutorService executorService;

  AgentFacadeWorker(
      Function<T, AgentOutput> mapper) {
    this.mapper = mapper;
    this.parallelism = 1;
    this.executorService = new ForkJoinPool(parallelism);
  }

  synchronized void setParallelism(int parallelism) {
    this.parallelism = parallelism;
    executorService.shutdown();
    this.executorService = new ForkJoinPool(parallelism);
  }

  List<AgentOutput> process(List<T> input) {
    if (parallelism == 1) {
      return processSequence(input);
    }
    return processParallel(input);
  }

  private List<AgentOutput> processSequence(List<T> input) {
    return input
        .stream()
        .map(mapper)
        .collect(toList());
  }

  private List<AgentOutput> processParallel(List<T> input) {
    var stream = input.parallelStream();

    var task = (Callable<List<AgentOutput>>) () -> stream.map(x -> {
      try {
        return mapper.apply(x);
      } catch (Exception e) {
        throw new WrapperException(e);
      }
    }).collect(toList());
    var result = executorService.submit(task);
    return consume(result);
  }

  @SneakyThrows
  private static <T> T consume(Future<T> future) {
    try {
      return future.get();
    } catch (ExecutionException e) {
      throw detect(e);
    }
  }

  private static Throwable detect(ExecutionException e) {
    var cause = e.getCause();
    if (cause instanceof WrapperException) {
      return cause.getCause();
    }
    return cause;
  }

  private static class WrapperException extends RuntimeException {

    private static final long serialVersionUID = -5260201846205888538L;

    WrapperException(Throwable e) {
      super(e);
    }
  }
}
