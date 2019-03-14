## 环境

[牛牛截图](http://www.ggniu.cn/index.html)：截取化学式

[waifu2x](https://github.com/nagadomi/waifu2x)：对截取的化学式放大

[Imago](http://lifescience.opensource.epam.com/imago/index.html)：OCR化学式

[ketcher](http://lifescience.opensource.epam.com/ketcher/index.html)：化学式编辑器

## 说明

加载一篇有化学式的文章(html)，截取化学式，自动使用waifu2x放大和Imago OCR识别，之后弹出窗口人工矫正提交。

## 流程

1. 截取化学式图片
2. 图片上传到服务器
3. 调用waifu2x放大化学式
4. Imago OCR识别化学式
5. 返回Molfiles
6. Molfiles加载到ketcher
7. 弹出窗口 人工矫正


