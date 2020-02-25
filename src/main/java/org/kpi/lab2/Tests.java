package org.kpi.lab2;

import org.kpi.lab2.exceptions.MatricesAreNotMultipliedException;
import org.kpi.lab2.exceptions.MatricesAreNotSquareException;
import org.kpi.lab2.exceptions.WrongProcessorsNumberException;
import org.kpi.lab2.foxAlgo.FoxAlgo;
import org.kpi.lab2.lineAlgo.StrapeOne;
import org.kpi.lab2.matrix.Matrix;

public class Tests {
    private static long start;

    public static void main(String[] args) {
        int[] processors = {4, 9, 16, 25};
        int[] sizes = {500, 1000, 1500, 2000, 2500, 3000};

        for (int size : sizes) {
            Matrix A = new Matrix(size, size);
            Matrix B = new Matrix(size, size);
            System.out.println("\nРОЗМІР МАТРИЦЬ: " + size);
            System.out.println("\n---------- Звичайне множення матриць ----------");
            Matrix simpleC = new Matrix(A.getRows(), B.getCols(), 0);
            start = System.nanoTime();
            StrapeOne.simpleMultiplying(A, B, simpleC);
            finishNanoTimer();

            System.out.println();
            for (int p : processors) {
                System.out.print(p + "\t\t");
            }

            System.out.println("\n---------- Cтрічковий алгоритм множення матриць ----------");
            for (int p : processors) {
                Matrix blockC = new Matrix(A.getRows(), B.getCols(), 0);
                start = System.nanoTime();
                StrapeOne.blockMultiply(A, B, blockC, p);
                finishNanoTimer();
            }

            System.out.println("\n---------- Алгоритм Фокса ----------");
            for (int p : processors) {
                start = System.nanoTime();
                try {
                    FoxAlgo.foxMultiply(A, B, p);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(0);
                }
                finishNanoTimer();
            }
        }
    }

    public static void finishNanoTimer() {
        long finish = System.nanoTime();
        long time = finish - start;
        System.out.print((time / 1000000) + " мс.\t");
        start = 0;
    }
}
