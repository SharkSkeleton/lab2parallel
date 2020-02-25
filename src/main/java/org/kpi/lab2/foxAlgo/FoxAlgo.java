package org.kpi.lab2.foxAlgo;

import org.kpi.lab2.exceptions.MatricesAreNotMultipliedException;
import org.kpi.lab2.exceptions.MatricesAreNotSquareException;
import org.kpi.lab2.exceptions.WrongProcessorsNumberException;
import org.kpi.lab2.matrix.Matrix;

public class FoxAlgo {

    public static Matrix foxMultiply(final Matrix aMatrix, final Matrix bMatrix, final int proc)
            throws MatricesAreNotSquareException, WrongProcessorsNumberException, MatricesAreNotMultipliedException {
        final int aRows = aMatrix.getRows();
        final int aCols = aMatrix.getCols();
        final int bRows = bMatrix.getRows();
        final int bCols = bMatrix.getCols();

        if (aRows != aCols || bRows != bCols) {
            throw new MatricesAreNotSquareException("Матриці повинні бути квадратними");
        }

        final double procRoot = Math.sqrt(proc);
        final int blocksSqrt = (int) procRoot;

        // Check for sqrt of proc
        if (procRoot % 1 != 0) {
            throw new WrongProcessorsNumberException("Число процесорів має бути числом з якого можна взяти корінь");
        }

        final double blockSizeRaw = aRows / procRoot;
        final int blockSize = (int) blockSizeRaw + (blockSizeRaw % 1 > 0 ? 1 : 0);
        final int[][] multiplier1 = aMatrix.getMatrix();
        final int[][] multiplier2 = bMatrix.getMatrix();
        final int[][] product = new int[aRows][aCols];

        if (aCols != bRows) {
            String msg = String.format("Матриці не можуть бути перемножені\nЧисло колонок в першій матриці = %d\n" +
                    "Число рядків в другій матриці = %d", aCols, bRows);
            throw new MatricesAreNotMultipliedException(msg);
        } else {

            Thread[][] tArray = new Thread[blocksSqrt][blocksSqrt];
            for (int i = 0; i < blocksSqrt; i++) {
                for (int j = 0; j < blocksSqrt; j++) {
                    tArray[i][j] = new FoxThread(multiplier1, multiplier2, product, i, j, blockSize, blocksSqrt);
                }
            }

            Matrix cMatrix = null;

            try {
                for (int i = 0; i < blocksSqrt; i++) {
                    for (int j = 0; j < blocksSqrt; j++) {
                        tArray[i][j].start();
                    }
                }

                for (int i = 0; i < blocksSqrt; i++) {
                    for (int j = 0; j < blocksSqrt; j++) {
                        tArray[i][j].join();
                    }
                }
            } catch (InterruptedException ex) {
                System.err.println("Помилка в роботі потоків, назапланована зупинка програми.");
                System.exit(0);
            } finally {
                cMatrix = new Matrix(product);
            }

            return cMatrix;
        }
    }
}
