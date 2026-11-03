package net.bsnx.jlib.graphics;

public class Vec3d  {

    public double x;
    public double y;
    public double z;

    public Vec3d() {
        this(0,0,0);
    }

    public Vec3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setValues(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getDimensions() {
        return 3;
    }


    public double getComponent(int i) {
        switch (i) {
            case 0: return x;
            case 1: return y;
            case 2: return z;
            default: return 0;
        }
    }


    public Vec3d copy() {
        return new Vec3d(x, y, z);
    }

    public Vec3d nullVector() {
        return new Vec3d(0,0,0);
    }

    public Vec3d add(Vec3d v) {
        Vec3d vv = (Vec3d)v;
        return new Vec3d(x + vv.x, y + vv.y, z + vv.z);
    }

    public String numberToString(double val) {
        return String.format("%.6f", val).replace(',', '.');
    }

    public String implde(String glue) {
        return numberToString(x)+glue+numberToString(y)+glue+numberToString(z);
    }

    public double distance(double x, double y, double z) {
        double v = (this.x-x)*(this.x-x)+(this.y-y)*(this.y-y)+(this.z-z)*(this.z-z);
        if (v > 0) {
            return Math.sqrt(v);
        } else {
            return 0;
        }
    }

    public double distance(Vec3d other) {
        double v = (this.x-other.x)*(this.x-other.x)+(this.y-other.y)*(this.y-other.y)+(this.z-other.z)*(this.z-other.z);
        if (v > 0) {
            return Math.sqrt(v);
        } else {
            return 0;
        }
    }
}
