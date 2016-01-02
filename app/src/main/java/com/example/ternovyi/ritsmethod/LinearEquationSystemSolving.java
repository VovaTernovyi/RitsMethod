package com.example.ternovyi.ritsmethod;

/**
 * Created by vova on 04.12.15.
 */
public class LinearEquationSystemSolving {

    public static double[] rightProgonka(double[][] matrix, double[] d, int n) {
        double[] a = new double[n];
        double[] b = new double[n];
        double[] c = new double[n];

        for (int i = 0; i < n; i++) {
            if (i != 0) {
                a[i] = matrix[i][i - 1];
            } else {
                a[i] = 0;
            }

            b[i] = matrix[i][i];

            if (i != n - 1) {
                c[i] = matrix[i][i + 1];
            } else {
                c[i] = 0;
            }
        }

        double[] c_ = new double[n];
        double[] d_ = new double[n];

        c_[0] = c[0] / b[0];
        d_[0] = d[0] / b[0];

        for (int i = 1; i < n; i++) {
            c_[i] = c[i] / (b[i] - c_[i - 1] * a[i]);
            d_[i] = (d[i] - d_[i - 1] * a[i]) / (b[i] - c_[i - 1] * a[i]);
        }

        double[] x = new double[n];

        x[n - 1] = d_[n - 1];

        for (int i = n - 2; i >= 0; i--) {
            x[i] = d_[i] - (c_[i] * x[i + 1]);
        }

        return x;
    }
}
