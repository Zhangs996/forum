$(function(){//页面加载完要加载这个函数
    $("#uploadForm").submit(upload);//当点击提交按钮，触发表单的提交时，执行upload函数，也就是下面写的函数
});

function upload() {
    //$.POST是对$.ajax一个简化
    $.ajax({
        url: "http://up-z2.qiniup.com",
        method: "post",//请求参数
        processData: false,//不要把表单内容转给字符串
        contentType: false,//不让jquery设置上传的类型，浏览器会自动设置
        data: new FormData($("#uploadForm")[0]),//浏览器向服务器发的对象，就是uploadForm里面的数据。数组的第一个值，其实数组里只有一个值
        success: function(data) {//七牛云服务器返回给浏览器的数据
            if(data && data.code == 0) {
                // 更新头像访问路径
                $.post(
                    CONTEXT_PATH + "/user/header/url",//传给controller的某个路径
                    {"fileName":$("input[name='key']").val()},//得到setting.html的hidden，就是从表单里取到的
                    function(data) {//我们的springboot服务器端返回给浏览器的字符串
                        data = $.parseJSON(data);//解析成json
                        if(data.code == 0) {
                            window.location.reload();//刷新当前页面
                        } else {
                            alert(data.msg);
                        }
                    }
                );
            } else {
                alert("上传失败!");
            }
        }
    });
    return false;
}