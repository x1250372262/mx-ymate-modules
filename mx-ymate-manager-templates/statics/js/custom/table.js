const PAGE_TYPE = {
    CLIENT: "client",
    SERVER: "server"
}

class Table {
    constructor() {
        //表格dom
        this.domParam = $("#tableAjaxId");
        //搜索表单dom
        this.searchDomParam = $("#searchForm");
        //搜索按钮
        this.searchButtonParam = $("#searchButton");
        //重置查询按钮
        this.resetButtonParam = $("#resetButton");
        //添加保存按钮dom
        this.submitDomParam = $("#submit");
        //添加按钮dom
        this.createDomParam = $(".creates");
        //导出按钮dom
        this.exportDomParam = $("#exportFile");
        //添加弹窗id
        this.formDivDom = $("#commonDiv");
        //添加弹窗form id
        this.formDom = $("#commonForm");
        //list接口地址
        this.listUrlParam = "";
        //添加接口地址
        this.createUrlParam = "";
        //修改接口地址
        this.updateUrlParam = "";
        //详情接口地址
        this.detailUrlParam = "";
        //删除接口地址
        this.deleteUrlParam = "";
        //状态接口地址
        this.statusUrlParam = "";
        //导出接口地址
        this.exportUrlParam = "";
        //是否需要token
        this.isTokenParam = true;
        //token失败的回调方法
        this.tokenCallBackParam = null;
        //自定义校验
        this.customValidateParam = null;
        //请求方式
        this.methodParam = REQUEST_METHOD.GET;
        //请求头
        this.headersParam = {};
        // 设置为 false 禁用 AJAX 数据缓存， 默认为true
        this.cacheParam = false;
        //表格显示条纹，默认为false
        this.stripedParam = true;
        // 是否显示所有的列
        this.showColumnsParam = true;
        // 是否显示刷新按钮
        this.showRefreshParam = true;
        // 是否显示详细视图和列表视图的切换按钮(clickToSelect同时设置为true时点击会报错)
        this.showToggleParam = true;
        // 在表格底部显示分页组件，默认false
        this.paginationParam = true;
        // 设置页面可以显示的数据条数
        this.pageListParam = [10, 20, 50, 100, 300];
        // 页面数据条数
        this.pageSizeParam = 10;
        // 首页页码
        this.pageNumberParam = 1;
        // 设置为服务器端分页
        this.sidePaginationParam = PAGE_TYPE.SERVER;
        //显示的列
        this.columnsParam = [];
        //查询参数函数
        this.queryParamsParam = null;
        //加载服务器数据之前的处理程序，可以用来格式化数据。
        this.responseHandlerParam = null;
        // 远程数据加载成功时函数
        this.onLoadSuccessParam = null;
        //远程数据加载失败时函数
        this.onLoadErrorParam = null;
        //当切换列的时候触发。
        this.onColumnSwitchParam = null;
        //是否初始化表格
        this.isInitTable = false;
    }

    static builder() {
        return new Table();
    }

    dom(dom) {
        this.domParam = dom;
        return this;
    }

    getDom() {
        return this.domParam;
    }

    searchDom(searchDom) {
        this.searchDomParam = searchDom;
        return this;
    }

    getSearchDom() {
        return this.searchDomParam;
    }

    searchButton(searchButton) {
        this.searchButtonParam = searchButton;
        return this;
    }

    getSearchButton() {
        return this.searchButtonParam;
    }

    resetButton(resetButton) {
        this.resetButtonParam = resetButton;
        return this;
    }

    getResetButton() {
        return this.resetButtonParam;
    }

    submitDom(submitDom) {
        this.submitDomParam = submitDom;
        return this;
    }

    getSubmitDom() {
        return this.submitDomParam;
    }

    createDom(createDom) {
        this.createDomParam = createDom;
        return this;
    }

    getCreateDom() {
        return this.createDomParam;
    }

    exportDom(exportDom) {
        this.exportDomParam = exportDom;
        return this;
    }

    getExportDom() {
        return this.exportDomParam;
    }

    formDiv(formDiv) {
        this.formDivDom = formDiv;
        return this;
    }

    getFormDiv() {
        return this.formDivDom;
    }

    form(form) {
        this.formDom = form;
        return this;
    }

