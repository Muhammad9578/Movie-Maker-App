package com.creativetechnologies.slideshows.videos.songs.videomaker.libffmpeg.utils;

import java.io.IOException;
import java.io.OutputStream;

public class StubOutputStream extends OutputStream {
    private boolean closed = false;

    public void write(int oneByte) throws IOException {
    }

    public void close() throws IOException {
        super.close();
        this.closed = true;
    }

    public boolean isClosed() {
        return this.closed;
    }
}
