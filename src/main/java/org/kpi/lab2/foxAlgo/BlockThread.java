package org.kpi.lab2.foxAlgo;

import org.kpi.lab2.matrix.Matrix;

public class BlockThread extends Thread{
    Matrix A;
    Matrix B;
    Matrix C;
    private int start;          // number of counter beginning in current sub task
    private int finish;         // number for next sub task, finish

    public BlockThread(Matrix A, Matrix B, Matrix C, int start, int finish) {
        this.A = A;
        this.B = B;
        this.C = C;
        this.start = start;
        this.finish = finish;
    }

    @Override
    public void run() {
        // For each string sub task ai*
        for(int i = this.start; i < this.finish; i++) {
            // For each colon of matrix B b*j
            for(int j = 0; j < this.B.getCols(); j++) {
                // For each element in colon matrix B bkj
                for(int k = 0; k < this.B.getRows(); k++) {
                    int value = C.getElement(i, j) + A.getElement(i, k) * B.getElement(k, j);
                    C.setElement(i, j, value);
                }
            }
        }
    }
}
