package com.jacob.vertx_stock_broker;


import com.jacob.vertx_stock_broker.Verticles.MainVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
public class TestAssetsRESTApi{

  private static final Logger log = LoggerFactory.getLogger(TestAssetsRESTApi.class);
  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void returns_all_assets(Vertx vertx, VertxTestContext testContext) throws Throwable {
    WebClient client = WebClient.create(vertx, new WebClientOptions().setDefaultPort(MainVerticle.PORT));
    client.get("/assets").send()
      .onComplete(testContext.succeeding(response -> {
        JsonArray json = response.bodyAsJsonArray();
        log.info("Response: {}", json);
        assertEquals("[{\"name\":\"AAPL\"},{\"name\":\"AMZN\"},{\"name\":\"NFLX\"},{\"name\":\"TSLA\"},{\"name\":\"FB\"},{\"name\":\"GOOG\"},{\"name\":\"MSFT\"}]", json.encode());
        assertEquals(200, response.statusCode());
        testContext.completeNow();
      }));

  }

}
