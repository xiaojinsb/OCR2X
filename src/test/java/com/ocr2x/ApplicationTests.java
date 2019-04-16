package com.ocr2x;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    @Test
    public void contextLoads() {

        try {
            Process p = Runtime.getRuntime().exec("C:\\Users\\Avalon\\Downloads\\waifu2x\\waifu2x --model-dir \"C:\\Users\\Avalon\\Downloads\\waifu2x\\models_rgb\" --scale-ratio \"2.0\" --noise-level \"3\" -i \"C:\\Users\\Avalon\\Pictures\\1.png\"");
            InputStream is = p.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"gb2312"));
            String line;
            while((line = reader.readLine())!= null){
                System.out.println(line);
            }
            is.close();
            reader.close();
            p.destroy();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