    getForm() {
        return this.formDom;
    }

    listUrl(listUrl) {
        this.listUrlParam = listUrl;
        return this;
    }

    getListUrl() {
        return this.listUrlParam;
    }

    createUrl(createUrl) {
        this.createUrlParam = createUrl;
        return this;
    }

    getCreateUrl() {
        return this.createUrlParam;
    }

    updateUrl(updateUrl) {
        this.updateUrlParam = updateUrl;
        return this;
    }

    getUpdateUrl() {
        return this.updateUrlParam;
    }


    detailUrl(detailUrl) {
        this.detailUrlParam = detailUrl;
        return this;
    }

    getDetailUrl() {
        return this.detailUrlParam;
    }

    deleteUrl(deleteUrl) {
        this.deleteUrlParam = deleteUrl;
        return this;
    }

    getDeleteUrl() {
        return this.deleteUrlParam;
    }

    statusUrl(statusUrl) {
        this.statusUrlParam = statusUrl;
        return this;
    }

    getStatusUrl() {
        return this.statusUrlParam;
    }

    exportUrl(exportUrl) {
        this.exportUrlParam = exportUrl;
        return this;
    }

    getExportUrl() {
        return this.exportUrlParam;
    }

    isToken(isToken) {
        this.isTokenParam = isToken;
        return this;
    }

    getIsToken() {
        return this.isTokenParam;
    }

    tokenCallBack(tokenCallBack) {
        this.tokenCallBackParam = tokenCallBack;
        return this;
    }

    getTokenCallBack() {
        return this.tokenCallBackParam;
    }

    customValidate(customValidate) {
        this.customValidateParam = customValidate;
        return this;
    }

    getCustomValidate() {
        return this.customValidateParam;
    }

    method(method) {
        this.methodParam = method;
        return this;
    }

    getMethod() {
        return this.methodParam;
    }

    headers(headers) {
        this.headersParam = headers;
        return this;
    }

    getHeaders() {
        return this.headersParam;
    }

    cache(cache) {
        this.cacheParam = cache;
        return this;
    }

    getCache() {
        return this.cacheParam;
    }

    striped(striped) {
        this.stripedParam = striped;
        return this;
    }

    getStriped() {
        return this.stripedParam;
    }

    showColumns(showColumns) {
        this.showColumnsParam = showColumns;
        return this;
    }

    getShowColumns() {
        return this.showColumnsParam;
    }

    showRefresh(showRefresh) {
        this.showRefreshParam = showRefresh;
        return this;
    }

    getShowRefresh() {
        return this.showRefreshParam;
    }

    showToggle(showToggle) {
        this.showToggleParam = showToggle;
        return this;
    }

    getShowToggle() {
        return this.showToggleParam;
    }


    pagination(pagination) {
        this.paginationParam = pagination;
        return this;
    }

    getPagination() {
        return this.paginationParam;
    }

    pageList(pageList) {
        this.pageListParam = pageList;
        return this;
    }

    getPageList() {
        return this.pageListParam;
    }

    pageSize(pageSize) {
        this.pageSizeParam = pageSize;
        return this;
    }

    getPageSize() {
        return this.pageSizeParam;
    }

    pageNumber(pageNumber) {
        this.pageNumberParam = pageNumber;
        return this;
    }

    getPageNumber() {
        return this.pageNumberParam;
    }

    sidePagination(sidePagination) {
        this.sidePaginationParam = sidePagination;
        return this;
    }

    getSidePagination() {
        return this.sidePaginationParam;
    }

    columns(columns) {
        this.columnsParam = columns;
        return this;
    }

    getColumns() {
        return this.columnsParam;
    }

    queryParams(queryParams) {
        this.queryParamsParam = queryParams;
        return this;
    }

    getQueryParams() {
        return this.queryParamsParam;
    }

    responseHandler(responseHandler) {
        this.responseHandlerParam = responseHandler;
        return this;
    }

    getResponseHandler() {
        return this.responseHandlerParam;
    }

    onLoadSuccess(onLoadSuccess) {
        this.onLoadSuccessParam = onLoadSuccess;
        return this;
    }

    getOnLoadSuccess() {
        return this.onLoadSuccessParam;
    }

