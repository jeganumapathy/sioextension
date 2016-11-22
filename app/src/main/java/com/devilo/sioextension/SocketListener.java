package com.devilo.sioextension;

import io.socket.emitter.Emitter;

/**
 */
public interface SocketListener extends Emitter.Listener {

    void connect();

    void disconnect();


}
