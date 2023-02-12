package com.jacob.vertx_stock_broker.Verticles.RestAPIs.Assets;

public class Asset {
  private final String name;

  public Asset(final String name){
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
