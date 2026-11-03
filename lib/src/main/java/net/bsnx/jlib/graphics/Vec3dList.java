package net.bsnx.jlib.graphics;

import java.util.ArrayList;

public class Vec3dList extends ArrayList<Vec3d> {

    public double min(int component) {
        if (size()==0) {
            return 0;
        }
        double res = get(0).getComponent(component);
        for (Vec3d v : this) {
            res = Math.min(res, v.getComponent(component));
        }
        return res;
    }

    public double max(int component) {
        if (size()==0) {
            return 0;
        }
        double res = get(0).getComponent(component);
        for (Vec3d v : this) {
            res = Math.max(res, v.getComponent(component));
        }
        return res;
    }

    public double minX() {
        return min(0);
    }

    public double minY() {
        return min(1);
    }

    public double minZ() {
        return min(2);
    }

    public double maxX() {
        return max(0);
    }

    public double maxY() {
        return max(1);
    }

    public double maxZ() {
        return max(2);
    }

    public Vec3dList reduceDistance(double d) {
        Vec3dList points = new Vec3dList();
        points.addAll(this);
        Vec3dList result = new Vec3dList();
        while (!points.isEmpty()) {
            Vec3d point = points.get(0);
            points.remove(0);
            result.add(point);
            Vec3dList buffer = new Vec3dList();
            for (Vec3d other : points) {
                if (point.distance(other)>d) {
                    buffer.add(other);
                }
            }
            points = buffer;
        }
        return result ;
    }
}