    onLoadError(onLoadError) {
        this.onLoadErrorParam = onLoadError;
        return this;
    }

    getOnLoadError() {
        return this.onLoadErrorParam;
    }

    onColumnSwitch(onColumnSwitch) {
        this.onColumnSwitchParam = onColumnSwitch;
        return this;
    }

    getOnColumnSwitch() {
        return this.onColumnSwitchParam;
    }

    init() {
        let tableThis = this;
        let tableDom = tableThis.getDom();
        let headers = tableThis.getHeaders();
        headers["X-Requested-With"] = "XMLHttpRequest";
        if (tableThis.getIsToken()) {
            if (!Token.expires()) {
                let tokenCallBack = tableThis.getTokenCallBack();
                if (tokenCallBack != null) {
                    tokenCallBack();
                } else {
                    LayerUtil.failMsg(ERROR_MSG.NO_LOGIN)
                    setTimeout(function () {
                        window.location.href = LOGIN_VIEW;
                    }, 1000)
                    return false;
                }
            } else {
                headers[HEADER_PARAM_NAME] = Token.get();
            }
        }
        let col = tableThis.getColumns();
        if (col.length === 0) {
            col = columns;
        }
        tableDom.bootstrapTable({
            url: tableThis.getListUrl(),
            method: tableThis.getMethod(),
            cache: tableThis.getCache(),
            striped: tableThis.getStriped(),
            showColumns: tableThis.getShowColumns(),
            showRefresh: tableThis.getShowRefresh(),
            showToggle: tableThis.getShowToggle(),
            pagination: tableThis.getPagination(),
            pageList: tableThis.getPageList(),
            pageSize: tableThis.getPageSize(),
            pageNumber: tableThis.getPageNumber(),
            sidePagination: tableThis.getSidePagination(),
            columns: col,
            ajaxOptions: {
                headers: headers
            },
            queryParams: function (params) {
                // 请求服务器数据时发送的参数，可以在这里添加额外的查询参数，返回false则终止请求
                let queryParamsFunc = tableThis.getQueryParams();
                if (queryParamsFunc !== undefined && queryParamsFunc !== null) {
                    queryParamsFunc(params);
                } else {
                    let queryParams = {};
                    let searchDom = tableThis.getSearchDom();
                    searchDom.find('[name]').each(function () {
                        let value = $(this).val();
                        if ($.trim(value)) {
                            if ($(this).hasClass("dates") || $(this).hasClass("times")) {
                                if ($(this).hasClass("end")) {
                                    value = value + " 23:59:59";
                                }
                                value = new Date(value.replace(/-/g, "/")).getTime();
                            }
                            queryParams[$(this).attr('name')] = value;
                        }
                    });
                    queryParams['pageSize'] = params.limit;   //页面大小
                    queryParams['page'] = (params.offset / params.limit) + 1;   //当前页码
                    queryParams['field'] = params.sort;   //排序的列名
                    queryParams['order'] = params.order;   //排序方式'asc' 'desc'
                    return queryParams;
                }
            },
            responseHandler: function (res, xhr) {
                let responseHandlerFunc = tableThis.getResponseHandler();
                if (responseHandlerFunc !== undefined && responseHandlerFunc !== null) {
                    responseHandlerFunc(res, xhr);
                } else {
                    if (res.code === "M0004") {
                        LayerUtil.failMsg(ERROR_MSG.NO_LOGIN)
                        setTimeout(function () {
                            window.location.href = LOGIN_VIEW;
                        }, 1000)
                    } else if (res.code === "00000") {
                        if (!tableThis.isInitTable) {
                            tableThis.initDetail();
                            tableThis.initDelete();
                            tableThis.initSearch();
                            tableThis.initUpdate();
                            tableThis.initCreate();
                            tableThis.initSubmit();
                            tableThis.initStatus();
                            tableThis.initExport();
                            tableThis.isInitTable = true;
                        }
                        return {
                            "total": res.data.recordCount,
                            "rows": res.data.resultData
                        }
                    } else {
                        LayerUtil.failMsg(res.msg ? res.msg : "查询失败")
                    }
                }
            },
            onLoadSuccess: function (data) {  //加载成功时执行
                let onLoadSuccessFunc = tableThis.getOnLoadSuccess();
                if (onLoadSuccessFunc !== undefined && onLoadSuccessFunc !== null) {
                    onLoadSuccessFunc(data);
                }
            },
            onLoadError: function () {  //加载失败时执行
                let onLoadErrorFunc = tableThis.getOnLoadError();
                if (onLoadErrorFunc !== undefined && onLoadErrorFunc !== null) {
                    onLoadErrorFunc();
                }
            },
            onColumnSwitch: function (field, checked) {
                let onColumnSwitchFunc = tableThis.getOnColumnSwitch();
                if (onColumnSwitchFunc !== undefined && onColumnSwitchFunc !== null) {
                    onColumnSwitchFunc(field, checked);
                }
            }

        })
    }

