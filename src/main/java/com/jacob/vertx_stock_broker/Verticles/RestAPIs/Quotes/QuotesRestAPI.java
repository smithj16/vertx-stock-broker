package com.jacob.vertx_stock_broker.Verticles.RestAPIs.Quotes;

import com.jacob.vertx_stock_broker.Verticles.RestAPIs.Assets.Asset;
import com.jacob.vertx_stock_broker.Verticles.RestAPIs.Assets.AssetRestAPI;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class QuotesRestAPI {

  private static final Logger log = LoggerFactory.getLogger(QuotesRestAPI.class);

  public static void attach(Router parent){
    final Map<String, Quote> cachedQuotes = new HashMap<>();

    AssetRestAPI.ASSETS.forEach(symbol ->
      cachedQuotes.put(symbol, initRandomQuote(symbol)));

    //asset is a path parameter
    parent.get("/quotes/:asset").handler(context -> {

      final String assetParam = context.pathParam("asset");

      log.debug("Asset param: {}", assetParam);

      Optional<Quote> maybeQuote = Optional.ofNullable(cachedQuotes.get(assetParam));

      if(maybeQuote.isEmpty()) {

        context.response()
          .setStatusCode(HttpResponseStatus.NOT_FOUND.code())
          .end(new JsonObject()
            .put("message", "quote for asset " + assetParam + " not available!")
            .put("path", context.normalizedPath()).toBuffer());

        return;
      }


      final JsonObject response = maybeQuote.get().toJsonObject();

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
