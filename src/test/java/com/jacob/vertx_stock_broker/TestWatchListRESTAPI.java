package com.jacob.vertx_stock_broker;

import com.jacob.vertx_stock_broker.Verticles.MainVerticle;
import com.jacob.vertx_stock_broker.Verticles.RestAPIs.Assets.Asset;
import com.jacob.vertx_stock_broker.Verticles.RestAPIs.WatchList.WatchList;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
@ExtendWith(VertxExtension.class)
public class TestWatchListRESTAPI {

  private static final Logger log = LoggerFactory.getLogger(TestWatchListRESTAPI.class);
  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void adds_and_returns_watchlist_for_account(Vertx vertx, VertxTestContext testContext) throws Throwable {
    WebClient client = WebClient.create(vertx, new WebClientOptions().setDefaultPort(MainVerticle.PORT));
    UUID accountID = UUID.randomUUID();
    WatchList list = new WatchList(Arrays.asList(new Asset("AMZN"),
      new Asset("TSLA")));
    client.put("/account/watchlist/" + accountID).sendJsonObject(JsonObject.mapFrom(list))
      .onComplete(testContext.succeeding(response -> {
        JsonObject json = response.bodyAsJsonObject();
        log.info("Response: {}", json);
        assertEquals("{\"assets\":[{\"name\":\"AMZN\"},{\"name\":\"TSLA\"}]}", json.encode());
        assertEquals(200, response.statusCode());
      })).compose(next -> {
        client.get("//account/watchlist/" + accountID.toString())
          .send().onComplete(testContext.succeeding(response -> {
            JsonObject json = response.bodyAsJsonObject();
            log.info("Response GET: {}", json);
            assertEquals("{\"assets\":[{\"name\":\"AMZN\"},{\"name\":\"TSLA\"}]}", json.encode());
            assertEquals(200, response.statusCode());
            testContext.completeNow();
          }));
        return Future.succeededFuture();
      });

  }

  @Test
  void add_and_deletes_watchlist_for_account(Vertx vertx, VertxTestContext testContext){
    WebClient client = WebClient.create(vertx, new WebClientOptions().setDefaultPort(MainVerticle.PORT));
    UUID accountID = UUID.randomUUID();
    client.put("/account/watchlist/" + accountID).sendJsonObject(body())
      .onComplete(testContext.succeeding(response -> {
        JsonObject json = response.bodyAsJsonObject();
        log.info("Response: {}", json);
        assertEquals("{\"assets\":[{\"name\":\"AMZN\"},{\"name\":\"TSLA\"}]}", json.encode());
        assertEquals(200, response.statusCode());
      }))
      .compose(
        next -> {
          client.delete("//account/watchlist/" + accountID.toString())
            .send().onComplete(testContext.succeeding(response -> {
              JsonObject json = response.bodyAsJsonObject();
              log.info("Response DELETE: {}", json);
              assertEquals("{\"assets\":[{\"name\":\"AMZN\"},{\"name\":\"TSLA\"}]}", json.encode());
              assertEquals(200, response.statusCode());
              testContext.completeNow();
            }));
          return Future.succeededFuture();
        });
  }

  private JsonObject body() {
    return new WatchList(Arrays.asList(
      new Asset("AMZN"),
      new Asset("TSLA")
    )).toJsonObject();
  }

}

