package edu.swe2.cs.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cache<T, S> {

    private Map<T, S> data;

    /**
     * Creates an empty Cache
     */
    public Cache() {
        data = new HashMap<T, S>();
    }

    /**
     * Set cache data
     *
     * @param data Map containing the data
     */
    public void setData(Map<T, S> data) {
        this.data = data;
    }

    /**
     * Add data to cache
     *
     * @param key Key with which the specified value is associated
     * @param value Value to be associated with the specified key
     */
    public void addData(T key, S value) {
        if (data != null) {
            data.put(key, value);
        }
    }

    /**
     * Update data in cache
     *
     * @param key Key with which the specified value is associated
     * @param value Value to be associated with the specified key
     */
    public void updateData(T key, S value) {
        if (data != null) {
            if (data.containsKey(key)) {
                data.replace(key, value);
            }
        }
    }

    /**
     * Remove data from cache
     *
     * @param key Key with which the specified value is associated
     */
    public void deleteData(T key) {
        if (data != null) {
            if (data.containsKey(key)) {
                data.remove(key);
            }
        }
    }

    /**
     * Get all cache data
     *
     * @return A list of all values associated with keys in cache
     */
    public List<S> getALLData() {
        if (data != null) {
            return new ArrayList<S>(data.values());
        } else return null;
    }

    /**
     * Get cache data
     *
     * @param key Key with which the specified value is associated
     * @return Value associated with specified key
     */
    public S getData(T key) {
        if (data != null) {
            if (data.containsKey(key)) {
                return (S) data.get(key);
            }
        }
        return null;
    }
}

