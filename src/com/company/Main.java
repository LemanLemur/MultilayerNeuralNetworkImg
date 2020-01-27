package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Main {

    public static BufferedImage img;
    public static BufferedImage imgOut;
    public static String path = "C:\\Users\\Lemur\\IdeaProjects\\Neural Network\\src\\com\\img\\nic.jpg";
    public static double[][][] trainingResultsImg;
    public static double[][][] trainingDataImg;
    public static float[][][] trainingResultsImgFloat;
    public static float[][][] trainingDataImgFloat;
    private final static int[] hiddenLeyer = {40, 40};
    static JPanel outPanel;
    static JLabel ITER;
    static int iter=0;

    public static NeuNet2 neuralNetwork = new NeuNet2(2, hiddenLeyer, 3);
    public static NeuNet siec = new NeuNet(2, 2, 40, 3);

    public static void main(String[] args) {
        przygotujDane();
        createWindow();
//
//
//        float[][] trainingData = new float[][]{
//                new float[]{0, 0},
//                new float[]{0, 1},
//                new float[]{1, 0},
//                new float[]{1, 1},
//        };
//        float[][] trainingResultsXOR = new float[][]{new float[]{193/255f, 102/255f, 196/255f}, new float[]{100/255f, 100/255f, 100/255f}, new float[]{192/255f, 102/255f,102/255f}, new float[]{255/255f, 255/255f, 255/255f}}; //XOR
//        for(int iteration = 0; iteration < 1000; iteration++){
//            for(int i = 0; i < trainingResultsXOR.length; i++){
//                siec.train(trainingData[i], trainingResultsXOR[i], 0.4f,0.6f);
//            }
//            System.out.println();
//            for(int i = 0; i < trainingResultsXOR.length; i++){
//                float[] t = trainingData[i];
////                float[] outputs = siec.run(t);
////                System.out.println("Num of iterations: " + (iteration + 1));
////                System.out.printf("%.1f, %.1f --> %.3f\n", t[0], t[1], siec.run(t)[0]);
//                System.out.println("x: "+t[0]+" y: "+t[1]+ "|| r--> "+ siec.run(t)[0]*255+" g--> "+ siec.run(t)[1]*255+" b--> "+ siec.run(t)[2]*255);
//            }
//            for(int i=0; i<trainingResultsXOR.length; i++){
//                System.out.printf("\nprzykład %d\n red %.2f green %.2f blue %.2f",i,trainingResultsXOR[i][0]*255, trainingResultsXOR[i][1]*255, trainingResultsXOR[i][2]*255);
//            }
//        }

//        for (int i = 0; i < img.getWidth(); i++) {
//            for (int j = 0; j < img.getHeight(); j++) {
//                int pos = i * img.getWidth() + j;
//                siec.train(trainingDataImg[pos], trainingResultsImg[pos], 0.3f, 0.6f);
//            }
//        }
//        for (int i = 0; i < img.getWidth(); i++) {
//            for (int j = 0; j < img.getHeight(); j++) {
//                int pos = i * img.getWidth() + j;
//                float[] t = trainingDataImg[pos];
//                System.out.println("x: "+t[0]+" y: "+t[1]+ "r "+ siec.run(t)[0]+" g"+ siec.run(t)[1]+" b "+ siec.run(t)[2]);
//            }
//        }
    }

    public static void rysuj() {

        Random rand = new Random();

        for (
                int iteration = 0;
                iteration < 100000; iteration++) {

            int i = rand.nextInt(imgOut.getWidth());
            int j = rand.nextInt(imgOut.getHeight());
//                    int pos = i * img.getWidth() + j;
            neuralNetwork.train(trainingDataImg[i][j], trainingResultsImg[i][j]);

        }
        iter += 100000;
        ITER.setText("Iteracje: "+iter);

        for (
                int i = 0; i < imgOut.getWidth(); i++) {
            for (int j = 0; j < imgOut.getHeight(); j++) {
//                    int pos = i * imgOut.getWidth() + j;
                double[] outputs = neuralNetwork.run(trainingDataImg[i][j], trainingResultsImg[i][j]);
                int red = (int) Math.floor(outputs[0] * 255);
                int green = (int) Math.floor(outputs[1] * 255);
                int blue = (int) Math.floor(outputs[2] * 255);
//                    System.out.println("x "+i+" y "+j+" r "+ outputs[0]* 255+" g "+ outputs[1]* 255+" b "+ outputs[2]* 255);
//                    System.out.println(new Color(red, green, blue).getRGB());
                imgOut.setRGB(i, j, new Color(red, green, blue).getRGB());
            }
        }
        outPanel.repaint();
//        System.out.println(new Color(249, 249, 249).getRGB());
    }


    public static void rysuj2() {

        Random rand = new Random();
        for (int iteration = 0; iteration < 10000; iteration++) {

            int i = rand.nextInt(imgOut.getWidth());
            int j = rand.nextInt(imgOut.getHeight());
//                    int pos = i * img.getWidth() + j;
            siec.train(trainingDataImgFloat[i][j], trainingResultsImgFloat[i][j], 0.1f, 0.1f);

        }
        iter += 100000;
        ITER.setText("Iteracje: "+iter);

        for (int i = 0; i < imgOut.getWidth(); i++) {
            for (int j = 0; j < imgOut.getHeight(); j++) {
//                    int pos = i * imgOut.getWidth() + j;
                float[] outputs = siec.run(trainingDataImgFloat[i][j]);
                int red = (int) Math.floor(outputs[0] * 255);
                int green = (int) Math.floor(outputs[1] * 255);
                int blue = (int) Math.floor(outputs[2] * 255);
//                    System.out.println("x "+i+" y "+j+" r "+ outputs[0]* 255+" g "+ outputs[1]* 255+" b "+ outputs[2]* 255);
//                    System.out.println(new Color(red, green, blue).getRGB());
                imgOut.setRGB(i, j, new Color(red, green, blue).getRGB());
            }
        }
        outPanel.repaint();
//        System.out.println(new Color(249, 249, 249).getRGB());
    }

    public static void przygotujDane() {

        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        trainingDataImg = new double[img.getWidth()][img.getHeight()][2];
        trainingResultsImg = new double[img.getWidth()][img.getHeight()][3];
        trainingDataImgFloat = new float[img.getWidth()][img.getHeight()][2];
        trainingResultsImgFloat = new float[img.getWidth()][img.getHeight()][3];

        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                Color pixelColor = new Color(img.getRGB(i, j));
                int red = pixelColor.getRed();
                int green = pixelColor.getGreen();
                int blue = pixelColor.getBlue();
//                int pos = i * img.getWidth() + j;

                trainingDataImg[i][j][0] = i / (double) img.getWidth();
                trainingDataImg[i][j][1] = j / (double) img.getHeight();

                trainingDataImgFloat[i][j][0] = i / (float) img.getWidth();
                trainingDataImgFloat[i][j][1] = j / (float) img.getHeight();

//                System.out.println("red: "+red/255f+" green: "+green/255f+" blue: "+blue/255f);

                trainingResultsImg[i][j][0] = red / 255.0f;
                trainingResultsImg[i][j][1] = green / 255.0f;
                trainingResultsImg[i][j][2] = blue / 255.0f;
                trainingResultsImgFloat[i][j][0] = red / 255.0f;
                trainingResultsImgFloat[i][j][1] = green / 255.0f;
                trainingResultsImgFloat[i][j][2] = blue / 255.0f;
//
//                double[] inputs = new double[22];
//
//                int index = 2;
//
//                for (int k = 1; k < 11; k++) {
//                    inputs[index] = Math.sin(k * i * 2 * Math.PI);
//                    index++;
//                }
//
//                for (int k = 1; k < 11; k++) {
//                    inputs[index] = Math.sin(k * j * 2 * Math.PI);
//                    index++;
//                }
//                inputs[0] = (i * 1.0 / img.getHeight()) * 2.0 - 1.0;
//                inputs[1] = (j * 1.0 / img.getWidth()) * 2.0 - 1.0;

//                tmpDataSet.add(new Data(inputs, new double[]{red, green, blue}, 1, 255));
            }
        }

    }

    private static void createWindow() {
        imgOut = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        JFrame jFrame = new JFrame("Programowanko");

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLayout(new CardLayout());
        jFrame.setMinimumSize(new Dimension(500, 500));
        jFrame.setLayout(new GridLayout(2, 2));
        ITER = new JLabel("Iteracje: 0");

        JButton button = new JButton("Start");
        button.setMaximumSize(new Dimension(100, 50));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                rysuj();
            }
        });
        outPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(imgOut, 0, 0, imgOut.getWidth(), imgOut.getHeight(), this);
            }
        };
        outPanel.setPreferredSize(new Dimension(imgOut.getWidth(), imgOut.getHeight()));

        jFrame.add(button);
        jFrame.add(ITER);
        jFrame.add(outPanel);
        jFrame.pack();
        jFrame.setVisible(true);
    }
}
