package com.jacob.vertx_stock_broker.Verticles.RestAPIs.WatchList;

import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.UUID;

public class WatchListRestApi {

  private static final Logger log = LoggerFactory.getLogger(WatchListRestApi.class);
  public static void attach(final Router parent) {

    final HashMap<UUID, WatchList> watchListPerAccount = new HashMap<UUID, WatchList>();

    final String path = "/account/watchlist/:accountId";
    parent.get(path).handler(routingContext -> {
      String accountId = routingContext.pathParam("accountId");
      log.debug("{} for account {}", routingContext.normalizedPath(), accountId);
      WatchList watchList = watchListPerAccount.get(UUID.fromString(accountId));
      routingContext.response().end(watchList.toJsonObject().toBuffer());
    });

    parent.put(path).handler(routingContext -> {

    });

    parent.delete(path).handler(routingContext -> {

    });

  }
}
