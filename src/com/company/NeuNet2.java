package com.company;

import org.ejml.simple.SimpleMatrix;

import java.util.Random;


class NeuNet2 {
    private final static double A = 0.1;
    private final static double LR = 0.1;

    private int hiddenLayers;

    private SimpleMatrix[] w;
    private SimpleMatrix[] prevW;
    private SimpleMatrix[] b;

    NeuNet2(int inputNodes, int[] hiddenLeyer, int outputNodes) {
        hiddenLayers = hiddenLeyer.length;

        Random random = new Random();
        int numWeights = hiddenLayers + 1;
        w = new SimpleMatrix[numWeights];
        prevW = new SimpleMatrix[numWeights];
        w[0] = SimpleMatrix.random_DDRM(hiddenLeyer[0], inputNodes, -1, 1, random);
        prevW[0] = new SimpleMatrix(w[0]);
        for (int i = 1; i < numWeights - 1; ++i) {
            w[i] = SimpleMatrix.random_DDRM(hiddenLeyer[i], hiddenLeyer[i - 1], -1, 1, random);
            prevW[i] = new SimpleMatrix(w[i]);
        }
        w[numWeights - 1] = SimpleMatrix.random_DDRM(outputNodes, hiddenLeyer[numWeights - 2], -1, 1, random);
        prevW[numWeights - 1] = new SimpleMatrix(w[numWeights - 1]);

        b = new SimpleMatrix[numWeights];
        for (int i = 0; i < numWeights - 1; ++i) {
            b[i] = SimpleMatrix.random_DDRM(hiddenLeyer[i], 1, -1, 1, random);
        }

        b[numWeights - 1] = SimpleMatrix.random_DDRM(outputNodes, 1, -1, 1, random);
    }

    double[] run(double[] inputs, double[] targets) {
        int numData = hiddenLayers + 2;

        SimpleMatrix[] data = new SimpleMatrix[numData];
        double[][] inputArray = new double[1][];
        inputArray[0] = inputs;
        data[0] = new SimpleMatrix(inputArray).transpose();

        for (int i = 1; i < numData; ++i) {
            data[i] = w[i - 1].mult(data[i - 1]).plus(b[i - 1]);
            sigmoid(data[i]);
        }

        return toArray(data[numData - 1]);
    }

    void train(double[] inputs, double[] targets) {
        int numData = hiddenLayers + 2;
        int numErrors = hiddenLayers + 1;

        SimpleMatrix[] data = new SimpleMatrix[numData];
        double[][] inputArray = new double[1][];
        inputArray[0] = inputs;
        data[0] = new SimpleMatrix(inputArray).transpose();

        for (int i = 1; i < numData; ++i) {
            data[i] = w[i - 1].mult(data[i - 1]).plus(b[i - 1]);
            sigmoid(data[i]);
        }

        double[][] targetArray = new double[1][];
        targetArray[0] = targets;
        SimpleMatrix targetMatrix = new SimpleMatrix(targetArray).transpose();

        SimpleMatrix[] errors = new SimpleMatrix[numErrors];
        errors[0] = data[numData - 1].minus(targetMatrix).elementMult(pSigmoid(data[numData - 1]));

        for (int i = 1; i < numErrors; ++i) {
            errors[i] = w[numErrors - i].transpose().mult(errors[i - 1]).elementMult(pSigmoid(data[numData - 1 - i]));
        }

        SimpleMatrix deltaWeights;
        for (int i = 0; i < numErrors; ++i) {
            deltaWeights = new SimpleMatrix(w[numErrors - 1 - i]).minus(prevW[numErrors - 1 - i]);
            prevW[numErrors - 1 - i] = w[numErrors - 1 - i];

            w[numErrors - 1 - i] = w[numErrors - 1 - i].minus(errors[i].scale(LR).mult(data[numData - 2 - i].transpose()))
                    .plus(deltaWeights.scale(A));
            b[numErrors - 1 - i] = b[numErrors - 1 - i].minus(errors[i].scale(LR));
        }
    }

    private double[] toArray(SimpleMatrix m) {
        double[] array = new double[m.getNumElements()];
        for (int i = 0; i < m.getNumElements(); ++i) {
            array[i] = m.get(i);
        }
        return array;
    }

    private void sigmoid(SimpleMatrix m) {
        for (int i = 0; i < m.getNumElements(); ++i) {
            double x = m.get(i);
            m.set(i, 1 / (1 + Math.exp(-x)));
        }
    }

    private SimpleMatrix pSigmoid(SimpleMatrix m) {
        SimpleMatrix newM = new SimpleMatrix(m);
        for (int i = 0; i < newM.getNumElements(); ++i) {
            double x = newM.get(i);
            newM.set(i, x * (1 - x));
        }
        return newM;
    }
}
