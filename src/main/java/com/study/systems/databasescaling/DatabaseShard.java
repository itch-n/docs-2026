package com.study.systems.databasescaling;

import java.util.*;

/** Represents a single database shard. */
public class DatabaseShard {
    int shardId;
    Map<String, String> data;

    public DatabaseShard(int shardId) {
        this.shardId = shardId;
        this.data = new HashMap<>();
    }

    public void insert(String key, String value) {
        data.put(key, value);
    }

    public String get(String key) {
        return data.get(key);
    }

    public void delete(String key) {
        data.remove(key);
    }

    public int getRecordCount() {
        return data.size();
    }
}
