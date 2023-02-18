package com.jacob.vertx_stock_broker.Verticles.RestAPIs.Quotes;

import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuotesRestAPI {

  private static final Logger log = LoggerFactory.getLogger(QuotesRestAPI.class);

  public static void attach(Router parent){
    parent.get("/quotes/:asset").handler(context -> {
      final String assetParam = context.pathParam("asset");
      log.info("Asset param: {}", assetParam);
    });
  }
}
