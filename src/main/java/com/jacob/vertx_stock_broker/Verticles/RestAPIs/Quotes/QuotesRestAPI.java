package com.jacob.vertx_stock_broker.Verticles.RestAPIs.Quotes;

import com.jacob.vertx_stock_broker.Verticles.RestAPIs.Assets.Asset;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

public class QuotesRestAPI {

  private static final Logger log = LoggerFactory.getLogger(QuotesRestAPI.class);

  public static void attach(Router parent){

    parent.get("/quotes/:asset").handler(context -> {

      final String assetParam = context.pathParam("asset");

      log.debug("Asset param: {}", assetParam);

      final Quote quote = initRandomQuote(assetParam);

      final JsonObject response = quote.toJsonObject();

      log.info("Path {} responds with {}", context.normalizedPath(), response.encode());
      context.response().end(response.toBuffer());

    });

  }

  private static Quote initRandomQuote(String assetParam) {
    return Quote.builder()
      .asset(new Asset(assetParam))
      .volume(randomValue())
      .ask(randomValue())
      .bid(randomValue())
      .lastPrice(randomValue())
      .build();
  }

  private static BigDecimal randomValue() {
    return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1,100));
  }
}
