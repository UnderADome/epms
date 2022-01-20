layui.define(['layer','jquery','wangEditor'], function (exports) {
    var $ = layui.jquery,
    layer = layui.layer,
    wangEditor = layui.wangEditor;

    //表示整个js模块，在最后exports的时候包装一下名称
    var obj = {
        //表示函数的名称
        /**
         * 用于初始化wangEditor
         * @param mContent 渲染的数据
         * @param dom  需要被渲染的dom，格式：$('#dom')
         */
        initEditor : function(mContent, dom){
                var editor = new wangEditor('#editor');
                //这里默认为true，即表示能够使用网络图片
                editor.customConfig.showLinkImg = false;

                //过滤掉复制文本的样式  此处关闭了该功能
                editor.customConfig.pasteFilterStyle = false;

                //自定义上传
                editor.customConfig.customUploadImg = async  (files, insert) => {
                    var daw = new FormData();
                    for (var i = 0; i < files.length; i++) {
                        daw.append("pictures", files[i]);
                        console.log(files[i].name)
                    }
                    console.log("打印FormData  " + daw.toString());
                    for (var [a, b] of daw.entries()) {
                        console.log(a, b);
                    }
                    index = layer.load(1, {
                        shade: [0.1, '#fff'] //0.1透明度的白色背景
                    });

                    $.ajax({
                        url: "wangEditor/submitMulti",
                        type: "POST",
                        data: daw,
                        async: false,
                        cache: false,
                        contentType: false,
                        processData: false,
                        success: function (result) {
                            layer.close(index);
                            if (result.code == 0) {
                                for (var j = 0; j < result.data.length; j++) {
                                    insert(result.data[j]);
                                }
                            } else {
                                layer.msg(da.msg);
                                return;
                            }
                        },
                    });
                };
                editor.customConfig.customAlert = function (info) {
                    layer.msg(info);
                }

                editor.customConfig.onchange = function (html) {
                    console.log("变化：" + html);
                    dom.val(html);
                }

                editor.create();
                editor.txt.clear();
                editor.txt.html(mContent);

                dom.text(mContent);
        }
    }
    exports("epmsEditor",obj);
})


