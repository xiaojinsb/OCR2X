package com.ocr2x.controller;

import com.ggasoftware.imago.Imago;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

@CrossOrigin
@RestController
public class ocr2xController {

    Imago imago = new Imago();

    @PostMapping(value = "/ocr2x")
    public void OCR2X(HttpServletRequest request, @RequestParam Map<String, Object> reqMap) {
        String outMessage = "";

        String picdata = (String) reqMap.get("picdata");
        System.out.println(picdata);

        BASE64Decoder decoder = new BASE64Decoder();
        byte[] picbyte = {};
        String fileName = new Date().getTime() + ".png";
        String upPath = request.getServletContext().getRealPath("/upload");

        try{
            picbyte = decoder.decodeBuffer(picdata);
            FileOutputStream fos = new FileOutputStream(upPath + "/" + fileName);
            fos.write(picbyte);
            fos.flush();
            fos.close();

            imago.loadImage(upPath + "/" + fileName);
            imago.filterImage();
            imago.recognize();
            System.out.println(imago.getResultMolecule());
        }catch(IOException e){
            System.out.println(e);
        }
    }

}
