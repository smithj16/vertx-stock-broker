package com.jacob.vertx_stock_broker.Verticles.RestAPIs.Assets;

import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AssetRestAPI {

  private static final Logger log = LoggerFactory.getLogger(AssetRestAPI.class);

  public static void attach(Router parent){
    parent.get("/assets").handler(context -> {

      final JsonArray response = new JsonArray();
      response.add(new Asset("AAPL"));
      response.add(new Asset("AMZN"));
      response.add(new Asset("NFLX"));
      response.add(new Asset("TSLA"));

      log.info("Path {} responds with {}", context.normalizedPath(), response.encode());
      context.response().end(response.toBuffer());
    });
  }
}
