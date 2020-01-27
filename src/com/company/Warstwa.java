package com.company;

import java.util.Arrays;
import java.util.Random;

public class Warstwa {

    private float[] input;
    private float[] output;
    private float[] weights;
    private float[] dWeights;

    public Warstwa(int inputSize, int outputSize){
        this.output = new float[outputSize];
        this.input = new float[inputSize+1]; // + 1 baias
        this.weights = new float[(inputSize+1) * outputSize];
        this.dWeights = new float[weights.length];
        losujWagi();
    }

    public float[] getOutput() {
        return output;
    }

    private void losujWagi() {
        Random rand = new Random();
        for(int i = 0; i < weights.length; i++){
            weights[i] = (rand.nextFloat() * 2f) - 1f;
        }
    }

    public float[] run(float[] inputArray) {
        System.arraycopy(inputArray, 0, input, 0, inputArray.length);

        input[input.length-1] = 1;
        int offset = 0;
        for(int i = 0; i < output.length; i++){
            for(int j = 0; j < input.length; j++){
                output[i] += weights[offset+j] * input[j];
            }
            output[i] = sigmoid(output[i]);
//            System.out.println(output[i]);
            offset += input.length;
        }
        return Arrays.copyOf(output, output.length);
    }

    float sigmoid(float s){
        return (float)(1/(1+Math.exp(-s)));
    }

    float pSigmoid(float s){
        return (float)(s * (1-s));
    }

    public float[] train(float[] error, float learningRate, float alfa) {
        int offset = 0;
        float[] nextError = new float[input.length];

        for(int i = 0; i < output.length; i++){
            float delta = error[i] * pSigmoid(output[i]);
            for(int j = 0; j < input.length; j++){
                int weightIndex = offset + j;
                nextError[j] += weights[weightIndex] * delta;
                float dw = input[j] * delta * learningRate;
                weights[weightIndex] += dWeights[weightIndex] * alfa + dw;
                dWeights[weightIndex] = dw;
            }
            offset += input.length;
        }
        return nextError;
    }
}
