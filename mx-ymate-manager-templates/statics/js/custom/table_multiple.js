var TableMultiple = function () {

    var table;
    var init = function (tableDom,columnsName,method,url,isToken, footerFunction) {
        table = tableDom;
        if(!$.trim(url)){
            url = tableDom.attr("listurl");
        }
        var headers = {}
        if (isToken) {
            headers[HEADER_PARAM_NAME] = Token.get();
        }
        tableDom.bootstrapTable({ // 对应table标签的id
            url: url, // 获取表格数据的url
            method: method,
            cache: false, // 设置为 false 禁用 AJAX 数据缓存， 默认为true
            striped: true,  //表格显示条纹，默认为false
            showColumns: true,         // 是否显示所有的列
            showRefresh: true,         // 是否显示刷新按钮
            showToggle: true,        // 是否显示详细视图和列表视图的切换按钮(clickToSelect同时设置为true时点击会报错)
            pagination: true, // 在表格底部显示分页组件，默认false
            pageList: [10, 20, 50, 100, 300], // 设置页面可以显示的数据条数
            pageSize: 10, // 页面数据条数
            pageNumber: 1, // 首页页码
            sidePagination: 'server', // 设置为服务器端分页
            ajaxOptions: {
                headers: headers
            },
            queryParams: function (params) { // 请求服务器数据时发送的参数，可以在这里添加额外的查询参数，返回false则终止请求
                var queryParams = {};
                $(tableDom.attr("searchFormClass")).find('[name]').each(function () {
                    var value = $(this).val();
                    if ($.trim(value)) {
                        if ($(this).attr("class") && $(this).attr("class").indexOf("times") > 0) {
                            queryParams[$(this).attr('name')] = new Date(value.replace(/-/g, "/")).getTime();

                        }
                        if ($(this).attr("class") && $(this).attr("class").indexOf("dates") > 0) {
                            queryParams[$(this).attr('name')] = new Date(value.replace(/-/g, "/")).getTime();

                        } else {
                            queryParams[$(this).attr('name')] = value;
                        }
                    }
                });

                queryParams['pageSize'] = params.limit;   //页面大小
                queryParams['page'] = (params.offset / params.limit) + 1;   //当前页码
                return queryParams;
            },
            sortName: 'id', // 要排序的字段
            sortOrder: 'desc', // 排序规则
            responseHandler: function (res, xhr) {
                if (res.code === "M0004") {
                    MX.failMsg("请登录后进行操作")
                    setTimeout(function () {
                        window.location.href = LOGIN_VIEW;
                    }, 1000)
                } else if (res.code === "00000") {
                    return {
                        "total": res.data.recordCount,
                        "rows": res.data.resultData
                    }
                }
            },
            columns: columnsName!==undefined&&columnsName!==null&&columnsName!==""?columns[columnsName]:columns,
            onLoadSuccess: function (data) {  //加载成功时执行
                if(footerFunction!==undefined&&footerFunction!=null){
                    footerFunction(data);
                }
            },
            onLoadError: function () {  //加载失败时执行
            }

        })


        //点击编辑事件
        tableDom.on("click", ".edits", function () {
            var id = $(this).attr("dataId");
            var dom = $(this).attr("data-target");
            var editUrl = tableDom.attr("editUrl") + "/" + id
            var detailUrl = tableDom.attr("detailUrl") + "/" + id
            $(dom).find("h4").text("编辑");
            $(dom).find("form").attr("action", editUrl);

            $(dom).find("input[type='file']").each(function () {
                $(this).val("")
                $(this).next().val("")
                $(this).parent().next().html("")
            });
            var isToken = true;
            if(needToken()!==undefined && needToken()!==null && needToken()!==""){
                isToken = needToken()==="true";
            }
            //删除input下方的红字提醒
            if ($(dom).find(".has-error").length > 0) {
                $(dom).find(".has-error").each(function () {
                    $(this).removeClass("has-error")
                })
            }
            if ($(dom).find(".help-block").length > 0) {
                $(dom).find(".help-block").each(function () {
                    $(this).remove()
                })
            }
            MX.axget(detailUrl, null, isToken,null,function (e) {
                if (e.code === "00000") {
                    FORM.setValues($(dom), e.data);
                }
            }, "json")
        });

        //单个
        tableDom.on("click", ".deletes", function () {
            var id = $(this).attr("dataId");
            $("#deletes").attr("dataId", id);
        });

        //删除
        $(document).on("click","#deletes",function(){
            var id = $(this).attr("dataId");
            var records = tableDom.bootstrapTable('getSelections');
            var ids = [];
            if ($.trim(id)) {
                ids[0] = id;
            } else {
                if (records.length > 0) {
                    for (var i in records) {
                        ids[i] = records[i]["id"];
                    }
                } else {
                    MX.failMsg("请选择要删除的数据")
                    return false;
                }
            }
            var isToken = true;
            if(needToken()!==undefined && needToken()!==null && needToken()!==""){
                isToken = needToken()==="true";
            }
            MX.axpost(tableDom.attr("delUrl"), {"ids": ids}, isToken,null,function (e) {
                if (e.code === "00000") {
                    $("#deletes").attr("dataId", "");
                    MX.successMsg("删除成功~");
                    setTimeout(function () {
                        tableDom.bootstrapTable('refresh');
                        $(".removeDio").find(".close").trigger("click");
                    }, 1000)
                } else {
                    MX.failMsg(e.msg != null ? e.msg : "删除失败")
                }
            })
        });


        //启用
        $(document).on("click","#enabledBtn",function(){
            var value =  $("#enabledBtn").attr("value");
            if(value===undefined||value===null||value===""){
                MX.failMsg("参数错误,请检查")
            }
            var records = tableDom.bootstrapTable('getSelections');
            var ids = [];
            if (records.length > 0) {
                for (var i in records) {
                    ids[i] = records[i]["id"];
                }
            } else {
                MX.failMsg("请选择要启用的数据")
                return false;
            }
            var isToken = true;
            if(needToken()!==undefined && needToken()!==null && needToken()!==""){
                isToken = needToken()==="true";
            }
            MX.axpost(tableDom.attr("statusUrl"), {"ids": ids,"status":value}, isToken,null,function (e) {
                if (e.code === "00000") {
                    MX.successMsg("启用成功~");
                    setTimeout(function () {
                        table.bootstrapTable('refresh');
                        $(".enabledDio").find(".close").trigger("click");
                    }, 1000)
                } else {
                    MX.failMsg(e.msg != null ? e.msg : "启用失败")
                }
            })
        })

        //禁用
        $(document).on("click","#disabledBtn",function(){
            var value =  $("#disabledBtn").attr("value");
            if(value===undefined||value===null||value===""){
                MX.failMsg("参数错误,请检查")
            }
            var records = tableDom.bootstrapTable('getSelections');
            var ids = [];
            if (records.length > 0) {
                for (var i in records) {
                    ids[i] = records[i]["id"];
                }
            } else {
                MX.failMsg("请选择要禁用的数据")
                return false;
            }
            var isToken = true;
            if(needToken()!==undefined && needToken()!==null && needToken()!==""){
                isToken = needToken()==="true";
            }
            MX.axpost(tableDom.attr("statusUrl"), {"ids": ids,"status":value},isToken,null, function (e) {
                if (e.code === "00000") {
                    MX.successMsg("禁用成功~");
                    setTimeout(function () {
                        table.bootstrapTable('refresh');
                        $(".disabledDio").find(".close").trigger("click");
                    }, 1000)
                } else {
                    MX.failMsg(e.msg != null ? e.msg : "禁用失败")
                }
            })
        })

        //表格内修改状态
        $(document).on("click",".mx_status",function(){
            var id = $(this).attr("dataId");
            var status = $(this).attr("status");

            var ids = [];
            ids[0] = id;
            var isToken = true;
            if(needToken()!==undefined && needToken()!==null && needToken()!==""){
                isToken = needToken()==="true";
            }
            MX.axpost(tableDom.attr("statusUrl"), {"ids": ids,"status":status}, isToken,null,function (e) {
                if (e.code === "00000") {
                    MX.successMsg("操作成功~")
                    setTimeout(function(){
                        tableDom.bootstrapTable('refresh');
                        $(".statusDio").find(".close").trigger("click");
                    },1000)
                } else {
                    MX.failMsg(e.msg != null ? e.msg : "操作失败")
                }
            })
        });

        //点击添加事件
        $(".creates").on("click", function () {
            var dom = $(this).attr("data-target");
            $(dom).find('[name]').each(function () {
                if (!$(this).hasClass("no_clear")) {
                    $(this).val('');
                }
            });
            $(dom).find("h4").text("添加");
            $(dom).find("form").attr("action", tableDom.attr("addUrl"));

            $(dom).find("img").each(function () {
                $(this).attr("src", "/statics/images/no_image.gif");
            });
            //radio 按钮
            $(dom).find("input[type='radio']").each(function () {
                if ("0" === $(this).val()) {
                    $(this).prop('checked', true)
                }
            });

            $(dom).find(".fileresult").val("");

            //百度编辑器 重置内部html内容
            if ($(dom).find(".editor").length > 0) {
                $(dom).find(".editor").each(function () {
                    if (editors[$(this).attr("id")].body) {
                        editors[$(this).attr("id")].setContent("");
                    } else {
                        $(this).find("iframe").contents().find("body").html("");
                    }

                });
            }
            $(dom).find(".js-tags-input").each(function () {
                $(this).importTags('');
                $(this).tagsInput({
                    height: '36px',
                    width: '100%',
                    defaultText: '添加值',
                    removeWithBackspace: true,
                    delimiter: [',']
                });
            });

            $(dom).find("input[type='file']").each(function () {
                $(this).val("")
                $(this).next().val("")
                $(this).parent().next().html("")
            });

            $(dom).find(".lyear-switch").each(function () {
                var inputChecked = $(this).children("input[type=checkbox]");
                inputChecked.prop('checked', false)
            });

            //删除input下方的红字提醒
            if ($(dom).find(".has-error").length > 0) {
                $(dom).find(".has-error").each(function () {
                    $(this).removeClass("has-error")
                })
            }
            if ($(dom).find(".help-block").length > 0) {
                $(dom).find(".help-block").each(function () {
                    $(this).remove()
                })
            }

        });



        //点击添加编辑按钮
        $("#submit").click(function () {
            var isToken = true;
            if(needToken()!==undefined && needToken()!==null && needToken()!==""){
                isToken = needToken()==="true";
            }
            if(isToken){
                VALIDATE.validate($("#commonForm"),null,null,function(e){
                    if (e.code === "00000") {
                        MX.successMsg("操作成功~")
                        setTimeout(function(){
                            tableDom.bootstrapTable('refresh');
                            $("#commonDiv").find(".close").trigger("click");
                        },1000)
                    } else {
                        MX.failMsg(e.msg != null ? e.msg : "操作失败")
                    }
                })
            }else{
                VALIDATE.validateNoToken($("#commonForm"),null,null,function(e){
                    if (e.code === "00000") {
                        MX.successMsg("操作成功~")
                        setTimeout(function(){
                            tableDom.bootstrapTable('refresh');
                            $("#commonDiv").find(".close").trigger("click");
                        },1000)
                    } else {
                        MX.failMsg(e.msg != null ? e.msg : "操作失败")
                    }
                })
            }

        });


        //总的删除
        $(".deleteAll").click(function () {
            $("#deletes").attr("dataId", "");
        });


        //搜索事件
        $(document).on("click",tableDom.attr("searchButtonClass"),function(){
            tableDom.bootstrapTable('refresh');//刷新Table，Bootstrap Table 会自动执行重新查询
        })
        //重置事件
        $(document).on("click",tableDom.attr("resetButtonClass"),function(){
            $(tableDom.attr("searchFormClass")).find('[name]').each(function () {
                $(this).val('');
            });
            tableDom.bootstrapTable('refresh');
        })

        //点击批量启用
        $(".enabledStatus").click(function () {
            var value = $(this).attr("value");
            $("#enabledBtn").attr("value", value);
        });
        //点击批量禁用
        $(".disabledStatus").click(function () {
            var value = $(this).attr("value");
            $("#disabledBtn").attr("value", value);
        });



        /**
         * 点击图片 可以放大
         */
        $(document).on("click", ".smallpic", function () {
            var src = $(this).attr("src");
            if (src != null && src !== '' && src !== undefined) {
                $(".picbig").find("img").attr("src", src);
            } else {
                $(".picbig").find("img").attr("src", "/admin/images/no_image.gif");
            }
        });

        /**
         * 查看更多文字
         */
        $(document).on("click", ".moreText", function () {
            var dataValue = $(this).attr("dataValue");
            if (dataValue != null && dataValue !== '' && dataValue !== undefined) {
                if ((navigator.userAgent.match(/(iPhone|iPod|Android|ios|iOS|iPad|Backerry|WebOS|Symbian|Windows Phone|Phone)/i))) {
                    layer.open({
                        type: 1,
                        skin: 'layui-layer-rim', //加上边框
                        area: ['100%', '240px'], //宽高
                        content: dataValue
                    });
                }else{
                    layer.open({
                        type: 1,
                        skin: 'layui-layer-rim', //加上边框
                        area: ['420px', '240px'], //宽高
                        content: dataValue
                    });
                }

            }
        });


    }

    var setToken = function(token){
        localStorage.setItem("tableToken",token);
    }
    var needToken = function(){
        return localStorage.getItem("tableToken");
    }





    return {
        init: function (tableDom,columnsName,method,footerFunction) {
            setToken(true);
            if(tableDom===undefined||tableDom===null||tableDom===""){
                tableDom = $("#tableAjaxId");
            }
            init(tableDom,columnsName,method,null,true, footerFunction);
        },
        initNotToken: function (tableDom,columnsName,method,footerFunction) {
            setToken(false);
            if(tableDom===undefined||tableDom===null||tableDom===""){
                tableDom = $("#tableAjaxId");
            }
            init(tableDom,columnsName,method,null,false, footerFunction);
        },
        initByUrl: function (tableDom,columnsName,url,method,footerFunction) {
            setToken(true);
            if(tableDom===undefined||tableDom===null||tableDom===""){
                tableDom = $("#tableAjaxId");
            }
            init(tableDom,columnsName,method,url, true, footerFunction);
        },
        initByUrlNotToken: function (tableDom,columnsName,url,method,footerFunction) {
            setToken(true);
            if(tableDom===undefined||tableDom===null||tableDom===""){
                tableDom = $("#tableAjaxId");
            }
            init(tableDom,columnsName,method,url, false, footerFunction);
        },
        initPic: function (value) {
            if (value !== undefined && value != null && value !== '') {
                return "<img data-toggle='modal' data-target='.picbig' class='smallpic' src='" + value + "' style='height:50px;width: 100px;'>";
            }
            return "<img data-toggle='modal' data-target='.picbig' class='smallpic' src='/statics/images/no_image.gif' style='height:50px;width: 100px;'>";
        },
        initSwitch: function (value,checkedValue,dataId,status) {
            var is_checked = "";
            var statusTemp;
            if (value === checkedValue) {
                statusTemp = status;
                is_checked = 'checked="checked"';
            }else{
                statusTemp = checkedValue
            }
            return  '<div class="custom-control custom-switch"><input type="checkbox" class="custom-control-input" '+ is_checked +' />' +
                '<label class="custom-control-label mx_status" style="cursor: pointer;" dataId="'+dataId+'" status="'+statusTemp+'"></label></div>';

        },
        setFooter: function (index, value) {
            var footer = $(".table-responsive").children(".bootstrap-table").children(".fixed-table-container").children(".fixed-table-footer");
            var ths = footer.children("table").children("thead").children("tr").find("th");
            $(ths).eq(index).children(".th-inner").html(value);
        },
        tableOptionDefault: function (value, row, index, customFuc) {
            var html = ' <a class="btn btn-xs btn-default edits" data-toggle="modal" data-target="#commonDiv" dataId="' + row.id + '" title="编辑" >编辑</a>' +
                '<a class="btn btn-xs btn-default deletes" data-toggle="modal" data-target=".removeDio" dataId="' + row.id + '"  title="删除" >删除</a>';
            if (customFuc !== undefined && customFuc !== null) {
                html += customFuc(value, row, index);
            }
            return html;

        },
        moreText: function (value) {
            if (value === null || value === undefined) {
                value = "";
            }
            var regex = /(<([^>]+)>)/ig;
            value = value.replace(regex, "");
            var v = value;
            if (value && value.length > 20) {
                value = value.substr(0, 18) + "...";
                return "<span style='cursor:pointer;' class='moreText' dataValue='" + v + "'>" + value + "</span>";
            }
            return "<span>" + value + "</span>";
        },
        numberToText: function (value, valueArray) {
            if (value === null || valueArray === null) {
                return "未知";
            }
            if(typeof(valueArray)=='string'){
                valueArray = JSON.parse(valueArray);
            }
            let result = valueArray.find((ele) => {
                return ele.value === value
            });
            return result.text;
        }
    };

}();


