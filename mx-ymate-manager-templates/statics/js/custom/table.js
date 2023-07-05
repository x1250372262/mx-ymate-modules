var LIST_URL;
var DELETE_URL;
var ADD_URL;
var UPDATE_URL;
var DETAIL_URL;
var STATUS_URL;
var Table = function () {

    var tableDom = $("#tableAjaxId");

    var setUrl = function (listUrl, addUrl, updateUrl, detailUrl, deleteUrl, statusUrl) {
        LIST_URL = listUrl;
        DELETE_URL = deleteUrl;
        ADD_URL = addUrl;
        UPDATE_URL = updateUrl;
        DETAIL_URL = detailUrl;
        STATUS_URL = statusUrl;
    }

    var init = function (method, isToken, footerFunction) {
        var url = $.trim(LIST_URL);
        if (!url) {
            url = $.trim(tableDom.attr("listurl"));
        }
        if (!url) {
            MX.failMsg("listurl不存在");
            return false;
        }
        var headers = {}
        headers["X-Requested-With"] = "XMLHttpRequest";
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
                $('#searchForm').find('[name]').each(function () {
                    var value = $(this).val();
                    if ($.trim(value)) {
                        if ($(this).attr("class") && $(this).attr("class").indexOf("times") > 0) {
                            queryParams[$(this).attr('name')] = new Date(value.replace(/-/g, "/")).getTime();

                        } else if ($(this).attr("class") && $(this).attr("class").indexOf("dates") > 0) {
                            queryParams[$(this).attr('name')] = new Date(value.replace(/-/g, "/")).getTime();

                        } else {
                            queryParams[$(this).attr('name')] = value;
                        }
                    }
                });

                queryParams['pageSize'] = params.limit;   //页面大小
                queryParams['page'] = (params.offset / params.limit) + 1;   //当前页码
                queryParams['field'] = params.sort;   //排序的列名
                queryParams['order'] = params.order;   //排序方式'asc' 'desc'
                return queryParams;
            },
            // sortName: 'id', // 要排序的字段
            // sortOrder: 'desc', // 排序规则
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
                }else{
                    MX.failMsg(res.msg?res.msg:"查询失败")
                }
            },
            columns: columns,
            onLoadSuccess: function (data) {  //加载成功时执行
                if (footerFunction !== undefined && footerFunction != null) {
                    footerFunction(data);
                }
            },
            onLoadError: function () {  //加载失败时执行
            }

        })

    }

    var setToken = function (token) {
        localStorage.setItem("tableToken", token);
    }
    var needToken = function () {
        return localStorage.getItem("tableToken");
    }


    //点击添加事件
    $(".creates").on("click", function () {
        var dom = $(this).attr("data-target");
        $(dom).find("h4").text("添加");
        var url = $.trim(ADD_URL);
        if (!url) {
            url = $.trim(tableDom.attr("addUrl"));
        }
        if (!url) {
            MX.failMsg("addUrl不存在");
            return false;
        }
        $(dom).find("form").attr("action", url);
        FORM.clearValues($(dom))
        if($(dom).find("#rowTemplate").length > 0){
            MMP.clearValues();
            MMP.addTag();
        }
    });

    //点击导入excel
    $(".importExcel").on("click", function () {
        var dom = $(this).attr("data-target");
        $(dom).find("h4").text("导入excel");
        FORM.clearValues($(dom))
        if($(dom).find("#rowTemplate").length > 0){
            MMP.clearValues();
            MMP.addTag();
        }
    });

    //点击编辑事件
    tableDom.on("click", ".edits", function () {
        var id = $(this).attr("dataId");
        var dom = $(this).attr("data-target");
        var editUrl = $.trim(UPDATE_URL);
        if (!editUrl) {
            editUrl = $.trim(tableDom.attr("editUrl"));
        }
        var detailUrl = $.trim(DETAIL_URL);
        if (!detailUrl) {
            detailUrl = $.trim(tableDom.attr("detailUrl"));
        }
        if (!editUrl) {
            MX.failMsg("editUrl不存在");
            return false;
        }
        if (!detailUrl) {
            MX.failMsg("detailUrl不存在");
            return false;
        }
        detailUrl = detailUrl + "/" + id;
        editUrl = editUrl + "/" + id;
        $(dom).find("h4").text("编辑");
        $(dom).find("form").attr("action", editUrl);
        var isToken = true;
        if (needToken() !== undefined && needToken() !== null && needToken() !== "") {
            isToken = needToken() === "true";
        }
        FORM.clearValues($(dom))
        if($(dom).find("#rowTemplate").length > 0){
            MMP.clearValues();
        }
        MX.axget(detailUrl, null, isToken, null, function (e) {
            if (e.code === "00000") {
                FORM.setValues($(dom), e.data);
            }
        }, "json")
    });

    //点击详情事件
    tableDom.on("click", ".detail", function () {
        var id = $(this).attr("dataId");
        var dom = $(this).attr("data-target");
        var detailUrl = $.trim(DETAIL_URL);
        if (!detailUrl) {
            detailUrl = $.trim(tableDom.attr("detailUrl"));
        }
        if (!detailUrl) {
            MX.failMsg("detailUrl不存在");
            return false;
        }
        detailUrl = detailUrl + "/" + id
        $(dom).find("h4").text("详情");
        $(dom).find(".mx_fileName").each(function () {
            $(this).html("")
        });
        $(dom).find("input[type='file']").each(function () {
            $(this).val("")
            $(this).next().val("")
            $(this).parent().next().html("")
        });
        var isToken = true;
        if (needToken() !== undefined && needToken() !== null && needToken() !== "") {
            isToken = needToken() === "true";
        }
        MX.axget(detailUrl, null, isToken, null, function (e) {
            if (e.code === "00000") {
                FORM.setValues($(dom), e.data);
            }
        }, "json")
    });

    //点击添加编辑按钮
    $("#submit").click(function () {
        var isToken = true;
        if (needToken() !== undefined && needToken() !== null && needToken() !== "") {
            isToken = needToken() === "true";
        }
        if (isToken) {
            VALIDATE.validate($("#commonForm"), null, null, null, function (e) {
                if (e.code === "00000") {
                    MX.successMsg("操作成功~")
                    setTimeout(function () {
                        $("#tableAjaxId").bootstrapTable('refresh');
                        $("#commonDiv").find(".close").trigger("click");
                    }, 1000)
                } else {
                    MX.failMsg(e.msg != null ? e.msg : "操作失败")
                }
            })
        } else {
            VALIDATE.validateNoToken($("#commonForm"), null, null, null, function (e) {
                if (e.code === "00000") {
                    MX.successMsg("操作成功~")
                    setTimeout(function () {
                        $("#tableAjaxId").bootstrapTable('refresh');
                        $("#commonDiv").find(".close").trigger("click");
                    }, 1000)
                } else {
                    MX.failMsg(e.msg != null ? e.msg : "操作失败")
                }
            })
        }

    });

    //单个
    tableDom.on("click", ".deletes", function () {
        var id = $(this).attr("dataId");
        $("#deletes").attr("dataId", id);
    });
    //总的删除
    $(".deleteAll").click(function () {
        $("#deletes").attr("dataId", "");
    });
    //删除
    $(document).on("click", "#deletes", function () {
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
        if (needToken() !== undefined && needToken() !== null && needToken() !== "") {
            isToken = needToken() === "true";
        }
        var url = $.trim(DELETE_URL);
        if (!url) {
            url = $.trim(tableDom.attr("delUrl"));
        }
        if (!url) {
            MX.failMsg("delUrl不存在");
            return false;
        }
        MX.axpost(url, {"ids": ids}, isToken, null, function (e) {
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

    //搜索事件
    $("#searchButton").click(function () {
        $("#tableAjaxId").bootstrapTable('refresh');//刷新Table，Bootstrap Table 会自动执行重新查询
    });
    //重置事件
    $("#resetButton").click(function () {
        $('#searchForm').find('[name]').each(function () {
            $(this).val('');
        });
        $("#tableAjaxId").bootstrapTable('refresh', {pageNumber: 1});
    });

    //表格内修改状态
    $(document).on("click", ".mx_status", function () {
        var id = $(this).attr("dataId");
        var status = $(this).attr("status");
        var lastModifyTime = $(this).attr("lastModifyTime");

        var isToken = true;
        if (needToken() !== undefined && needToken() !== null && needToken() !== "") {
            isToken = needToken() === "true";
        }
        var url = $.trim(STATUS_URL);
        if (!url) {
            url = $.trim(tableDom.attr("statusUrl"));
        }
        if (!url) {
            MX.failMsg("statusUrl不存在");
            return false;
        }
        url = url + "/" + id;
        MX.axpost(url, {"lastModifyTime": lastModifyTime, "status": status}, isToken, null, function (e) {
            if (e.code === "00000") {
                MX.successMsg("操作成功~")
                setTimeout(function () {
                    tableDom.bootstrapTable('refresh');
                    $(".statusDio").find(".close").trigger("click");
                }, 1000)
            } else {
                MX.failMsg(e.msg != null ? e.msg : "操作失败")
            }
        })
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
            } else {
                layer.open({
                    type: 1,
                    skin: 'layui-layer-rim', //加上边框
                    area: ['420px', '240px'], //宽高
                    content: dataValue
                });
            }

        }
    });

    /**
     * 预览文件
     */
    $(document).on("click", ".fileYL", function () {
        var fileUrl = $(this).attr("fileUrl");
        if (fileUrl != null && fileUrl !== '' && fileUrl !== undefined) {
            MX.yl(fileUrl);

        }
    });


    return {
        setUrl: function (listUrl, addUrl, updateUrl, detailUrl, deleteUrl, statusUrl) {
            setUrl(listUrl, addUrl, updateUrl, detailUrl, deleteUrl, statusUrl);
        },
        init: function (method, footerFunction) {
            setToken(true);
            init(method, true, footerFunction);
        },
        initNotToken: function (method, footerFunction) {
            setToken(false);
            init(method, false, footerFunction);
        },
        initPic: function (value) {
            if (value !== undefined && value != null && value !== '') {
                return "<img data-toggle='modal' data-target='.picbig' class='smallpic' src='" + value + "' style='height:50px;width: 100px;'>";
            }
            return "<img data-toggle='modal' data-target='.picbig' class='smallpic' src='/statics/images/no_image.gif' style='height:50px;width: 100px;'>";
        },
        initFile: function (value) {
            return '<button class="btn btn-primary btn-w-md fileYL" fileUrl="' + value + '" type="button">预览文件</button>';

        },
        tableOptionDefault: function (value, row, index, customFuc) {
            var html = ' <a class="btn btn-xs btn-default edits" data-toggle="modal" data-target="#commonDiv" dataId="' + row.id + '" title="编辑" >编辑</a>' +
                '<a class="btn btn-xs btn-default deletes" data-toggle="modal" data-target=".removeDio" dataId="' + row.id + '"  title="删除" >删除</a>';
            if (customFuc !== undefined && customFuc !== null) {
                html += customFuc(value, row, index);
            }
            return html;

        },
        initSwitch: function (value, checkedValue, status, row) {
            var is_checked = "";
            var statusTemp;
            if (value === checkedValue) {
                statusTemp = status;
                is_checked = 'checked="checked"';
            } else {
                statusTemp = checkedValue
            }
            return '<div class="custom-control custom-switch"><input type="checkbox" class="custom-control-input" ' + is_checked + ' />' +
                '<label class="custom-control-label mx_status" style="cursor: pointer;" lastModifyTime="' + row.lastModifyTime + '" dataId="' + row.id + '" status="' + statusTemp + '"></label></div>';

        },
        setFooter: function (index, value) {
            var footer = $(".table-responsive").children(".bootstrap-table").children(".fixed-table-container").children(".fixed-table-footer");
            var ths = footer.children("table").children("thead").children("tr").find("th");
            $(ths).eq(index).children(".th-inner").html(value);
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


