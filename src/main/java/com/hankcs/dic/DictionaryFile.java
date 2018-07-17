package com.hankcs.dic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * User: Kenn
 * Date: 2018/2/8
 * Time: 17:12
 * Project: elasticsearch-analysis-hanlp
 * Description:
 */
public class DictionaryFile {

    private String path;

    private String type;

    private long lastModified;

    public DictionaryFile() {
    }

    public DictionaryFile(String path, long lastModified) {
        this.path = path;
        this.lastModified = lastModified;
    }

    public DictionaryFile(String path, String type, long lastModified) {
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
            byte[] bytes = path.getBytes("utf-8");
            out.writeInt(bytes.length);
            out.write(bytes);
        } else {
            out.writeInt(0);
        }
        if (type != null && type.length() != 0) {
            byte[] bytes = type.getBytes("utf-8");
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
            path = new String(bytes, "utf-8");
        }

        int typeLength = in.readInt();
        if (typeLength != 0) {
            byte[] bytes = new byte[typeLength];
            in.read(bytes);
            type = new String(bytes, "utf-8");
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
