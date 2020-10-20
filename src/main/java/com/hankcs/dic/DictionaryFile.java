package com.hankcs.dic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Project: elasticsearch-analysis-hanlp
 * Description: 自定义词典文件信息
 * Author: Kenn
 * Create: 2018-12-14 15:10
 */
public class DictionaryFile {

    private String path;

    private String type;

    private long lastModified;

    public DictionaryFile() {
    }

    DictionaryFile(String path, long lastModified) {
        this.path = path;
        this.lastModified = lastModified;
    }

    DictionaryFile(String path, String type, long lastModified) {
        this(path, lastModified);
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public void write(DataOutputStream out) throws IOException {
        if (path != null && path.length() != 0) {
            byte[] bytes = path.getBytes(StandardCharsets.UTF_8);
            out.writeInt(bytes.length);
            out.write(bytes);
        } else {
            out.writeInt(0);
        }
        if (type != null && type.length() != 0) {
            byte[] bytes = type.getBytes(StandardCharsets.UTF_8);
            out.writeInt(bytes.length);
            out.write(bytes);
        } else {
            out.writeInt(0);
        }
        out.writeLong(lastModified);
    }

    public void read(DataInputStream in) throws IOException {
        int pathLength = in.readInt();
        if (pathLength != 0) {
            byte[] bytes = new byte[pathLength];
            in.read(bytes);
            path = new String(bytes, StandardCharsets.UTF_8);
        }

        int typeLength = in.readInt();
        if (typeLength != 0) {
            byte[] bytes = new byte[typeLength];
            in.read(bytes);
            type = new String(bytes, StandardCharsets.UTF_8);
        }
        lastModified = in.readLong();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DictionaryFile that = (DictionaryFile) o;
        return lastModified == that.lastModified &&
                Objects.equals(path, that.path) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, type, lastModified);
    }

    @Override
    public String toString() {
        return "DictionaryFile{" +
                "path='" + path + '\'' +
                ", lastModified=" + lastModified +
                '}';
    }
}
