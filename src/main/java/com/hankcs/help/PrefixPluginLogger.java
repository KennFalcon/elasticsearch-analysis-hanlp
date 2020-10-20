package com.hankcs.help;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.log4j.spi.ExtendedLoggerWrapper;

import java.util.WeakHashMap;

/**
 * Project: elasticsearch-analysis-hanlp
 * Description: 日志
 * Author: Kenn
 * Create: 2019-02-14 15:10
 */
public class PrefixPluginLogger extends ExtendedLoggerWrapper {

    private static final WeakHashMap<String, Marker> MARKERS = new WeakHashMap<>();

    private final Marker marker;

    static int markersSize() {
        return MARKERS.size();
    }

    public String prefix() {
        return this.marker.getName();
    }

    PrefixPluginLogger(ExtendedLogger logger, String name, String prefix) {
        super(logger, name, null);
        String actualPrefix = prefix == null ? "" : prefix;
        MarkerManager.Log4jMarker actualMarker;
        synchronized (MARKERS) {
            MarkerManager.Log4jMarker maybeMarker = (MarkerManager.Log4jMarker)MARKERS.get(actualPrefix);
            if (maybeMarker == null) {
                actualMarker = new MarkerManager.Log4jMarker(actualPrefix);
                MARKERS.put(actualPrefix, actualMarker);
            } else {
                actualMarker = maybeMarker;
            }
        }
        this.marker = actualMarker;
    }

    @Override
    public void logMessage(String fqcn, Level level, Marker marker, Message message, Throwable t) {
        assert marker == null;
        super.logMessage(fqcn, level, this.marker, message, t);
    }
}