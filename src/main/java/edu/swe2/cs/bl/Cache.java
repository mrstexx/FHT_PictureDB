package edu.swe2.cs.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cache<T, S> {

    private Map<T, S> data;

    public Cache() {
        data = new HashMap<T, S>();
    }

    public void setData(Map<T, S> data) {
        this.data = data;
    }

    public void addData(T key, S value) {
        if (data != null) {
            data.put(key, value);
        }
    }

    public void updateData(T key, S value) {
        if (data != null) {
            if (data.containsKey(key)) {
                data.replace(key, value);
            }
        }
    }

    public void deleteData(T key) {
        if (data != null) {
            if (data.containsKey(key)) {
                data.remove(key);
            }
        }
    }

    public List<S> getALLData() {
        if (data != null) {
            return new ArrayList<S>(data.values());
        } else return null;
    }

    public S getData(T key) {
        if (data != null) {
            if (data.containsKey(key)) {
                return (S) data.get(key);
            }
        }
        return null;
    }
}

