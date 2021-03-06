package org.kpi.lab2.lineAlgo;

import org.kpi.lab2.foxAlgo.BlockThread;
import org.kpi.lab2.matrix.Matrix;

import java.io.*;

public class StrapeOne {
    private static int currentIndex = -1;


    public static StrapeOne createMatrix(int rows, int cols, boolean isFromFile, File file) {
        Matrix matrix = new Matrix(rows, cols);
        if (isFromFile) {
            try {
                return new Matrix(readFile(file));
            } catch (Exception e) {
                System.out.println("Не вдалося зчитати матриці");
                System.exit(0);
            }
        }
        else {
            StrapeOne.writeInFile(matrix, file);
        }
        return matrix;
    }

    public static void writeInFile(Matrix matrix, File file) {
        try {
            writeFile(file, matrix);
            System.out.println("Матрицю записано у файл: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Матриця не записалась у файл " + file.getAbsolutePath());
        }
    }


    //  Робота з файлами
    private static void writeFile(File file, Matrix matrix) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(matrix.getRows() + "\t" + matrix.getCols() + "\t\n");
        fileWriter.flush();
        for (int i = 0; i < matrix.getRows(); i++) {
            for (int j = 0; j < matrix.getCols(); j++) {
                fileWriter.write(matrix.getElement(i, j) + "\t");
                fileWriter.flush();
            }
            fileWriter.write("\n");
            fileWriter.flush();
        }
    }

    public static int[][] readFile(File file) throws FileNotFoundException, IOException {
        FileInputStream inFile = new FileInputStream(file);
        byte[] str = new byte[inFile.available()];
        inFile.read(str);
        String text = new String(str);

        String[] numbers = text.split("\\D");
        int i, j;
        int n = next(numbers);
        int m = next(numbers);
        int[][] newMatrix = new int[n][m];

        for (i = 0; i < n; ++i)
            for (j = 0; j < m; ++j)
                newMatrix[i][j] = next(numbers);

        currentIndex = -1;
        return newMatrix;
    }

    private static Integer next(String numbers[]) {
        ++currentIndex;
        while (currentIndex < numbers.length
                && numbers[currentIndex].equals(""))
            ++currentIndex;
        return currentIndex < numbers.length ? Integer
                .parseInt(numbers[currentIndex]) : null;
    }

    public static void simpleMultiplying(Matrix A, Matrix B, Matrix C) {
        for (int i = 0; i < A.getRows(); i++) {
            for (int j = 0; j < B.getCols(); j++) {
                for (int k = 0; k < B.getRows(); k++) {
                    int value = C.getElement(i, j) + A.getElement(i, k) * B.getElement(k, j);
                    C.setElement(i, j, value);
                }
            }
        }
    }

    public static void blockMultiply(Matrix A, Matrix B, Matrix C, int proc) {
        int lengthOfSections = A.getRows() / proc;               // Кількість рядків на один потік
        Thread[] threads = new Thread[proc];                     // Масив потоків
        // кожному потоку передаємо його підзадачі
        for (int i = 0; i < threads.length; i++) {
            if (i < threads.length - 1) {
                threads[i] = new Thread(new BlockThread(A, B, C,
                        i * lengthOfSections, (i + 1) * lengthOfSections));
            } else {
                threads[i] = new Thread(new BlockThread(A, B, C,
                        i * lengthOfSections, C.getRows()));
            }
            threads[i].start();
        }

        // чекаємо завершення виконання потоків
        for (Thread th : threads) {
            try {
                th.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



}
