package com.nulfy.athora;

import com.nulfy.athora.assets.AthoraAssets;
import org.json.simple.parser.ParseException;

import java.io.IOException;

import static java.lang.System.out;

public class AthoraMain {

    public static void main(String[] args) throws IOException, ParseException {

        out.println(AthoraAssets.welcome);

        AthoraLogic.AwaitMovement();

    }

}