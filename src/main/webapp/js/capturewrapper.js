var captureObj = null;

//用于初始化牛牛截图控件，此函数需要在页面加载完成后立即调用
function Init() {
    captureObj = new NiuniuCaptureObject();
    captureObj.NiuniuAuthKey = "moe";
    //此处可以设置相关参数
    captureObj.TrackColor = rgb2value(0, 174, 255);
    captureObj.EditBorderColor = rgb2value(255, 174, 0);

    //设置控件加载完成以及截图完成的回调函数
    captureObj.FinishedCallback = OnCaptureFinishedCallback;
    captureObj.PluginLoadedCallback = PluginLoadedCallback;
    captureObj.VersionCallback = VersionCallback;

    //初始化控件
    captureObj.InitNiuniuCapture();
}

// 截图入口函数
function Capture() {
    var defaultName = "1.png"; //图片格式
    return captureObj.DoCapture("1.png", 0, 0, 0, 0, 0, 0);
}

//此处是截图后的回调函数，用于将截图的详细信息反馈回来，你需要调整此函数，完成图像数据的传输与显示
function OnCaptureFinishedCallback(type, x, y, width, height, info, content, localpath) {
    if (type < 0) {
        console.log("需要重新安装控件")
        return;
    }
    $('#show').hide();
    switch (type) {
        case 1: {
            console.log('截图成功： X:' + x + ',Y:' + y + ',宽:' + width + ',高:' + height);
            $.ajax({
                type: "POST",
                url: "http://localhost/ocr2x",
                dataType: "json",
                data: {
                    format: 'png'
                    ,picdata: content
                },
                success: function (obj) {},
                error : function(){}
            });
            break;
        }
        case 2: {
            console.log('取消截图');
            break;
        }
        case 3: {
            console.log('保存截图： X:' + x + ',Y:' + y + ',宽:' + width + ',高:' + height);
            break;
        }
    }
}

//当控件成功加载后回调的的函数，您可以在此控制相应的UI显示
function PluginLoadedCallback(success) {
}

//用于返回控件的版本号
function VersionCallback(ver) {
}