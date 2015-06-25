/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arquivolivre.servlets;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.arquivolivre.generator.SequenceGenerator;

/**
 *
 * @author thiagogonzaga
 */
@WebServlet("/green")
public class Green extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        GpioController gpio = (GpioController) session.getAttribute("gpio");
        GpioPinDigitalOutput[] leds = (GpioPinDigitalOutput[]) session.getAttribute("leds");
        SequenceGenerator sc = (SequenceGenerator) session.getAttribute("sc");
        Integer count = (Integer) session.getAttribute("count");
        sc.play('G');
        if (count < sc.nextCount) {
            if (sc.check(count - 1, 'G')) {
                count++;
                session.setAttribute("count", count);
                resp.getWriter().print("OK");
            } else {
                sc = new SequenceGenerator(leds);
                session.setAttribute("sc", sc);
                session.setAttribute("count", 0);
                leds[0].toggle();
                leds[1].toggle();
                leds[2].toggle();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Green.class.getName()).log(Level.SEVERE, null, ex);
                }
                leds[0].toggle();
                leds[1].toggle();
                leds[2].toggle();
                resp.getWriter().print("FAIL");
            }

        } else {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(Red.class.getName()).log(Level.SEVERE, null, ex);
            }
            sc.playNextSeq();
            session.setAttribute("count", 1);
            resp.getWriter().print("NEW");
        }
    }

}
