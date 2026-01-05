package net.gripps.ccn.icnsfc.routing;

import net.gripps.ccn.CCNUtil;
import net.gripps.ccn.core.CCNRouter;
import net.gripps.ccn.core.ForwardHistory;
import net.gripps.ccn.core.InterestPacket;
import net.gripps.ccn.icnsfc.AutoUtil;
import net.gripps.ccn.icnsfc.process.AutoSFCMgr;
import net.gripps.cloud.core.VCPU;
import net.gripps.cloud.nfv.sfc.SFC;
import net.gripps.cloud.nfv.sfc.VNF;

import java.util.Iterator;

import net.gripps.ccn.core.CCNNode;
import java.util.HashMap;

/**
 * 分散処理をせず，常に１ノードで処理を行うためのルーティングアルゴリズム (ルーティングをしない) .
 * 常に呼び出し元ルータのローカルVCPUを返す．
 */
public class NoRouting extends AutoRouting {

    public NoRouting() {
        super();
    }

    public NoRouting(HashMap<Long, CCNNode> nodeMap, HashMap<Long, CCNRouter> routerMap) {
        super(nodeMap, routerMap);
    }

    @Override
    public String findNextRouter(InterestPacket p, CCNRouter r) {
        // AutoRoutingとの互換性のため，一旦同じように処理を行う．
        // InterestからのSFC
        String prefix = p.getPrefix();
        SFC sfc_int = (SFC)p.getAppParams().get(AutoUtil.SFC_NAME);
        SFC sfc_own = null;
        
        // routerが持つSFC
        if(r.isSFCExists(sfc_int)){
            sfc_own = r.getSfcMap().get(sfc_int);
        }else{
            // 初めてのSFCなら，登録する．
            r.getSfcMap().put(sfc_int.getSfcID(), (SFC)sfc_int.deepCopy());
            sfc_own = r.getSfcMap().get(sfc_int.getSfcID());
        }

        // sfc_intとsfc_ownに差分がある場合，どうするか．
        // 自分から転送するsfcでは，あくまで自身がもつものである．よって，すべてが最新である必要がある．
        // 双方に割当先が記載されていれば，あくまで自分のものを優先させるべきである．
        Iterator<VNF> vIte = sfc_int.getVnfMap().values().iterator();
        while(vIte.hasNext()){
            VNF vnf = vIte.next();
            String vPrefix = vnf.getvCPUID();
            if(vPrefix != null){
                VNF ownVNF = sfc_own.findVNFByLastID(vnf.getIDVector().get(1));
                if(ownVNF.getvCPUID() == null){
                    // もし自身のprefixがなければ設定してあげる．
                    ownVNF.setvCPUID(vPrefix);
                }
            }
        }
        // そしてInterestパケットの中身のSFCを更新する．
        sfc_int = sfc_own;
        
        // 次に，後続タスクを取得
        SFC sfc = sfc_own;
        Long predID = AutoSFCMgr.getIns().getPredVNFID(p.getPrefix());
        VNF predSF = sfc.findVNFByLastID(predID);
        
        if(predSF.getvCPUID() != null){
            // もし既に割り当て済みなら，それを送る．
            return predSF.getvCPUID();
        }

        // ここから NoRouting 特有の処理
        // 他のルータを探索せず，必ず自身のvCPUから選択する．

        // まずは自身のvCPUにおける最小のblevelWSTを計算する．
        Iterator<VCPU> vIte2 = r.getvCPUMap().values().iterator();
        VCPU localVCPU = null;
        double localMinWST = Long.MAX_VALUE;

        Long sucID = AutoSFCMgr.getIns().getSucVNFID(p.getPrefix());

        while(vIte2.hasNext()){
            VCPU vcpu = vIte2.next();
            double blevelWST = Long.MAX_VALUE;
            // END->nullの場合は，ENDの処理時間のみを考える．
            if(sucID == -1){
                blevelWST = AutoSFCMgr.getIns().calcExecTime(predSF.getWorkLoad(), vcpu);
            }else{
                // ENDでなければ，普通に計算する．
                blevelWST = this.calcBlevelWST(predSF, vcpu, sfc);
            }
            if(blevelWST < localMinWST){
                localMinWST = blevelWST;
                localVCPU = vcpu;
            }
        }

        //NoRoutingのための処理
        // 常に自身の最良のvCPUを返す
        if (localVCPU != null) {
            return localVCPU.getPrefix();
        } else {
            // 万が一vCPUがない場合などは自身のルータIDなどを返す必要があるかもしれないが
            // 通常vCPUはあるはずなので，ここではnullチェック程度にしておく
            return r.getRouterID().toString(); 
        }
    }
}
