package net.gripps.ccn.caching;

import net.gripps.ccn.CCNUtil;
import net.gripps.ccn.core.CCNContents;
import net.gripps.ccn.core.CCNNode;
import net.gripps.ccn.core.CCNRouter;
import net.gripps.ccn.core.Face;
import net.gripps.ccn.process.CCNMgr;

import java.util.HashMap;
import java.util.Iterator;

/**
 *  Create by Hidehiro Kanemitsu
 */
public class OnPathCaching extends BaseCachingAlgorithm {

    public OnPathCaching(HashMap<Long, CCNNode> nodeMap, HashMap<Long, CCNRouter> routerMap) {
        super(nodeMap, routerMap);
    }

    public OnPathCaching() {
    }

    @Override
    public boolean cachingProcess(CCNContents c, CCNRouter r) {
        //コンテンツの中身をキャッシュとする．
        c.setCache(true);
        //履歴をクリアする．
        c.getHistoryList().clear();
        c.setCurrentOwnerID(r.getRouterID());
        c.setGeneratedTimeAtCache(System.currentTimeMillis());
        //rのcsへ追加する．
        r.getCSEntry().getCacheMap().put(c.getPrefix(), c);
        //その後，faceリストの他ルータのFIBに追加してあげる？
        Face f= new Face(null, r.getRouterID(), CCNUtil.NODETYPE_ROUTER);
        //隣接ルータに対して，それらのFIBに，当該ルータへのポインタを追加しておく．
        //this.addFacetoFIB(c.getPrefix(), f, r);

        return true;
    }

    /**
     * ルータrのfaceリストにある隣接ルータたちに対してfを追加した後，(prefix, f)を
     * FIBへ追加する．
     * @param prefix
     * @param f
     * @param r
     */
    public void addFacetoFIB(String prefix, Face f, CCNRouter r){
        Iterator<Face> fIte  = r.getFace_routerMap().values().iterator();
        while(fIte.hasNext()){
            Face face = fIte.next();

            //隣接ルータを取得
            CCNRouter nRouter = CCNMgr.getIns().getRouterMap().get(face.getPointerID());
            //隣接ルータにfを追加
            nRouter.addFace(f, nRouter.getFace_routerMap());
            //FIBに追加
            nRouter.getFIBEntry().addFace(prefix, f);

        }
    }

    @Override
    public boolean chachingProcessIfNoPITEntry(CCNContents c, CCNRouter r) {
        c.setCache(true);
        c.setCurrentOwnerID(r.getRouterID());
        //履歴をクリアする．
        c.getHistoryList().clear();
        c.setGeneratedTimeAtCache(System.currentTimeMillis());
        //rのcsへ追加する．
        r.getCSEntry().getCacheMap().put(c.getPrefix(), c);
        return true;
    }

    @Override
    public boolean chachingIFCSFULL(CCNContents c, CCNRouter r) {
        return false;
    }
}
