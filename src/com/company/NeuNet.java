package com.company;

public class NeuNet {
    private Warstwa[] warstwy;

    public NeuNet (int inputSize,int hiddenLeyerSize, int hiddenSize, int outputSize){
        warstwy = new Warstwa[1+hiddenLeyerSize];
        if(hiddenLeyerSize < 2) {
            warstwy[0] = new Warstwa(inputSize, hiddenSize);
            warstwy[1] = new Warstwa(hiddenSize, outputSize);
        }else{
            int i=0;
            warstwy[i] = new Warstwa(inputSize, hiddenSize);
            i++;
            for(int j=0; j<hiddenLeyerSize-1; j++){
                warstwy[i] = new Warstwa(hiddenSize, hiddenSize);
                i++;
            }
            warstwy[i] = new Warstwa(hiddenSize, outputSize);
        }
    }

    public float[] getOutput(){
        return warstwy[warstwy.length-1].getOutput();
    }

    public float[] run(float[] input){
        float[] activations = input;
        for(int i = 0; i<warstwy.length; i++){
            activations = warstwy[i].run(activations);
        }
//        System.out.println(warstwy.length);
        return activations;
    }

    public void train(float[] input, float[] targetOutput, float learningRate, float alfa){
        float[] calculatedOutput = run(input);

//        System.out.println(input[0]+" "+input[1]);
        float[] error = new float[calculatedOutput.length];

        for(int i = 0; i < error.length; i++){
            error[i] = targetOutput[i] - calculatedOutput[i];
//            System.out.printf("błąd %.3f --> target %.3f - output %.3f\n",error[i], targetOutput[i],calculatedOutput[i]);
        }
        for(int i = warstwy.length-1; i >= 0; i--){
            error = warstwy[i].train(error, learningRate, alfa);
        }
    }
}