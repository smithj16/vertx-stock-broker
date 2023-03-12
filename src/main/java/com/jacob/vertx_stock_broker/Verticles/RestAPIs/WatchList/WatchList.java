package com.jacob.vertx_stock_broker.Verticles.RestAPIs.WatchList;

import com.jacob.vertx_stock_broker.Verticles.RestAPIs.Assets.Asset;
import io.vertx.core.json.JsonObject;
import lombok.Value;

import java.util.List;

@Value
public class WatchList {
  List<Asset> assets;

  JsonObject toJsonObject(){
    return JsonObject.mapFrom(this);
  }
}