    //点击详情按钮
    initDetail() {
        let tableThis = this;
        let tableDom = tableThis.getDom();
        tableDom.on("click", ".detail", function () {
            let id = $(this).attr("dataId");
            let dom = $(this).attr("data-target");
            let detailUrl = $.trim(tableThis.getDetailUrl());
            if (!detailUrl) {
                LayerUtil.failMsg("detailUrl不存在");
                return false;
            }
            detailUrl = detailUrl + "/" + id
            $(dom).find("h4").text("详情");
            Form.clearValues($(dom))
            Request.builder()
                .url(detailUrl)
                .isToken(tableThis.getIsToken())
                .callback(function (e) {
                    if (e.code === "00000") {
                        Form.setValues($(dom), e.data);
                    }
                }).get();
        });
    }

    //点击修改按钮
    initUpdate() {
        let tableThis = this;
        let tableDom = tableThis.getDom();
        tableDom.on("click", ".edits", function () {
            let id = $(this).attr("dataId");
            let dom = $(this).attr("data-target");
            let updateUrl = $.trim(tableThis.getUpdateUrl());
            if (!updateUrl) {
                LayerUtil.failMsg("updateUrl不存在");
                return false;
            }
            let detailUrl = $.trim(tableThis.getDetailUrl());
            if (!detailUrl) {
                LayerUtil.failMsg("detailUrl不存在");
                return false;
            }
            detailUrl = detailUrl + "/" + id
            updateUrl = updateUrl + "/" + id;
            $(dom).find("h4").text("编辑");
            $(dom).find("form").attr("action", updateUrl);
            Form.clearValues($(dom))
            Request.builder()
                .url(detailUrl)
                .isToken(tableThis.getIsToken())
                .callback(function (e) {
                    if (e.code === "00000") {
                        Form.setValues($(dom), e.data);
                    }
                }).get();
        });

    }

    //点击添加按钮
    initCreate() {
        let tableThis = this;
        let createDom = tableThis.getCreateDom();
        createDom.on("click", function () {
            let dom = $(this).attr("data-target");
            let url = $.trim(tableThis.getCreateUrl());
            if (!url) {
                LayerUtil.failMsg("createUrl不存在");
                return false;
            }
            $(dom).find("h4").text("添加");
            $(dom).find("form").attr("action",url);
            Form.clearValues($(dom))
        })
    }

    //点击提交按钮
    initSubmit() {
        let tableThis = this;
        let tableDom = tableThis.getDom();
        let submitDom = tableThis.getSubmitDom();
        let isToken = tableThis.getIsToken();
        let customFunction = tableThis.getCustomValidate();
        let formDiv = tableThis.getFormDiv();
        let formDom = tableThis.getForm();
        submitDom.click(function () {
            let url = $.trim(formDom.attr("action"));
            Validator.builder()
                .dom(formDom)
                .url(url)
                .customValidate(customFunction)
                .isToken(isToken)
                .method(REQUEST_METHOD.POST)
                .submitCallback(function (e) {
                    if (e.code === "00000") {
                        LayerUtil.successMsg(ERROR_MSG.SUCCESS)
                        setTimeout(function () {
                            tableDom.bootstrapTable('refresh');
                            formDiv.find(".close").trigger("click");
                        }, 1000)
                    } else {
                        LayerUtil.failMsg(e.msg != null ? e.msg : ERROR_MSG.FAIL)
                    }
                }).do()
        });
    }

