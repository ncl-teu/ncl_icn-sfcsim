package net.gripps.ccn.core;

import java.util.HashMap;

/**
 * Created by kanemih on 2018/11/02.
 */
public class CS {

    /**
     * キャッシュテーブル
     */
    private HashMap<String, CCNContents> cacheMap;

    public CS() {
        this.cacheMap = new HashMap<String, CCNContents>();
    }

    public CS(HashMap<String, CCNContents> cacheMap) {
        this.cacheMap = cacheMap;
    }

    public HashMap<String, CCNContents> getCacheMap() {
        return cacheMap;
    }

    public void setCacheMap(HashMap<String, CCNContents> cacheMap) {
        this.cacheMap = cacheMap;
    }
}
