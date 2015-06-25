package com.arquivolivre.generator.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

public class GeniusController {

    public GeniusController() {
    }

    @GET
    @Path("/select/color/{color}")
    public void setColor(@PathParam("color") Color color) {
        
    }

}
