package com.jacob.vertx_stock_broker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends AbstractVerticle {

  private static final Logger log = LoggerFactory.getLogger(MainVerticle.class);
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.exceptionHandler(error -> {
      log.error("Unhandled: {}", error);
    });
    vertx.deployVerticle(new MainVerticle(), ar -> {
      if(ar.failed()) {
        log.error("Failed to deploy verticle. Error: {}", ar.cause());
        return;
      }
      log.info("Verticle Deployed {}", MainVerticle.class);
    });
  }
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.createHttpServer().requestHandler(req -> {
      req.response()
        .putHeader("content-type", "text/plain")
        .end("Hello from Vert.x!");
    }).listen(8888, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }
}
