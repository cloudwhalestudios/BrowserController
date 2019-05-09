package com.cloudwhale.browsercontroller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;
import java.util.stream.Stream;

// refer to https://www.baeldung.com/run-shell-command-in-java
public class StreamGobbler implements Runnable{
    private InputStream inputStream;
    private Consumer<String> consumer;

    public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
        this.inputStream = inputStream;
        this.consumer = consumer;
    }

    @Override
    public void run() {
        new BufferedReader(new InputStreamReader(inputStream)).lines()
                .forEach(consumer);
    }

}
