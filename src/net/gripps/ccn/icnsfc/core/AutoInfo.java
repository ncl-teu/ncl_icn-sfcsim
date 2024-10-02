package net.gripps.ccn.icnsfc.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

public class AutoInfo implements Serializable {

    /**
     * アプリID^SFCID
     */
    private String aplJobID;

    /**
     * SFCを基準コンピュータに割り当てた場合の
     * CCR値．
     */
    private double CCR;

    /**
     * Interstパケット送信開始時刻
     */
    private long startTime;

    /**
     * ノードに結果が来たときの時刻
     */
    private long finishTime;

    /**
     * finish - start
     */
    private long makeSpan;

    /**
     * SFCにおける，SFの数
     */
    private long sfNum;

    /**
     * アプリケーションが要求された時刻(CCNNodeが要求した時刻)
     */
    private long startAppRequestTime;

    /**
     * アプリケーションのTurnaround Time
     */
    private long turnaroundTime;


    /**
     * アプリケーションの実行開始時刻(つまり，StartTaskの実行開始時刻)
     */
    private long startAppExecTime;

    /**
     * アプリケーションの実行完了時刻(つまり，EndTaskの実行完了時刻)
     */
    private long finishAppExecTime;

    /**
     * アプリケーションの実行時間
     */
    private long appExecTime;

    /**
     * アプリケーションのトータルホップ数(Interestのホップ数の合計)
     */
    private int appHopNum;

    /**
     * SFCにおける，各SFが実行された（割り当てられた）回数
     */
    private HashMap<Long, Integer> sfAllocNum;



    /**
     * 使われたSFインスタンス数
     * Type^VMIDの集合
     */
    private HashSet<String> sfInsSet;

    /**
     * キャッシュヒット数
     */
    private long cacheHitNum;

    /**
     * vCPU数
     */
    private HashSet<String> vCPUSet;

    /**
     * ホスト集合
     */
    private HashSet<String> hostSet;

    /**
     * VM数
     */
    private HashSet<String> vmSet;

    public AutoInfo(String aplJobID) {
        this.aplJobID = aplJobID;
        this.cacheHitNum = 0;
        this.vCPUSet = new HashSet<String>();
        this.vmSet = new HashSet<String>();
        this.sfInsSet = new HashSet<String>();
        this.hostSet = new HashSet<String>();
        this.sfNum = 0;

    }

    public HashSet<String> getHostSet() {
        return hostSet;
    }

    public void setHostSet(HashSet<String> hostSet) {
        this.hostSet = hostSet;
    }

    public String getAplJobID() {
        return aplJobID;
    }

    public void setAplJobID(String aplJobID) {
        this.aplJobID = aplJobID;
    }

    public double getCCR() {
        return CCR;
    }

    public void setCCR(double CCR) {
        this.CCR = CCR;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    public long getMakeSpan() {
        return makeSpan;
    }

    public void setMakeSpan(long makeSpan) {
        this.makeSpan = makeSpan;
    }

    public long getSfNum() {
        return sfNum;
    }

    public void setSfNum(long sfNum) {
        this.sfNum = sfNum;
    }


    public long getStartAppRequestTime() {
        return startAppRequestTime;
    }

    public void setStartAppRequestTime(long startAppRequestTime) {
        this.startAppRequestTime = startAppRequestTime;
    }

    public long getTurnaroundTime() {
        return turnaroundTime;
    }

    public void setTurnaroundTime(long turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public long getStartAppExecTime() {
        return startAppExecTime;
    };

    public void setStartAppExecTime(long startAppExecTime) {
        this.startAppExecTime = startAppExecTime;
    }

    public long getFinishAppExecTime() {
        return finishAppExecTime;
    }

    public void setFinishAppExecTime(long finishAppExecTime) {
        this.finishAppExecTime = finishAppExecTime;
    }

    public void setAppExecTime(long appExecTime) {
        this.appExecTime = appExecTime;
    }

    public long getAppExecTime() {
        return appExecTime;
    }

    public int getAppHopNum() {
        return appHopNum;
    }

    public void setAppHopNum(int totalHopNum) {
        this.appHopNum = totalHopNum;
    }

    public HashMap<Long, Integer> getSfAllocNum() {
        return sfAllocNum;
    }

    public void setSfAllocNum(HashMap<Long, Integer> sfExecNum) {
        this.sfAllocNum = sfExecNum;
    }


    public HashSet<String> getSfInsSet() {
        return sfInsSet;
    }

    public void setSfInsSet(HashSet<String> sfInsSet) {
        this.sfInsSet = sfInsSet;
    }

    public long getCacheHitNum() {
        return cacheHitNum;
    }

    public void setCacheHitNum(long cacheHitNum) {
        this.cacheHitNum = cacheHitNum;
    }

    public HashSet<String> getvCPUSet() {
        return vCPUSet;
    }

    public void setvCPUSet(HashSet<String> vCPUSet) {
        this.vCPUSet = vCPUSet;
    }

    public HashSet<String> getVmSet() {
        return vmSet;
    }

    public void setVmSet(HashSet<String> vmSet) {
        this.vmSet = vmSet;
    }
}

