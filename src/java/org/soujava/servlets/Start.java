/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.soujava.servlets;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.soujava.generator.SequenceGenerator;

/**
 *
 * @author thiagogonzaga
 */
@WebServlet("/start")
public class Start extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(true);
        SequenceGenerator sc;
        final GpioController gpio;

        if (session.isNew()) {
            gpio = GpioFactory.getInstance();
            final GpioPinDigitalOutput[] leds = new GpioPinDigitalOutput[3];
            leds[0] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "Red", PinState.LOW);
            leds[1] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "Green", PinState.LOW);
            leds[2] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "Blue", PinState.LOW);
            sc = new SequenceGenerator(leds);
            session.setAttribute("leds", leds);
            session.setAttribute("gpio", gpio);
            session.setAttribute("sc", sc);
        } else {
            sc = new SequenceGenerator((GpioPinDigitalOutput[]) session.getAttribute("leds"));
        }
        sc.playNextSeq();
        session.setAttribute("count", 1);
        resp.getWriter().write(session.getId());
    }
}
