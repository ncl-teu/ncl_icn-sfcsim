package net.gripps.ccn.icnsfc;

import net.gripps.ccn.CCNUtil;
import net.gripps.cloud.CloudUtil;
import org.apache.commons.math.random.RandomDataImpl;

import java.io.FileInputStream;
import java.io.Serializable;
import java.util.Properties;

public class AutoUtil  implements Serializable {


    protected static AutoUtil own;

    /**
     * 生成するSFCの総数
     */
    public static int ccn_sfc_totalnum;

    /**
     * 重複するSFCの種類の数
     */
    public static int ccn_sfc_typenum;

    public static String SFC_NAME="app_sfc";

   // public static String VNF_FROMID="vnf_fromid";

    //public static String VNF_TOID = "vnf_toid";


    public static long sfc_vnf_num_min;

    public static long sfc_vnf_num_max;

    public static int ccn_sfc_mode;

    public static int sched_altorithm;

    //predVNF ordering
    /**
     * 0: Normal (random order)
     * 1: Workload
     * 2: Blevel
     */
    public static int predvnf_ordering_mode;

    //FNJ問題への対処1; Active/Passive check
    /**
     * 0: Active
     * 1: Passive
     * 2: Hybrid
     * Else: No check
     */
    public static int fnj_checkmode;

    public static String FNJ_ACTIVE = "FNJ_ACTIVE";

    public static String FNJ_PASSIVE = "FNJ_PASSIVE";

    public static String FNJ_HYBRID = "FNJ_HYBRID";

    public static Long fnj_passive_duration;

    //FNJ問題への対処2; Interest sending in one-stroke
    /**
     * 0: Normal (multicast)
     * 1: One-stroke (unicast)
     * Else: Normal (multicast)
     */
    public static int interest_sending_mode;

    public static int interest_duplicate_mode;

    /**
     * 0: Normal (random)
     * 1: Blevel
     * 2: SPR (Successor to Predecessor Ratio)
     * Else: Normal (multicast)
     */
    public static int vnf_prioritize_mode;

    public static Long sched_scheduling_per_delay_min;
    public static Long sched_scheduling_per_delay_max;
    public static Long sched_dispatch_per_delay_min;
    public static Long sched_dispatch_per_delay_max;



    /**
     * 設定情報プロパティ
     */
    public static Properties prop;

    public static AutoUtil getIns(){
        if(AutoUtil.own == null){
            AutoUtil.own = new AutoUtil();
        }
        return AutoUtil.own;
    }

    private AutoUtil() {

    }

    public void initialize(String fileName) {

        try{
            AutoUtil.prop = new Properties();
            AutoUtil.prop.load(new FileInputStream(fileName));
            AutoUtil.ccn_sfc_totalnum = Integer.valueOf(AutoUtil.prop.getProperty("ccn_sfc_totalnum"));
            AutoUtil.ccn_sfc_typenum = Integer.valueOf(AutoUtil.prop.getProperty("ccn_sfc_typenum"));
            AutoUtil.sfc_vnf_num_min = Long.valueOf(AutoUtil.prop.getProperty("sfc_vnf_num_min"));
            AutoUtil.sfc_vnf_num_max= Long.valueOf(AutoUtil.prop.getProperty("sfc_vnf_num_max"));
            AutoUtil.ccn_sfc_mode = Integer.valueOf(AutoUtil.prop.getProperty("ccn_sfc_mode"));
            AutoUtil.sched_altorithm = Integer.valueOf(AutoUtil.prop.getProperty("sched_algorithm"));
            AutoUtil.fnj_checkmode = Integer.valueOf(AutoUtil.prop.getProperty("fnj_checkmode"));
            AutoUtil.fnj_passive_duration = Long.valueOf(AutoUtil.prop.getProperty("fnj_passive_duration"));
            // Delay simulation
            AutoUtil.sched_scheduling_per_delay_min = Long.valueOf(AutoUtil.prop.getProperty("sched_scheduling_per_delay_min"));
            AutoUtil.sched_scheduling_per_delay_max = Long.valueOf(AutoUtil.prop.getProperty("sched_scheduling_per_delay_max"));
            AutoUtil.sched_dispatch_per_delay_min = Long.valueOf(AutoUtil.prop.getProperty("sched_dispatch_per_delay_min"));
            AutoUtil.sched_dispatch_per_delay_max = Long.valueOf(AutoUtil.prop.getProperty("sched_dispatch_per_delay_max"));
            // Interest sending in one-stroke
            AutoUtil.interest_sending_mode = Integer.parseInt(AutoUtil.prop.getProperty("ccn_interests_sending_mode"));
            AutoUtil.vnf_prioritize_mode = Integer.parseInt(AutoUtil.prop.getProperty("sfc_vnf_prioritize_mode"));
            // Interest duplicate mode
            AutoUtil.interest_duplicate_mode = Integer.parseInt(AutoUtil.prop.getProperty("ccn_interests_duplicate_mode"));
            // predVNF ordering
            AutoUtil.predvnf_ordering_mode = Integer.valueOf(AutoUtil.prop.getProperty("sfc_predvnf_ordering_mode"));





        }catch(Exception e){
            e.printStackTrace();
        }
    }


}
