package com.jacob.vertx_stock_broker.Verticles.RestAPIs.Assets;

import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class AssetRestAPI {

  private static final Logger log = LoggerFactory.getLogger(AssetRestAPI.class);

  public static final List<String> ASSETS = Arrays.asList("AAPL", "AMZN","NFLX", "TSLA", "FB", "GOOG", "MSFT");

  public static void attach(Router parent){
    parent.get("/assets").handler(context -> {

      final JsonArray response = new JsonArray();
      ASSETS.stream().map(Asset::new).forEach(response::add);

      log.info("Path {} responds with {}", context.normalizedPath(), response.encode());
      context.response().end(response.toBuffer());
    });
  }
}
