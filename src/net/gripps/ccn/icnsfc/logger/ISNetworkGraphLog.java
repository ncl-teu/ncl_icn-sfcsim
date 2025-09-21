package net.gripps.ccn.icnsfc.logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.*;
import edu.uci.ics.jung.algorithms.shortestpath.Distance;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationImageServer;
import edu.uci.ics.jung.visualization.VisualizationServer;

import net.gripps.ccn.core.CCNNode;
import net.gripps.ccn.core.CCNRouter;
import net.gripps.ccn.core.Face;
import net.gripps.ccn.process.CCNMgr;
import net.gripps.cloud.core.Cloud;

public class ISNetworkGraphLog {
    private static VisualizationImageServer<Long, Integer> graphImagePanel;
    private static ArrayList<Long> simplyRouters = new ArrayList<Long>();

    static Transformer<Long,Paint> nodeFillColor = new Transformer<Long, Paint>() {
        @Override
        public Paint transform(Long ID) {
            if(simplyRouters.contains(ID)) {
                return Color.MAGENTA;
            }
            else{
                return Color.RED;
            }
        }
    };

    public static void makeNetworkGraphLog(String logName) {
        HashMap<Long, ArrayList<Long>> NetworkEnvironmentMap = getNetworkEnvironmentMap();
        Graph<Long, Integer> networkGraph = createNetworkGraph(NetworkEnvironmentMap);
        Dimension graphArea = new Dimension(3000, 3000);
        Layout<Long, Integer> layout = new FRLayout<>(networkGraph);
        File file = new File("./is/" + logName + ".png");

        graphImagePanel = new VisualizationImageServer<>(layout, graphArea);

        graphImagePanel.getRenderContext().setVertexFillPaintTransformer(nodeFillColor);

        writeGraphImage(file);
    }

    /**
     * CCNMGrに問い合わせて，各ルータの隣接ノードリストを作成するメソッド
     */
    private static HashMap<Long, ArrayList<Long>> getNetworkEnvironmentMap() {
        HashMap<Long, ArrayList<Long>> networkEnvironment = new HashMap<Long, ArrayList<Long>>();
        //{RouterOrNodeID : [Neighbor1, Neighbor2]}
        HashMap<Long, Long> nodeBandWidthMap = new HashMap<Long, Long>();
        //{RouterID : BandWidth} // 未使用 将来のため残しておく

        HashMap<Long, CCNNode> nodeMap = CCNMgr.getIns().getNodeMap();
        HashMap<Long, CCNRouter> routerMap = CCNMgr.getIns().getRouterMap();
        for(Map.Entry<Long, CCNNode> nodeEntry : nodeMap.entrySet()) {
            ArrayList<Long> neighborsList = new ArrayList<Long>();
            Long nodeID = nodeEntry.getKey() + 10000;
            CCNNode node = nodeEntry.getValue();
            // add node BandWidth information
            nodeBandWidthMap.put(nodeID, node.getBw());
            // add neighbor router information
            HashMap<Long, CCNRouter> neighborRouterMap = node.getRouterMap();
            neighborsList.addAll(neighborRouterMap.keySet());
            networkEnvironment.put(nodeID, neighborsList);
        }
        for(Map.Entry<Long, CCNRouter> routerEntry : routerMap.entrySet()) {
            ArrayList<Long> neighborsList = new ArrayList<Long>();
            Long routerID = routerEntry.getKey();
            simplyRouters.add(routerID);
            CCNRouter router = routerEntry.getValue();
            // add node BandWidth information
            nodeBandWidthMap.put(routerID, router.getBw());
            // add neighbor router information
            HashMap<Long, Face> neighborRouterMap = router.getFace_routerMap();
            neighborsList.addAll(neighborRouterMap.keySet());
            // add neighbor node information
            HashMap<Long, Face> neighborNodeMap = router.getFace_nodeMap();
            for(Long neighborNodeID : neighborNodeMap.keySet()) {
                neighborsList.add(neighborNodeID + 10000);
            }
            networkEnvironment.put(routerID, neighborsList);
        }
        return networkEnvironment;
    }

    /**
     * 各ルータのFIBを見て隣接ノードリストを作成するメソッド
     */
    private static ArrayList<Long> getNeighborNode(CCNRouter router) {
        ArrayList<Long> neighbors = new ArrayList<Long>();
        Iterator<Face> faceIte = router.getFace_routerMap().values().iterator();

        while(faceIte.hasNext()) {
            Face face = faceIte.next();

            CCNRouter neighborRouter = CCNMgr.getIns().getRouterMap().get(face.getPointerID());
            neighbors.add(neighborRouter.getRouterID());
        }
        return neighbors;
    }

    private static Graph<Long, Integer> createNetworkGraph(Map<Long, ArrayList<Long>> networkEnvironment) {
        Graph<Long, Integer> networkGraph = new UndirectedSparseGraph<>();
        Integer edgeCount = 0;

        for(Map.Entry<Long, ArrayList<Long>> networkEntry : networkEnvironment.entrySet()) {
            Long nodeID = networkEntry.getKey();
            ArrayList<Long> neighborsList = networkEntry.getValue();

            networkGraph.addVertex(nodeID);

            for(Long neighborID : neighborsList) {
                networkGraph.addVertex(neighborID);
                networkGraph.addEdge(edgeCount, nodeID, neighborID);
                edgeCount++;
            }
        }
        return networkGraph;
    }

    protected static void writeGraphImage(File file) {
        BufferedImage bufferedImage = new BufferedImage(graphImagePanel.getWidth(), graphImagePanel.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        graphImagePanel.paint(g2d);
        g2d.dispose();

        File outputFile = new File(file.getAbsolutePath());

        try {
            ImageIO.write(bufferedImage, "png",outputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
