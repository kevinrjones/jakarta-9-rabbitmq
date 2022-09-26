package com.knowledgespike;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface LocalChannel {
    void basicPublish(String exchange, String queue, Object properties, byte[] message) throws IOException, TimeoutException;
}