    //删除
    initDelete() {
        let tableThis = this;
        let tableDom = tableThis.getDom();
        //单个
        tableDom.on("click", ".deletes", function () {
            let id = $(this).attr("dataId");
            $("#deletes").attr("dataId", id);
        });

        //总的删除
        $(".deleteAll").click(function () {
            $("#deletes").attr("dataId", "");
        });
        //删除
        $(document).on("click", "#deletes", function () {
            let id = $(this).attr("dataId");
            let records = tableDom.bootstrapTable('getSelections');
            let ids = [];
            if ($.trim(id)) {
                ids[0] = id;
            } else {
                if (records.length > 0) {
                    for (let i in records) {
                        ids[i] = records[i]["id"];
                    }
                } else {
                    LayerUtil.failMsg("请选择要删除的数据")
                    return false;
                }
            }
            let url = $.trim(tableThis.getDeleteUrl());
            if (!url) {
                LayerUtil.failMsg("deleteUrl不存在");
                return false;
            }

            Request.builder()
                .url(url)
                .data({"ids": ids})
                .isToken(tableThis.getIsToken())
                .callback(function (e) {
                    if (e.code === "00000") {
                        $("#deletes").attr("dataId", "");
                        LayerUtil.successMsg("删除成功~");
                        setTimeout(function () {
                            tableDom.bootstrapTable('refresh');
                            $(".removeDio").find(".close").trigger("click");
                        }, 1000)
                    } else {
                        LayerUtil.failMsg(e.msg != null ? e.msg : "删除失败")
                    }
                }).post();

        });
    }

    //搜索
    initSearch() {
        let tableThis = this;
        let tableDom = tableThis.getDom();
        let searchDom = tableThis.getSearchDom();
        let searchButtonDom = tableThis.getSearchButton();
        let resetButtonDom = tableThis.getResetButton();
        //搜索事件
        searchButtonDom.click(function () {
            tableDom.bootstrapTable('refresh', {pageNumber: 1});//刷新Table，Bootstrap Table 会自动执行重新查询
        });
        //重置事件
        resetButtonDom.click(function () {
            searchDom.find('[name]').each(function () {
                $(this).val('');
            });
            tableDom.bootstrapTable('refresh', {pageNumber: 1});
        });
    }

    //修改状态
    initStatus() {
        let tableThis = this;
        let tableDom = tableThis.getDom();
        tableDom.on("click", ".mx_status", function () {
            let id = $(this).attr("dataId");
            let status = $(this).attr("status");
            let lastModifyTime = $(this).attr("lastModifyTime");
            let url = $.trim(tableThis.getStatusUrl());
            if (!url) {
                LayerUtil.failMsg("statusUrl不存在");
                return false;
            }
            url = url + "/" + id;
            Request.builder()
                .url(url)
                .data({"lastModifyTime": lastModifyTime, "status": status})
                .isToken(tableThis.getIsToken())
                .callback(function (e) {
                    if (e.code === "00000") {
                        LayerUtil.successMsg(ERROR_MSG.SUCCESS)
                        setTimeout(function () {
                            tableDom.bootstrapTable('refresh');
                            $(".statusDio").find(".close").trigger("click");
                        }, 1000)
                    } else {
                        LayerUtil.failMsg(e.msg != null ? e.msg : ERROR_MSG.FAIL)
                    }
                }).post();
        });
    }

    //导出
    initExport(){
        let tableThis = this;
        let tableDom = tableThis.getDom();
        let exportDom = tableThis.getExportDom();
        let searchDom = tableThis.getSearchDom();
        let exportUrl = tableThis.getExportUrl();
        exportDom.click(function(){
            if(!$.trim(exportUrl)){
                LayerUtil.failMsg("exportUrl不存在");
                return false;
            }
            let data = Form.getValues(searchDom);
            let records = tableDom.bootstrapTable('getSelections');
            let ids = [];
            if (records.length > 0) {
                for (let i in records) {
                    ids[i] = records[i]["id"];
                }
            }
            data.ids = ids
            data.export = 1;
            let load = LayerUtil.loading();
            Request.builder()
                .url(exportUrl)
                .data(data)
                .isToken(tableThis.getIsToken())
                .callback(function (e) {
                    layer.close(load);
                    if (e.code === "00000") {
                        LayerUtil.successMsg("文件生成成功，开始下载...");
                        window.location.href = e.data;
                    } else {
                        LayerUtil.failMsg(e.msg ? e.msg : "生成失败");
                    }
                })
                .post();
        })


    }

