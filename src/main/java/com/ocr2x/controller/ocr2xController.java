package com.ocr2x.controller;

import com.epam.indigo.Indigo;
import com.epam.indigo.IndigoObject;
import com.ggasoftware.imago.Imago;
import com.ocr2x.util.R;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

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

        //获取传过来的图片BASE64
        String picdata = (String) reqMap.get("picdata");
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] picbyte = {};

        //地址
        //文件名
        Long fileName = new Date().getTime();
        //原始图片和放大图片存储位置
        String imgFile = "C:\\Users\\Avalon\\Desktop\\upload\\img\\" + fileName + ".png";
        String img2xFile = "C:\\Users\\Avalon\\Desktop\\upload\\img2x\\" + fileName;

        //waifu2x 和 运行模块 位置
        String waifu2xPath = "C:\\waifu2x\\waifu2x.exe";
        String modelsPath = "\"C:\\waifu2x\\models_rgb\"";

        //cmd运行命令 waifu2x地址 模块地址 放大2倍 降噪3 输出放大图片  输入原始图片
        String strCmd = waifu2xPath + " --model-dir " + modelsPath + " --scale-ratio \"2.0\" --noise-level \"3\"  -o \"" + img2xFile + "\" -i \"" + imgFile + "\"";
        //mol编码
        String mol = "";

        try {

            //BASE64转图片
            picbyte = decoder.decodeBuffer(picdata);
            //保存原始图片
            FileOutputStream fos = new FileOutputStream(imgFile);
            fos.write(picbyte);
            fos.flush();
            fos.close();

            //放大原始图片
            Process p = Runtime.getRuntime().exec(strCmd);
            InputStream is = p.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "gb2312"));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            is.close();
            reader.close();
            p.destroy();

            //ocr识别化学式
            //加载放大图片
            imago.loadImage(img2xFile + ".png");
            //锅炉
            imago.filterImage();
            //识别
            imago.recognize();
            //转为mol编码
            mol = imago.getResultMolecule();
        } catch (IOException e) {
            System.out.println(e);
        }


        return R.ok().put("img2x", fileName + ".png").put("mol", mol);
    }

    //如果没有info方法 不会显示 高级功能按钮
    @GetMapping("/info")
    public R info() {
        return R.ok().put("indigo_version", indigo.version()).put("imago_versions", imago.getVersion());
    }

    //整理化学式
    @PostMapping("/layout")
    public R layout(@RequestBody Map<String, Object> reqMap) {
        IndigoObject mol = indigo.loadMolecule((String) reqMap.get("struct"));
        mol.layout();
        return R.ok().put("struct", mol.molfile());
    }

}
