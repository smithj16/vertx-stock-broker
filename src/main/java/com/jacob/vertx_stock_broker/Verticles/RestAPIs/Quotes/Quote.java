package com.jacob.vertx_stock_broker.Verticles.RestAPIs.Quotes;

import com.jacob.vertx_stock_broker.Verticles.RestAPIs.Assets.Asset;
import io.vertx.core.json.JsonObject;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class Quote {

  Asset asset;
  BigDecimal bid;
  BigDecimal ask;

  BigDecimal lastPrice;

  BigDecimal volume;

  public JsonObject toJsonObject(){
    return JsonObject.mapFrom(this);
  }
}