    //显示更多文字
    static moreText(value){
        if (value === null || value === undefined) {
            value = "";
        }
        let regex = /(<([^>]+)>)/ig;
        value = value.replace(regex, "");
        let v = value;
        if (value && value.length > 20) {
            value = value.substr(0, 18) + "...";
            return "<span style='cursor:pointer;' class='moreText' dataValue='" + v + "'>" + value + "</span>";
        }
        return "<span>" + value + "</span>";
    }

    //默认table选项
    static tableOptionDefault(value, row, index, customFuc){
        let html = ' <a class="btn btn-xs btn-default edits" data-toggle="modal" data-target="#commonDiv" dataId="' + row.id + '" title="编辑" >编辑</a>' +
            '<a class="btn btn-xs btn-default deletes" data-toggle="modal" data-target=".removeDio" dataId="' + row.id + '"  title="删除" >删除</a>';
        if (customFuc !== undefined && customFuc !== null) {
            html += customFuc(value, row, index);
        }
        return html;
    }

    //初始化图片
    static initPic(value){
        if (value !== undefined && value != null && value !== '') {
            return "<img data-toggle='modal' data-target='.picbig' class='smallpic' src='" + value + "' style='height:50px;width: 100px;'>";
        }
        return "<img data-toggle='modal' data-target='.picbig' class='smallpic' src='/statics/images/no_image.gif' style='height:50px;width: 100px;'>";
    }

    //初始化文件
    static initFile(value){
        return '<button class="btn btn-primary btn-w-md fileYL" fileUrl="' + value + '" type="button">预览文件</button>';
    }

    //初始化状态
    static initSwitch(value, checkedValue,status, row){
        let isChecked = "";
        let statusTemp;
        if (value === checkedValue) {
            statusTemp = status;
            isChecked = 'checked="checked"';
        } else {
            statusTemp = checkedValue
        }
        return '<div class="custom-control custom-switch"><input type="checkbox" class="custom-control-input" ' + isChecked + ' />' +
            '<label class="custom-control-label mx_status" style="cursor: pointer;" lastModifyTime="' + row.lastModifyTime + '" dataId="' + row.id + '" status="' + statusTemp + '"></label></div>';
    }

    //底部统计函数
    static censusFooter(index, value){
        let footer = $(".table-responsive").children(".bootstrap-table").children(".fixed-table-container").children(".fixed-table-footer");
        let ths = footer.children("table").children("thead").children("tr").find("th");
        $(ths).eq(index).children(".th-inner").html(value);
    }
}

$(function(){
    $(document).on("click", ".moreText", function () {
        let dataValue = $(this).attr("dataValue");
        if (dataValue != null && dataValue !== '' && dataValue !== undefined) {
            if ((navigator.userAgent.match(/(iPhone|iPod|Android|ios|iOS|iPad|Backerry|WebOS|Symbian|Windows Phone|Phone)/i))) {
                layer.open({
                    type: 1,
                    skin: 'layui-layer-rim', //加上边框
                    area: ['100%', '240px'], //宽高
                    content: '<div style="padding: 10px;text-indent: 28px;">' + dataValue + '</div>'
                });
            } else {
                layer.open({
                    type: 1,
                    skin: 'layui-layer-rim', //加上边框
                    area: ['420px', '240px'], //宽高
                    content: '<div style="padding: 10px;text-indent: 28px;">' + dataValue + '</div>'
                });
            }

        }
    });

    /**
     * 点击图片 可以放大
     */
    $(document).on("click", ".smallpic", function () {
        let src = $(this).attr("src");
        if (src != null && src !== '' && src !== undefined) {
            $(".picbig").find("img").attr("src", src);
        } else {
            $(".picbig").find("img").attr("src", "/statics/images/no_image.gif");
        }
    });


    /**
     * 预览文件
     */
    $(document).on("click", ".fileYL", function () {
        let fileUrl = $(this).attr("fileUrl");
        if (fileUrl != null && fileUrl !== '' && fileUrl !== undefined) {
            LayerUtil.preview(fileUrl);
        }
    });
});




