package com.example.ternovyi.ritsmethod;

/**
 * Created by vova on 04.12.15.
 */
public class SturmLiouvilleProblem {

    private double a, b;
    public int N;
    private double h;
    public double[] result;
    private double integralE = 0.00001;

    Func p = new Func() {
        @Override
        public double funcExecute(double x) {
            return x*x;
        }
    };

    Func q = new Func() {
        @Override
        public double funcExecute(double x) {
            return 4 * (x + 1);
        }
    };

    Func f = new Func() {
        @Override
        public double funcExecute(double x) {
            return -(2 * x * (-2 * (x - 2)) + x * x * (-2) - 4 * (x + 1) * (-(x - 2) * (x - 2) + 4));
        }
    };


    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }

    public SturmLiouvilleProblem(Func f, Func p, Func q, double A, double B)
    {
        this.f = f;
        this.p = p;
        this.q = q;
        a = A;
        b = B;
    }
    public double[] SolveWithFiniteElementMethod(int _n)
    {
        N = _n;
        h = (b - a) / N;
        // Формуємо матрицю
        double[][] A = new double[N][N];
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                A[i][j] = 0;
            }
        }

        A[0][0] += Integral.CalculateWithSimpsonMethod(core_i2j2, a, a + h, integralE, 1);
        for (int i = 2; i <= N; i++)
        {
            A[i - 2][i - 2] += Integral.CalculateWithSimpsonMethod(core_i1j1, a + (i - 1) * h, a + i * h, integralE, i);
            A[i - 2][i - 1] += Integral.CalculateWithSimpsonMethod(core_i1j2, a + (i - 1) * h, a + i * h, integralE, i);
            A[i - 1][i - 2] += Integral.CalculateWithSimpsonMethod(core_i2j1, a + (i - 1) * h, a + i * h, integralE, i);
            A[i - 1][i - 1] += Integral.CalculateWithSimpsonMethod(core_i2j2, a + (i - 1) * h, a + i * h, integralE, i);
        }
        // Формуємо праву частину
        double[] Q = new double[N];
        for (int i = 0; i < N; i++)
        {
            Q[i] = 0;
        }
        Q[0] += Integral.CalculateWithSimpsonMethod(B2, a, a + h, integralE, 1);
        for (int i = 2; i <= N; i++)
        {
            Q[i - 2] += Integral.CalculateWithSimpsonMethod(B1, a + (i - 1) * h, a + i * h, integralE, i);
            Q[i - 1] += Integral.CalculateWithSimpsonMethod(B2, a + (i - 1) * h, a + i * h, integralE, i);
        }
        result = LinearEquationSystemSolving.rightProgonka(A, Q, N);
        return result;
    }
    public double Solution(double x)
    {
        double result = 0; ;
        for (int i = 1; i <= N; i++)
        {
            result += this.result[i - 1] * BaseFunc(i, x);
        }
        return result;
    }
    private double BaseFunc(int i, double x)
    {
        double result = 0;
        if ((x > a + (i - 1) * h) && (x <= a + i * h)) result = (x - a - (i - 1) * h) / h;
        if ((x < a + (i + 1) * h) && (x > a + i * h)) result = -(x - a - (i + 1) * h) / h;
        return result;
    }

    Core core_i1j1 = new Core() {
        @Override
        public double GetValue(double x, double i) {
            return p.funcExecute(x) / (h * h) + q.funcExecute(x) * Math.pow(x - a - i * h, 2) / (h * h); //?
        }
    };

    Core core_i1j2 = new Core() {
        @Override
        public double GetValue(double x, double i) {
            return -p.funcExecute(x) / (h * h) + q.funcExecute(x) * (-(x - a - i * h) * (x - a - (i - 1) * h)) / (h * h);
        }
    };

    Core core_i2j1 = new Core() {
        @Override
        public double GetValue(double x, double i) {
            return -p.funcExecute(x) / (h * h) + q.funcExecute(x) * (-(x - a - i * h) * (x - a - (i - 1) * h)) / (h * h);
        }
    };

    Core core_i2j2 = new Core() {
        @Override
        public double GetValue(double x, double i) {
            return p.funcExecute(x) / (h * h) + q.funcExecute(x) * Math.pow(x - a - (i - 1) * h, 2) / (h * h);
        }
    };

    Core B1 = new Core() {
        @Override
        public double GetValue(double x, double i) {
            return f.funcExecute(x) * (-(x - a - i * h) / h);
        }
    };

    Core B2 = new Core() {
        @Override
        public double GetValue(double x, double i) {
            return f.funcExecute(x) * (x - a - (i - 1) * h) / h;
        }
    };
}
