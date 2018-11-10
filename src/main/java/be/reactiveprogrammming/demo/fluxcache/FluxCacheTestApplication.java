package be.reactiveprogrammming.demo.fluxcache;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

public class FluxCacheTestApplication {

  public static void main(String[] args) throws InterruptedException {
    fluxConnectSecondSubscriberWithoutCache();
    System.out.println("---------------------------------------");
    fluxConnectSecondSubscriberWithCache();
  }

  private static void fluxConnectSecondSubscriberWithoutCache() throws InterruptedException {
    Flux<Long> startFlux = Flux.interval(Duration.ofSeconds(1)).share();

    Flux firstFlux = Flux.from(startFlux);
    Disposable disposable = firstFlux.subscribe(out -> System.out.println("firstFlux value: " + out));
    new CountDownLatch(1).await(5, TimeUnit.SECONDS);

    Flux secondFlux = Flux.from(startFlux);
    Disposable secondDisposable = secondFlux.subscribe(out -> System.out.println("secondFlux value: " + out));
    new CountDownLatch(1).await(5, TimeUnit.SECONDS);

    disposable.dispose();
    secondDisposable.dispose();
  }

  private static void fluxConnectSecondSubscriberWithCache() throws InterruptedException {
    Flux<Long> startFlux = Flux.interval(Duration.ofSeconds(1)).share().cache();

    Flux firstFlux = Flux.from(startFlux);
    Disposable disposable = firstFlux.subscribe(out -> System.out.println("firstFlux value: " + out));
    new CountDownLatch(1).await(5, TimeUnit.SECONDS);

    Flux secondFlux = startFlux.share();
    Disposable secondDisposable = secondFlux.subscribe(out -> System.out.println("secondFlux value: " + out));
    new CountDownLatch(1).await(5, TimeUnit.SECONDS);

    disposable.dispose();
    secondDisposable.dispose();
  }

}
