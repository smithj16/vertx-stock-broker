package com.jacob.vertx_stock_broker.Verticles.RestAPIs.WatchList;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class WatchListRestApi {

  private static final Logger log = LoggerFactory.getLogger(WatchListRestApi.class);
  public static void attach(final Router parent) {

    final HashMap<UUID, WatchList> watchListPerAccount = new HashMap<UUID, WatchList>();

    final String path = "/account/watchlist/:accountId";

    parent.get(path).handler(routingContext -> {
      String accountId = getAccountId(routingContext);
      Optional<WatchList> watchList = Optional.ofNullable(watchListPerAccount.get(UUID.fromString(accountId)));
      if(watchList.isEmpty()){
        routingContext.response().setStatusCode(HttpResponseStatus.NOT_FOUND.code())
          .end(new JsonObject()
            .put("message", "watchlist for account " + accountId + " not available.")
            .put("path" , routingContext.normalizedPath())
            .toBuffer())
        ;
          return;
      }

      routingContext.response().end(watchList.get().toJsonObject().toBuffer());
    });

    parent.put(path).handler(routingContext -> {
      String accountId = getAccountId(routingContext);
      JsonObject json = routingContext.getBodyAsJson();
      WatchList watchList = json.mapTo(WatchList.class);
      watchListPerAccount.put(UUID.fromString(accountId), watchList);
      routingContext.response().end(json.toBuffer());

    });

    parent.delete(path).handler(routingContext -> {
       String accountID = getAccountId(routingContext);
       WatchList deleted = watchListPerAccount.remove(UUID.fromString(accountID));
       log.info("Deleted:{}, remaining: {}", deleted, watchListPerAccount.values());
       routingContext.response().end(deleted.toJsonObject().toBuffer());
    });

  }

  private static String getAccountId(RoutingContext routingContext) {
    String accountId = routingContext.pathParam("accountId");
    log.debug("{} for account {}", routingContext.normalizedPath(), accountId);
    return accountId;
  }
}
