package com.github.nekomeowww.customdrones.api.model;

import java.util.*;
import java.util.Map.Entry;
import net.minecraft.util.math.Vec3d;
import com.github.nekomeowww.customdrones.api.helpers.VecHelper;

public class CMTriangleMountain
        extends CMBase
{
    public List<List<CMTriangle>> trianglesLayers = new ArrayList();
    public CMPolygon topCap;
    public CMPolygon bottomCap;

    public CMTriangleMountain(CapType cap, List<Vec3d>... layers)
    {
        for (int a = 0; a < layers.length - 1; a++)
        {
            List<Vec3d> thisLayer = layers[a];
            List<Vec3d> nextLayer = layers[(a + 1)];
            boolean nextHasMore = nextLayer.size() > thisLayer.size();
            this.trianglesLayers
                    .add(makeTriangles(nextHasMore ? nextLayer : thisLayer, nextHasMore ? thisLayer : nextLayer));
        }
        if (cap != CapType.NONE)
        {
            List<Vec3d> bottomLayer = layers[0];
            List<Vec3d> topLayer = layers[(layers.length - 1)];
            Vec3d bottomMid = VecHelper.getMidList(bottomLayer);
            Vec3d topMid = VecHelper.getMidList(topLayer);
            if (cap != CapType.BOTTOM)
            {
                this.topCap = new CMPolygon((Vec3d[])topLayer.toArray(new Vec3d[0]), null, topMid.subtract(bottomMid).normalize());
                addChild(this.topCap);
            }
            if (cap != CapType.TOP)
            {
                this.bottomCap = new CMPolygon(VecHelper.reverse((Vec3d[])bottomLayer.toArray(new Vec3d[0])), null, bottomMid.subtract(topMid).normalize());
                addChild(this.bottomCap);
            }
        }
    }

    public CMTriangleMountain(CapType cap, Vec3d[]... layers)
    {
        this(cap, toListList(layers));
    }

    public static List<Vec3d>[] toListList(Vec3d[]... layers)
    {
        List<Vec3d>[] listList = new List[layers.length];
        for (int a = 0; a < layers.length; a++)
        {
            List<Vec3d> thisList = Arrays.asList(layers[a]);
            listList[a] = thisList;
        }
        return listList;
    }

    public List<CMTriangle> makeTriangles(List<Vec3d> moreLayer, List<Vec3d> lessLayer)
    {
        List<CMTriangle> triangles = new ArrayList();
        Vec3d innerPoint = VecHelper.getMidList(moreLayer);
        LinkedHashMap<Integer, List<Integer>> moreToLess = new LinkedHashMap();
        LinkedHashMap<Integer, List<Integer>> lessToMore = new LinkedHashMap();
        for (int a = 0; a < lessLayer.size(); a++)
        {
            Vec3d v1InLess = (Vec3d)lessLayer.get(a);

            Vec3d closestMore = VecHelper.getClosest(v1InLess, (Vec3d[])moreLayer.toArray(new Vec3d[0]));
            Integer closestMoreIndex = Integer.valueOf(moreLayer.indexOf(closestMore));

            List<Integer> closestToLessv1 = !moreToLess.containsKey(closestMoreIndex) ? new ArrayList() : (List)moreToLess.get(closestMoreIndex);
            closestToLessv1.add(Integer.valueOf(a));
            moreToLess.put(closestMoreIndex, closestToLessv1);

            List<Integer> lessv1ToMore = !lessToMore.containsKey(Integer.valueOf(a)) ? new ArrayList() : (List)lessToMore.get(Integer.valueOf(a));
            lessv1ToMore.add(closestMoreIndex);
            lessToMore.put(Integer.valueOf(a), lessv1ToMore);
        }
        List<Integer> firstMoresConnectedToLess = new ArrayList(moreToLess.keySet());
        List<Integer> firstLessConnectedToMore = new ArrayList(lessToMore.keySet());
        for (int a = 0; a < firstMoresConnectedToLess.size(); a++)
        {
            Integer thisMoreHasConnection = (Integer)firstMoresConnectedToLess.get(a);
            List<Integer> thisMoreConnections = (List)moreToLess.get(thisMoreHasConnection);
            Integer nextMoreHasConnection = (Integer)firstMoresConnectedToLess.get((a + 1) % firstMoresConnectedToLess.size());
            List<Integer> nextMoreConnections = (List)moreToLess.get(nextMoreHasConnection);
            if ((nextMoreHasConnection.intValue() - thisMoreHasConnection.intValue() <= 1) ||
                    (thisMoreHasConnection.intValue() - nextMoreHasConnection.intValue() == moreLayer.size() - 1))
            {
                boolean adjacentMoreSameLess = false;
                for (Integer connectionOfThisMore : thisMoreConnections) {
                    if (nextMoreConnections.contains(connectionOfThisMore))
                    {
                        adjacentMoreSameLess = true;
                        break;
                    }
                }
                if (adjacentMoreSameLess) {}
            }
            else
            {
                Integer lessConnectedToThisMore = null;
                Integer lessConnectedToNextMore = null;
                for (int b = 0; b < firstLessConnectedToMore.size(); b++)
                {
                    Integer thisLessHasConnection = (Integer)firstLessConnectedToMore.get(b);
                    List<Integer> thisLessConnections = (List)lessToMore.get(thisLessHasConnection);
                    Integer nextLessHasConnection = (Integer)firstLessConnectedToMore.get((b + 1) % firstLessConnectedToMore.size());
                    List<Integer> nextLessConnections = (List)lessToMore.get(nextLessHasConnection);
                    if ((thisLessConnections.contains(thisMoreHasConnection)) &&
                            (nextLessConnections.contains(nextMoreHasConnection)))
                    {
                        lessConnectedToThisMore = thisLessHasConnection;
                        lessConnectedToNextMore = nextLessHasConnection;
                        break;
                    }
                }
                if (lessConnectedToThisMore != null)
                {
                    int amidUnlocalMax = nextMoreHasConnection.intValue() > thisMoreHasConnection.intValue() ? nextMoreHasConnection.intValue() : moreLayer.size() + nextMoreHasConnection.intValue();
                    int amidMaxIndex = amidUnlocalMax - thisMoreHasConnection.intValue();
                    int amidIndex = 0;
                    for (int amidMoreUnlocal = thisMoreHasConnection.intValue(); amidMoreUnlocal <= amidUnlocalMax; amidMoreUnlocal++)
                    {
                        int amidMore = amidMoreUnlocal >= moreLayer.size() ? amidMoreUnlocal % moreLayer.size() : amidMoreUnlocal;
                        if ((amidIndex > 0) && (amidIndex < amidMaxIndex))
                        {
                            List<Integer> newSet = new ArrayList();
                            if (amidIndex <= amidMaxIndex / 2)
                            {
                                newSet.add(lessConnectedToThisMore);
                                List<Integer> theList = (List)lessToMore.get(lessConnectedToThisMore);
                                theList.add(Integer.valueOf(amidMore));
                            }
                            if (amidIndex >= amidMaxIndex / 2)
                            {
                                newSet.add(lessConnectedToNextMore);
                                List<Integer> theList = (List)lessToMore.get(lessConnectedToNextMore);
                                theList.add(Integer.valueOf(amidMore));
                            }
                            moreToLess.put(Integer.valueOf(amidMore), newSet);
                        }
                        if ((amidIndex * 2 - 1 == amidMaxIndex) && (amidMaxIndex % 2 == 1))
                        {
                            List<Integer> theSet = moreToLess.containsKey(Integer.valueOf(amidMore)) ? (List)moreToLess.get(Integer.valueOf(amidMore)) : new ArrayList();

                            theSet.add(lessConnectedToThisMore);
                            moreToLess.put(Integer.valueOf(amidMore), theSet);
                            List<Integer> theList = (List)lessToMore.get(lessConnectedToThisMore);
                            theList.add(Integer.valueOf(amidMore));
                        }
                        amidIndex++;
                    }
                }
            }
        }
        for (Map.Entry<Integer, List<Integer>> moreToLessEntry : moreToLess.entrySet())
        {
            Integer moreIndex = (Integer)moreToLessEntry.getKey();
            Vec3d moreVertex = (Vec3d)moreLayer.get(moreIndex.intValue());
            List<Integer> lessList = (List)moreToLessEntry.getValue();
            for (int a = 0; a < lessList.size() - 1; a++)
            {
                Vec3d lessVertex1 = (Vec3d)lessLayer.get(((Integer)lessList.get(a)).intValue());
                Vec3d lessVertex2 = (Vec3d)lessLayer.get(((Integer)lessList.get(a + 1)).intValue());
                CMTriangle triangle = new CMTriangle(moreVertex, lessVertex1, lessVertex2, innerPoint);
                triangles.add(triangle);
            }
        }
        for (Map.Entry<Integer, List<Integer>> lessToMoreEntry : lessToMore.entrySet())
        {
            Integer lessIndex = (Integer)lessToMoreEntry.getKey();
            Vec3d lessVertex = (Vec3d)lessLayer.get(lessIndex.intValue());
            List<Integer> moreList = (List)lessToMoreEntry.getValue();
            for (int a = 0; a < moreList.size() - 1; a++)
            {
                Vec3d moreVertex1 = (Vec3d)moreLayer.get(((Integer)moreList.get(a)).intValue());
                Vec3d moreVertex2 = (Vec3d)moreLayer.get(((Integer)moreList.get(a + 1)).intValue());
                CMTriangle triangle = new CMTriangle(lessVertex, moreVertex1, moreVertex2, innerPoint);
                triangles.add(triangle);
            }
        }
        return triangles;
    }

    public void setPaletteIndexColor(String s, Color c, boolean setFull)
    {
        List<String> pIndexes = getPaletteIndexes();
        for (int a = 0; a < pIndexes.size(); a++)
        {
            String pNow = (String)pIndexes.get(a);
            if (pNow.equals(s))
            {
                if ((a == 0) && (this.bottomCap != null)) {
                    this.bottomCap.setColor(c);
                }
                Iterator localIterator;
                if (a < this.trianglesLayers.size())
                {
                    List<CMTriangle> triangles = (List)this.trianglesLayers.get(a);
                    for (localIterator = triangles.iterator(); localIterator.hasNext();)
                    {
                        triangle = (CMTriangle)localIterator.next();

                        triangle.setColor(c);
                    }
                }
                CMTriangle triangle;
                if (a == pIndexes.size() - 1)
                {
                    if (this.topCap != null) {
                        this.topCap.setColor(c);
                    }
                    for (int b = a + 1; b < this.trianglesLayers.size(); b++)
                    {
                        Object triangles = (List)this.trianglesLayers.get(b);
                        for (CMTriangle triangle : (List)triangles) {
                            triangle.setColor(c);
                        }
                    }
                }
            }
        }
    }

    public void render()
    {
        super.render();
        for (int a = 0; a < this.trianglesLayers.size(); a++)
        {
            List<CMTriangle> triangles = (List)this.trianglesLayers.get(a);
            for (CMTriangle triangle : triangles) {
                triangle.fullRender();
            }
        }
    }
}
