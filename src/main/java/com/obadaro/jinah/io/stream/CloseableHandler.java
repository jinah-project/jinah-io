/* 
 * JINAH Project - Java Is Not A Hammer
 * http://obadaro.com/jinah
 * 
 * Copyright (C) 2010-2012 Roberto Badaro 
 * and individual contributors by the @authors tag.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.obadaro.jinah.io.stream;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles streams, preventing leave a stream opened accidentally.
 * 
 * @author Roberto Badaro
 */
public class CloseableHandler {

    private final HashSet<Closeable> streams = new HashSet<Closeable>();

    /**
     * @param <T>
     * @param stream
     * @return
     */
    public <T extends Closeable> T add(final T stream) {
        if (stream == null) {
            throw new IllegalArgumentException("stream can't be null.");
        }

        streams.add(stream);
        return stream;
    }

    /**
     * @param <T>
     * @param stream
     */
    public <T extends Closeable> void close(final T stream) {
        if (stream != null) {
            streams.remove(stream);
            try {
                stream.close();
            } catch (final IOException e) {
                // NOOP
            }
        }
    }

    public <T extends Closeable> void close(final T stream, final boolean threwException) {

    }

    /**
     * 
     */
    public void closeAll() {
        for (final Closeable stream : streams) {
            if (stream != null) {
                try {
                    stream.close();
                } catch (final IOException e) {
                    // NOOP
                }
            }
        }

        streams.clear();
    }

    @Override
    protected void finalize() throws Throwable {
        if (!streams.isEmpty()) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.WARNING,
                    "Cleaning your garbage. You've forgot to execute 'CloseableHandler.closeAll()'.");
            closeAll();
        }

        super.finalize();
    }

}
