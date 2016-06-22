/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.wsockauth;

import java.io.IOException;
import java.io.Writer;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.json.spi.JsonProvider;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 *
 * @author alex
 */
public class WsockEncoder implements Encoder.TextStream<Messaage> {

  @Override
  public void encode(Messaage msgMsg, Writer writer) throws EncodeException, IOException {
    JsonProvider provider = JsonProvider.provider();
    JsonObject jsonMessage = provider.createObjectBuilder()
            .add("type", msgMsg.getType())
            .add("sequence_id", msgMsg.getSequenceId())
            .add("data", msgMsg.getData())
            .build();
    try (JsonWriter jsonWriter = provider.createWriter(writer)) {
      jsonWriter.write(jsonMessage);
    }
  }

  @Override
  public void init(EndpointConfig ec) {
  }

  @Override
  public void destroy() {
  }
}