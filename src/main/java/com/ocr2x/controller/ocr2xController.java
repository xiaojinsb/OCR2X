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
    public R OCR2X(@RequestParam Map<String, Object> reqMap) {
        String outMessage = "";

        String picdata = (String) reqMap.get("picdata");

        BASE64Decoder decoder = new BASE64Decoder();
        byte[] picbyte = {};
        String fileName = new Date().getTime() + ".png";
        String upPath = "C:/Users/Avalon/Documents/upload/img/"+fileName;
        String mol = "";

        try {
            picbyte = decoder.decodeBuffer(picdata);
            FileOutputStream fos = new FileOutputStream(upPath);
            fos.write(picbyte);
            fos.flush();
            fos.close();

            //ocr识别
            imago.loadImage(upPath);
            imago.filterImage();
            imago.recognize();
            mol = imago.getResultMolecule();
        } catch (IOException e) {
            System.out.println(e);
        }


        return R.ok().put("img2x",fileName).put("mol",mol);
    }

}
