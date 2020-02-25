import org.kpi.lab2.exceptions.MatricesAreNotMultipliedException;
import org.kpi.lab2.exceptions.MatricesAreNotSquareException;
import org.kpi.lab2.exceptions.WrongProcessorsNumberException;
import org.kpi.lab2.foxAlgo.FoxAlgo;
import org.kpi.lab2.lineAlgo.StrapeOne;
import org.kpi.lab2.matrix.Matrix;

import java.io.File;

public class Main {
    private static long start;

    public static void main(String[] args) {
        boolean isFromFile = false;
        int processors = 9;
        int size = 5;

        Matrix A = (Matrix) StrapeOne.createMatrix(size, size, isFromFile, new File("A.txt"));
        Matrix B = (Matrix) StrapeOne.createMatrix(size, size, isFromFile, new File("B.txt"));

        if (A.getCols() != B.getRows()) {
            System.out.println("Матриці не можуть бути перемножені");
            System.exit(0);
        }

        System.out.println("---------- Звичайне множення матриць ----------");
        Matrix simpleC = new Matrix(A.getRows(), B.getCols(), 0);
        start = System.nanoTime();
        StrapeOne.simpleMultiplying(A, B, simpleC);
        finishNanoTimer();
        StrapeOne.writeInFile(simpleC, new File("simpleC.txt"));
        System.out.println("---------- Алгоритм Фокса ----------");
        start = System.nanoTime();
        Matrix foxC = null;
        try {
            foxC = FoxAlgo.foxMultiply(A, B, processors);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        finishNanoTimer();
        StrapeOne.writeInFile(foxC, new File("foxC.txt"));

    }

    public static void finishNanoTimer() {
        long finish = System.nanoTime();
        long time = finish - start;
        System.out.println("Тривалість обчислень: " + (time / 1000000000) + " с., " +
                (time / 1000000 % 1000) + " мс, " + (time / 1000 % 1000) + " мкс.");
        System.out.println("Тривалість обчислень в наносекундах: " + time);
        start = 0;
    }
}
