package net.bsnx.jlib.graphics;

public class Vec2d {

	private double x;
	private double y;
	
	/**
	 * Von 0 bis 2*PI
	 * 
	 * @link https://de.wikipedia.org/wiki/Polarkoordinaten
	 */
	private double angle;
	private double r;

	public Vec2d(double x, double y) {
		this.x = x;
		this.y = y;
		//\varphi = \begin{cases}
		//+\arccos\frac{x}{r} & \mathrm{f\ddot ur}\ y\geq 0\\
		//-\arccos\frac{x}{r} & \mathrm{f\ddot ur}\ y<0
		//\end{cases}
		this.r = Math.sqrt ( x*x + y*y );
		if (y>=0) {
			angle = Math.acos(x/r);
		} else {
			angle = -Math.acos(x/r);
		}
		if (angle < 0) {
			angle = angle + 2*Math.PI;
		}
	}
	
	public double x() {
		return x;
	}
	
	public double y() {
		return y;
	}
	
	public double length() {
		return r;
	}
	
	public double angle() {
		return r;
	}
	
	/**
	 * 
	 * @return Grad (0-360)
	 */
	public double getDegree() {
		return 180/Math.PI * angle;
	}
	
	@Override
	public String toString() {
		return "("+x+"|"+y+")";
	}
	
	public Vec2d turnBy(double angle) {
		double a = this.angle + angle;
		// x=r\cos\varphi
		// y=r\sin\varphi
		return new Vec2d(r*Math.cos(a), r*Math.sin(a));
	}
	
	public Vec2d scale(double factor) {
		return new Vec2d(factor*r*Math.cos(angle), factor*r*Math.sin(angle));
	}
	
	public Vec2d normalize() {
		return new Vec2d(x/r, y/r);
	}   

	public double dotProduct ( Vec2d v1 ) {
		return this.x*v1.x + this.y*v1.y;
	}
	
	public Vec2d addLength(double dr) {
		return new Vec2d((r+dr)*Math.cos(angle), (r+dr)*Math.sin(angle));
	}
	
	//public Vector2D add( Vector2D v1 ) {
	//       Vector2D v2 = new Vector2D( this.dX + v1.dX, this.dY + v1.dY );
	//       return v2;
	//}

	//public Vector2D sub( Vector2D v1 ) {
	//       Vector2D v2 = new Vector2D( this.dX - v1.dX, this.dY - v1.dY );
	//       return v2;
	//}

}
