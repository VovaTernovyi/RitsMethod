package com.example.ternovyi.ritsmethod;

/**
 * Created by vova on 04.12.15.
 */
public class Integral {

    public static double CalculateWithSimpsonMethod(Core f, double a, double b, double eps, int i) {
        if (a == b) return 0;
        double I2n = 0, In = 0;
        eps = Math.abs(eps);
        double rizn = 1;
        double h = 0;
        double koef;
        In = 0;
        h = (b - a);
        In += (h / 6) * (f.GetValue(a,i) + 4.0 * f.GetValue((a + b) / 2, i) + f.GetValue(b, i));
        for (int n = 4; (n < 10000) && (rizn > eps); n *= 2)
        {
            I2n = 0;
            h = (b - a) / n;
            I2n += f.GetValue(a, i) + f.GetValue(b, i);
            boolean ans = true;
            for (int j = 1; j < n; j++)
            {
                koef = (ans) ? 4.0 : 2.0;
                I2n += koef * f.GetValue(a + j * h, i);
                ans = !ans;
            }
            I2n *= h / 3.0;
            rizn = Math.abs((I2n - In) / I2n);
            In = I2n;
        }
        return I2n;
    }
}
