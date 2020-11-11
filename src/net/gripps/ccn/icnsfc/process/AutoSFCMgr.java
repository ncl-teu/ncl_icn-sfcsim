package net.gripps.ccn.icnsfc.process;

import net.gripps.ccn.CCNUtil;
import net.gripps.ccn.icnsfc.AutoUtil;
import net.gripps.ccn.icnsfc.core.AutoEnvironment;
import net.gripps.cloud.CloudUtil;
import net.gripps.cloud.core.Cloud;
import net.gripps.cloud.core.ComputeHost;
import net.gripps.cloud.core.VCPU;
import net.gripps.cloud.nfv.NFVUtil;
import net.gripps.cloud.nfv.sfc.SFC;
import net.gripps.cloud.nfv.sfc.SFCGenerator;
import net.gripps.cloud.nfv.sfc.VNF;
import net.named_data.jndn.Name;
import org.ncl.workflow.ccn.sfc.process.NFDTask;
import org.ncl.workflow.util.NCLWUtil;

import java.io.Serializable;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by Hidehiro Kanemitsu on 2020/09/16
 */
public class AutoSFCMgr implements Serializable {

    private static AutoSFCMgr own;

    private long SFCNum;

    private LinkedBlockingDeque<SFC> sfcQueue;

    private AutoEnvironment env;

    public static AutoSFCMgr getIns(){
        if(AutoSFCMgr.own == null){
            AutoSFCMgr.own = new AutoSFCMgr();
        }

        return AutoSFCMgr.own;
    }

    private AutoSFCMgr(){
        this.SFCNum = 0;
        this.sfcQueue = new LinkedBlockingDeque<SFC>();

    }

    public AutoEnvironment getEnv() {
        return env;
    }

    public void setEnv(AutoEnvironment env) {
        this.env = env;
    }

    /**
     * 新規にSFCを生成する．
     * @return
     */
    public SFC createNewSFC(){
        //SFC sfc = SFCGenerator.getIns().multipleSFCProcess();
        NFVUtil.sfc_vnf_num = CCNUtil.genLong(AutoUtil.sfc_vnf_num_min, AutoUtil.sfc_vnf_num_max);
        SFC sfc = SFCGenerator.getIns().autoSFCProcess();

        //IDを付与する．
        this.SFCNum ++;
        sfc.setSfcID(this.SFCNum);


        //あとは，各タスクのIDを変更する．
        Iterator<VNF> vIte = sfc.getVnfMap().values().iterator();
        while(vIte.hasNext()){
            VNF vnf = vIte.next();
            vnf.getIDVector().set(0, new Long(this.SFCNum));
        }
        this.sfcQueue.add(sfc);
        //System.out.println("Num:"+sfc.getVnfMap().size());
        return sfc;

    }

    /**
     * 既存SFCのコピーを生成する．
     * IDも同一．
     * @param orgSFC
     * @return
     */
    public SFC replicateSFC(SFC orgSFC){
        SFC newSFC = (SFC)orgSFC.deepCopy();
        this.sfcQueue.add(newSFC);
        this.SFCNum++;
        return newSFC;
    }

    /**
     * Prefix生成する処理になります．
     * /JobID/fromTaskのID/toTaskのID/fromTaskのCmd
     *
     * @param
     * @return
     */
    public String  createPrefix(VNF  fromTask, VNF toTask) {
        long toID = -1;
        if (toTask != null) {
            toID = toTask.getIDVector().get(1);
        }
        Long jobID = fromTask.getIDVector().get(0);
        String val = "/"+fromTask.getIDVector().get(0) + "/" + fromTask.getIDVector().get(1) + "/" + toID + "/";
        val = val.replaceAll(" ", "");
        return val;
    }

    public String  createEndPrefix(VNF endTask) {
        String toID = "-1";

        Long jobID = endTask.getIDVector().get(0);
        String val = "/"+endTask.getIDVector().get(0) + "/" + endTask.getIDVector().get(1) + "/" + toID + "/";
        val = val.replaceAll(" ", "");
        return val;
    }



    public double calcExecTime(long w, VCPU vcpu){
        return CloudUtil.getRoundedValue((double) w / (double) vcpu.getMips());


    }


    public double calcComTime(long dataSize, VCPU fromVCPU, VCPU toVCPU) {
        //DCの情報．
        Long fromDCID = CloudUtil.getInstance().getDCID(fromVCPU.getPrefix());
        Long toDCID = CloudUtil.getInstance().getDCID(toVCPU.getPrefix());
        long dcBW = NFVUtil.MAXValue;
        Cloud fromCloud = env.getDcMap().get(fromDCID);
        Cloud toCloud = env.getDcMap().get(toDCID);

        //同一クラウド内であれば，DC間の通信は考慮しなくて良い．
        if (fromDCID.longValue() == toDCID.longValue()) {
        } else {
            //DCが異なれば，DC間の通信も考慮スべき．
            dcBW = Math.min(fromCloud.getBw(), toCloud.getBw());

        }
        Long fromHostID = CloudUtil.getInstance().getHostID(fromVCPU.getPrefix());
        Long toHostID = CloudUtil.getInstance().getHostID(toVCPU.getPrefix());

        //後は，ホスト間での通信
        ComputeHost fromHost = fromCloud.getComputeHostMap().get(fromHostID);
        ComputeHost toHost = toCloud.getComputeHostMap().get(toHostID);
        long hostBW = NFVUtil.MAXValue;
        if (fromHost.getDcID() == toHost.getMachineID()) {
            //同一ホストなら，0を返す．
            return 0;
        } else {
            hostBW = Math.min(fromHost.getBw(), toHost.getBw());
        }

        long realBW = Math.min(dcBW, hostBW);

        double time = CloudUtil.getRoundedValue((double) dataSize / (double) realBW);

        return time;

    }

    public long getJobID(String prefix){
        StringTokenizer st = new StringTokenizer(prefix, "/");
        long val = -1;
        String str = null;
        for(int i=0;i<1;i++){
            str = st.nextToken();

        }

        return Long.valueOf(str).longValue();
    }


    public long getPredVNFID(String prefix){
        StringTokenizer st = new StringTokenizer(prefix, "/");
        long val = -1;
        String str = null;
        for(int i=0;i<2;i++){
            str = st.nextToken();

        }

        return Long.valueOf(str).longValue();
    }

    public long getSucVNFID(String prefix){
        StringTokenizer st = new StringTokenizer(prefix, "/");
        long val = -1;
        String str = null;
        for(int i=0;i<3;i++){
            str = st.nextToken();

        }

        return Long.valueOf(str).longValue();
    }

    public long getSFCNum() {
        return SFCNum;
    }

    public void setSFCNum(long SFCNum) {
        this.SFCNum = SFCNum;
    }

    public LinkedBlockingDeque<SFC> getSfcQueue() {
        return sfcQueue;
    }

    public void setSfcQueue(LinkedBlockingDeque<SFC> sfcQueue) {
        this.sfcQueue = sfcQueue;
    }
}
