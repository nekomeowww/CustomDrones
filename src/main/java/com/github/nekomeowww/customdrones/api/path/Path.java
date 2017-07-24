package com.github.nekomeowww.customdrones.api.path;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.math.Vec3d;
import com.github.nekomeowww.customdrones.api.helpers.VecHelper;

public class Path
{
    public List<Node> nodes = new ArrayList();
    public int pathIndex;

    public Path simplifiedPath()
    {
        Path p = new Path();
        if (!this.nodes.isEmpty())
        {
            p.addNode((Node)this.nodes.get(0));
            for (int a = 1; a < this.nodes.size(); a++)
            {
                Node prevNode = (Node)this.nodes.get(a - 1);
                Node nowNode = (Node)this.nodes.get(a);
                if (a == this.nodes.size() - 1)
                {
                    p.addNode(nowNode);
                    break;
                }
                Vec3d travelled = nowNode.toVec().subtract(prevNode.toVec());
                double travelledLength = travelled.lengthVector();
                for (int b = a + 1; b < this.nodes.size(); b++)
                {
                    Node comingNode = (Node)this.nodes.get(b);
                    Vec3d toTravel = comingNode.toVec().subtract(nowNode.toVec());
                    double angleBetween = VecHelper.getAngleBetween(travelled, toTravel);
                    if ((b == this.nodes.size() - 1) || (angleBetween > 0.17453292519943295D) ||
                            (toTravel.lengthVector() - travelledLength * (b - a) > 8.0E-4D))
                    {
                        a = b;
                        p.addNode(comingNode);
                        break;
                    }
                }
            }
        }
        return p;
    }

    public void clearPath()
    {
        this.nodes.clear();
        resetPathIndex();
    }

    public void resetPathIndex()
    {
        this.pathIndex = 0;
    }

    public boolean hasPath()
    {
        return !this.nodes.isEmpty();
    }

    public Node goToNextNode()
    {
        if (this.pathIndex < this.nodes.size() - 1) {
            return (Node)this.nodes.get(this.pathIndex++);
        }
        return null;
    }

    public Node getNextNode()
    {
        if (this.pathIndex < this.nodes.size() - 1) {
            return (Node)this.nodes.get(this.pathIndex + 1);
        }
        return null;
    }

    public Node getCurrentNode()
    {
        if (this.pathIndex < this.nodes.size()) {
            return (Node)this.nodes.get(this.pathIndex);
        }
        return null;
    }

    public Node goToPrevNode()
    {
        if (this.pathIndex > 0) {
            return (Node)this.nodes.get(this.pathIndex--);
        }
        return null;
    }

    public Node getPrevNode()
    {
        if (this.pathIndex > 0) {
            return (Node)this.nodes.get(this.pathIndex - 1);
        }
        return null;
    }

    public Path addNode(Node p)
    {
        this.nodes.add(p);
        return this;
    }

    public Path addNodes(Node... pathNodes)
    {
        for (Node p : pathNodes) {
            addNode(p);
        }
        return this;
    }
}
