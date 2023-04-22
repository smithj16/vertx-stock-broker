package com.jacob.vertx_stock_broker.Verticles.RestAPIs.WatchList;

import com.jacob.vertx_stock_broker.Verticles.RestAPIs.Assets.Asset;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WatchList {
  List<Asset> assets;

   public JsonObject toJsonObject(){
    return JsonObject.mapFrom(this);
  }
}
