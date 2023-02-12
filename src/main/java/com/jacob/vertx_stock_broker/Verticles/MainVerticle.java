package com.jacob.vertx_stock_broker.Verticles;

import com.jacob.vertx_stock_broker.Verticles.RestAPIs.Assets.AssetRestAPI;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
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
    final Router restApi = Router.router(vertx);
    restApi.route().failureHandler(errorContext -> {
      if(errorContext.response().ended()){
        //ignore completed response
        return;
      }
      log.error("Route Error:", errorContext.failure());
      errorContext.response().setStatusCode(500).end(new JsonObject().put("message", "Something went wrong :(").toBuffer());
    });
    AssetRestAPI.attach(restApi);

    vertx.createHttpServer().requestHandler(restApi).exceptionHandler(error -> {
      log.error("HttpServer error: ", error);
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
