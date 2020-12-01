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



        }catch(Exception e){
            e.printStackTrace();
        }
    }


}
