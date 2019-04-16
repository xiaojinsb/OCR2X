package com.ocr2x.controller;

import com.epam.indigo.Indigo;
import com.epam.indigo.IndigoObject;
import com.ggasoftware.imago.Imago;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Date;
import java.util.Map;

@CrossOrigin
@RestController
public class ocr2xController {

    Indigo indigo = new Indigo();
    Imago imago = new Imago();

    //ocr识别
    @PostMapping(value = "/ocr2x")
    public R OCR2X(@RequestParam Map<String, Object> reqMap) {
        String outMessage = "";

        String picdata = (String) reqMap.get("picdata");

        BASE64Decoder decoder = new BASE64Decoder();
        byte[] picbyte = {};

        Long fileName = new Date().getTime();
        String upPath = "C:\\Users\\Avalon\\Desktop\\upload\\img\\"+fileName+".png";
        String upPath2x = "C:\\Users\\Avalon\\Desktop\\upload\\img\\"+fileName+"_[NS-L3][x2.000000].png";
        String mol = "";

        String strCmd = "C:\\waifu2x\\waifu2x --model-dir \"C:\\waifu2x\\models_rgb\" --scale-ratio \"2.0\" --noise-level \"3\" -i \""+upPath+"\"";
        System.out.println(strCmd);
        try {
            picbyte = decoder.decodeBuffer(picdata);
            FileOutputStream fos = new FileOutputStream(upPath);
            fos.write(picbyte);
            fos.flush();
            fos.close();

            Process p = Runtime.getRuntime().exec(strCmd);
            InputStream is = p.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"gb2312"));
            String line;
            while((line = reader.readLine())!= null){
                System.out.println(line);
            }
            is.close();
            reader.close();
            p.destroy();

            //ocr识别
            imago.loadImage(upPath2x);
            imago.filterImage();
            imago.recognize();
            mol = imago.getResultMolecule();
        } catch (IOException e) {
            System.out.println(e);
        }


        return R.ok().put("img2x",fileName+".png").put("mol",mol);
    }

    //如果没有info方法 不会显示 高级功能按钮
    @GetMapping("/info")
    public R info() {
        return R.ok().put("indigo_version", indigo.version()).put("imago_versions", imago.getVersion());
    }

    //整理
    @PostMapping("/layout")
    public R layout(@RequestBody Map<String, Object> reqMap) {
        IndigoObject mol = indigo.loadMolecule((String) reqMap.get("struct"));
        mol.layout();
        return R.ok().put("struct", mol.molfile());
    }

}
