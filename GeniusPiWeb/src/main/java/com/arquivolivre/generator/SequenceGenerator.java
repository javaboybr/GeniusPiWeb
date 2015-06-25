package com.arquivolivre.generator;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author thiagogonzaga
 */
public class SequenceGenerator {

    protected ArrayList<Character> sequence;
    protected GpioPinDigitalOutput[] leds;
    public int nextCount;

    public SequenceGenerator(GpioPinDigitalOutput[] leds) {
        this.leds = leds;
        this.sequence = newSequence();
        nextCount = 0;
    }

    public char next() {
        Random rand = new Random(System.currentTimeMillis());
        int number = rand.nextInt(4);
        switch (number) {
            case 1:
                return 'R';
            case 2:
                return 'G';
            case 3:
                return 'B';
        }
        return ' ';
    }

    private ArrayList<Character> newSequence() {
        nextCount = 1;
        return new ArrayList<>();
    }

    //verifica se o campo digitado está correto
    public boolean check(int index, char color) {
        return sequence.get(index) == color;
    }

    public void generate() {
        char n = next();
        while (n == ' ') {
            n = next();
        }
        play(n);
        sequence.add(n);
    }

    public void play(char color) {
        switch (color) {
            case 'R':
                leds[0].pulse(400, true);
                break;
            case 'G':
                leds[1].pulse(400, true);
                break;
            case 'B':
                leds[2].pulse(400, true);
                break;
        }
        try {
            Thread.sleep(150);
        } catch (InterruptedException ex) {
            Logger.getLogger(SequenceGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void playNextSeq() {
        for (int i = 0; i < sequence.size(); i++) {
            play(sequence.get(i));
        }
        generate();
        nextCount++;
    }

}
