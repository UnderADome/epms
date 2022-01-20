layui.define(['form','jquery','laydate'], function (exports) {
    var $ = layui.jquery, layer = layui.layer, element = layui.element;
    var form = layui.form;

    var obj = {
        /**
         * 仅适用于form表单中提交按钮 lay-filter="submitAll" 的情况
         * 请检查，否则调不动该函数
         * @param action  表示请求路径
         * @param subPage 表示是否是子页面，1表示是，0表示不是；1 则关闭该子页面重现父页面
         */
        submitAll : function (action, subPage) {
                form.on('submit(submitAll)', function (data) {
                    console.log("当前容器的全部表单字段\n" + JSON.stringify(data.field)); //当前容器的全部表单字段，名值对形式：{name: value}
                    var Content = JSON.stringify(data.field);
                    //console.log( "ssssss\n" + JSON.stringify($('#form').serialize()));
                    $.ajax({
                        url: action,
                        type: 'post',
                        dataType: 'text',  //从后台返回的值的类型
                        contentType: 'application/json',
                        data: Content,
                        //data:$('#form').serialize(),
                        // success: function (data1) {
                        //     console.log(data1);
                        //     console.log("ajax返回值：\n" + JSON.stringify(data1.field));
                        //     layer.msg("上传成功！");
                        // },

                        //在后台设置的1为成功，0为失败
                        success: function (data1) {
                            console.log(data1);
                            console.log("ajax返回值：\n" + JSON.stringify(data1.field));
                            if (data1 == '1'){
                                layer.msg("上传成功！");
                                if (subPage == 1){
                                    //关闭当前窗口，强制刷新父窗口
                                    layer.close(layer.index);
                                    window.parent.location.reload();
                                }
                            }
                            else if (data1 == '0'){
                                layer.msg("更新失败");
                            }

                        },
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                            // 状态码
                            console.log(XMLHttpRequest.status);
                            // 状态
                            console.log(XMLHttpRequest.readyState);
                            // 错误信息
                            console.log(textStatus);
                            // 打印堆栈
                            //console.log(errorThrown);
                        }
                    });

                    return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
                });
        }
    }

    exports("epmsForm",obj);
})
