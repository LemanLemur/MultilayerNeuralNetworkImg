package com.company;

import java.util.Random;

public class NeuralNetwork {
    double[][][] network;
    double[][][] errorsNetwork;
    double[][] leyErr;
    double[][] output;
    double[][] biasErr;
    double[][] bias;

    public NeuralNetwork(int iloscWejsc, int iloscWarstw, int iloscNuronow, int iloscWyjsc) {
        Random rand = new Random();

        network = new double[iloscWarstw][][];
        network[0] = new double[iloscNuronow][iloscWejsc];
        network[network.length - 1] = new double[iloscNuronow][iloscWyjsc];

        errorsNetwork = new double[iloscWarstw][][];
        errorsNetwork[0] = new double[iloscNuronow][iloscWejsc];
        errorsNetwork[errorsNetwork.length - 1] = new double[iloscNuronow][iloscWyjsc];

        bias = new double[network.length][];
        bias[0] = new double[iloscNuronow];
        bias[network.length - 1] = new double[iloscWyjsc];

        biasErr = new double[network.length][];
        biasErr[0] = new double[iloscNuronow];
        biasErr[network.length - 1] = new double[iloscWyjsc];

        output = new double[network.length][];
        output[0] = new double[iloscNuronow];
        output[network.length - 1] = new double[iloscWyjsc];

        leyErr = new double[network.length][];
        leyErr[0] = new double[iloscNuronow];
        leyErr[network.length - 1] = new double[iloscWyjsc];

        for (int i = 1; i < network.length - 1; i++) {
            leyErr[i] = new double[iloscNuronow];
            output[i] = new double[iloscNuronow];
            biasErr[i] = new double[iloscNuronow];
            bias[i] = new double[iloscNuronow];
            errorsNetwork[i] = new double[iloscNuronow][iloscNuronow];
            network[i] = new double[iloscNuronow][iloscNuronow];
        }

        for (int i = 0; i < network.length; i++) {
            for (int j = 0; j < network[i].length; j++) {
                for (int k = 0; k < network[i][j].length; k++) {
                    network[i][j][k] = rand.nextDouble() * 2.0 - 1;
                }
                bias[i][j] = rand.nextDouble() * 2.0 - 1;
            }
        }
    }

    void calculateErr(double[] inputs, double[] target){

        double sum=0;

        for(int i=0; i<inputs.length; i++){
            for(int j=0; j<network[0].length; j++){
                network[0][j][i]*=inputs[i]*network[0][j][i];
            }
        }

        System.out.println(network[0].length);
        //Mnożenie Baias
//        for(int i=0; i<network[0].length; i++) {
//            for (int j = 0; j < network[0][i].length; j++) {
//                network[0][j][i] *= bias[0][j] * network[0][i][j];
//            }
//        }

        for(int j=0; j<network[0].length;j++){
            sum=0;
            for(int i=0; i<network[0][j].length; i++){
                sum+=network[0][j][i];
                System.out.println("suma  "+sum);
            }
            output[0][j] = sigmoid(sum);
            System.out.println("sigmoid "+sigmoid(sum));
            System.out.println("output"+j+" "+output[0][j]);
        }

        for(int i=0; i<output[0].length; i++){
            for(int j=0; j<network[1].length; j++){
                network[1][j][i]*=output[0][i]*network[1][j][i];
            }
        }

        for(int j=0; j<network[1].length;j++){
            sum=0;
            for(int i=0; i<network[1][j].length; i++){
                sum+=network[1][j][i];
                System.out.println("2suma  "+sum);
            }
            output[0][j] = sigmoid(sum);
            System.out.println("2sigmoid "+sigmoid(sum));
            System.out.println("2output"+j+" "+output[0][j]);
        }

    }

    double sigmoid(double s){
        return 1/(1+Math.exp(-s));
    }

    void learn(double[] inputs, double[] targets){
        calculateErr(inputs, targets);
        double alfa = 0.1;

        for (int i = 0; i < errorsNetwork.length; i++) {
            for (int j = 0; j < errorsNetwork[i].length; j++) {
                bias[i][j] -= bias[i][j] * alfa;
                for (int k = 0; k < errorsNetwork[i][j].length; k++) {
                    network[i][j][k] -= errorsNetwork[i][j][k] * alfa;
                }
            }
        }

    }

}
