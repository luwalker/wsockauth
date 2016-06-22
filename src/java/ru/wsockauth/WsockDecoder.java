/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.wsockauth;

/**
 *
 * @author alex
 */
import java.io.IOException;
import java.io.Reader;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.spi.JsonProvider;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class WsockDecoder implements Decoder.TextStream<Messaage> {

    @Override
    public Messaage decode(Reader reader) throws DecodeException, IOException {
        JsonProvider provider = JsonProvider.provider();
        JsonReader jsonReader = provider.createReader(reader);
        JsonObject jsonMessage = jsonReader.readObject();

        Messaage message = new Messaage();
        message.setType(jsonMessage.getString("type"));
        message.setSequenceId(jsonMessage.getString("sequence_id"));
        message.setData(jsonMessage.getString("data"));
        message.parseData();
        return message;
    }

    @Override
    public void init(EndpointConfig ec) {
    }

    @Override
    public void destroy() {
    }
}
