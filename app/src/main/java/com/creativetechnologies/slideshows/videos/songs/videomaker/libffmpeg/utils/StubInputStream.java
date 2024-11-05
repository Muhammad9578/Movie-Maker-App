package com.creativetechnologies.slideshows.videos.songs.videomaker.libffmpeg.utils;

import java.io.IOException;
import java.io.InputStream;

public class StubInputStream extends InputStream {
    private boolean closed = false;

    public int read() throws IOException {
        return 0;
    }

    public void close() throws IOException {
        super.close();
        this.closed = true;
    }

    public boolean isClosed() {
        return this.closed;
    }
}
