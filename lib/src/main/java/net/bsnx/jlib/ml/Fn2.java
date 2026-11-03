package net.bsnx.jlib.ml;

public class Fn2 {
    private Double a, b, c;

    public Fn2() {}

    public Fn2(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public double call(double x) {
        if (a == null || b == null || c == null)
            throw new IllegalStateException("Not trained yet.");
        return a * x*x + b * x + c;
    }

    // points[i] = {x, y}
    public void train(double[]... points) {
        if (points.length < 3)
            throw new IllegalArgumentException("Need at least 3 points.");

        // Build augmented matrix for least squares: (X^T X) * coeffs = X^T Y
        double[][] XtX = new double[3][3];
        double[]   XtY = new double[3];

        for (double[] p : points) {
            double x = p[0], y = p[1];
            double[] row = {x*x, x, 1.0};
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++)
                    XtX[i][j] += row[i] * row[j];
                XtY[i] += row[i] * y;
            }
        }

        double[] coeffs = solve(XtX, XtY);
        this.a = coeffs[0];
        this.b = coeffs[1];
        this.c = coeffs[2];
    }

    private double[] solve(double[][] A, double[] Y) {
        int n = Y.length;
        // Build augmented matrix
        double[][] M = new double[n][n + 1];
        for (int i = 0; i < n; i++) {
            System.arraycopy(A[i], 0, M[i], 0, n);
            M[i][n] = Y[i];
        }
        // Gauss-Jordan
        for (int col = 0; col < n; col++) {
            int max = col;
            for (int row = col+1; row < n; row++)
                if (Math.abs(M[row][col]) > Math.abs(M[max][col])) max = row;
            double[] tmp = M[col]; M[col] = M[max]; M[max] = tmp;

            if (Math.abs(M[col][col]) < 1e-10)
                throw new ArithmeticException("Singular matrix.");

            for (int row = 0; row < n; row++) {
                if (row == col) continue;
                double f = M[row][col] / M[col][col];
                for (int j = col; j <= n; j++)
                    M[row][j] -= f * M[col][j];
            }
        }
        double[] result = new double[n];
        for (int i = 0; i < n; i++)
            result[i] = M[i][n] / M[i][i];
        return result;
    }

    @Override
    public String toString() {
        if (a == null) return "?x² + ?x + ?";
        return String.format("%+.4fx² %+.4fx %+.4f", a, b, c);
    }

    public static void main(String[] args) {
        // From training
        Fn2 fn1 = new Fn2();
        fn1.train(new double[]{0,1}, new double[]{1,3}, new double[]{2,9}, new double[]{3,19});
        System.out.println(fn1);
        System.out.println(fn1.call(2)); // ≈ 9.0

        // Direct
        Fn2 fn2 = new Fn2(1, -2, 3);
        System.out.println(fn2.call(2)); // ≈ 3.0
    }
}