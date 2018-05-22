package com.hankcs.dic;

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

    private long lastModified;

    public DictionaryFile(String path, long lastModified) {
        this.path = path;
        this.lastModified = lastModified;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
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
        return lastModified == that.lastModified && Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, lastModified);
    }

    @Override
    public String toString() {
        return "DictionaryFile{" +
                "path='" + path + '\'' +
                ", lastModified=" + lastModified +
                '}';
    }
}
