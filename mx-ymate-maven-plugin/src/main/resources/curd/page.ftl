<!DOCTYPE html>
<html lang="zh">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimal-ui">
    <title>${modelName}</title>
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="default">
    <script src="/statics/js/custom/dpstudio.js?_V=1"></script>
</head>

<body ng-app="">
<div class="lyear-layout-web">
    <div class="lyear-layout-container">
        <!--左侧导航-->
        <ng-include src="'/admin/common/sidebar.html'"></ng-include>
        <!--End 左侧导航-->
        <!--头部信息-->
        <ng-include src="'/admin/common/header.html'"></ng-include>
        <!--End 头部信息-->

        <!--页面主要内容-->
        <main class="lyear-layout-content">
            <div class="container-fluid p-t-15">

                <!--添加-->
                <div class="modal fade bs-example-modal-lg" id="commonDiv" tabindex="-1" role="dialog"
                     aria-labelledby="myLargeModalLabel" data-backdrop="static">
                    <div class="modal-dialog modal-lg" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h6 class="modal-title" id="myModalLabel">添加</h6>
                                <div class="float-right">
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                </div>
                            </div>
                            <div class="modal-body">
                                <form action="" id="commonForm">
<#--                                    <div class="form-group col-md-12 mx_validator">-->
<#--                                        <label for="name">名称</label><span class="required">-->
<#--										* </span>-->
<#--                                        <input type="hidden" name="id" value="">-->
<#--                                        <input type="hidden" name="lastModifyTime" value="">-->
<#--                                        <input type="text" class="form-control mx_required" mx_required_msg="名称不能为空"-->
<#--                                               id="name" name="name" value=""-->
<#--                                               placeholder="请输入名称"/>-->
<#--                                    </div>-->
<#--                                    <div class="form-group col-md-12">-->
<#--                                        <label for="type">类型</label>-->
<#--                                        <select id="type" class="form-control" name="type">-->
<#--                                            <option value="0">类型</option>-->
<#--                                            <option value="1">级别</option>-->
<#--                                        </select>-->
<#--                                    </div>-->
                                    <input type="hidden" name="id" value="">
                                    <#list dtoFieldList as field>
                                    <#if field.name == 'thumb' || field.name == 'logo' || field.name == 'photoUri'>
                                    <div class="form-group col-md-12 mx_validator">
                                        <label  for="${field.name}">${field.comment}</label><span class="required">* </span>
                                        <div class="form-controls">

                                            <ul class="list-inline row lyear-uploads-pic mb-0">
                                                <li class="col-6 col-md-4 col-lg-2">
                                                    <figure class="thumbnail">
                                                        <img name="{field.name}" src="/statics/images/no_image.gif">
                                                        <figcaption>
                                                            <input type="file" filter=".png,.jpg,.jpeg,.bmp,.gif"
                                                                   class="fileInput" style="display: none;"
                                                                   name="file"/>
                                                            <input type="hidden" id="{field.name}" name="${field.name}" class="fileresult">
                                                            <a class="btn btn-round btn-square btn-primary picUpload"
                                                               href="#!">上传</a>
                                                            <a class="btn btn-round btn-square btn-danger picDelete"
                                                               href="#!">移除</a>
                                                        </figcaption>
                                                    </figure>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                    <#elseif field.name?contains("Time") || field.name?contains("time")>
                                        <div class="form-group col-md-12 mx_validator">
                                            <label for="${field.name}">${field.comment}</label><span class="required">* </span>
                                            <input type="text" class="form-control mx_required dates js-datetimepicker" mx_required_msg="${field.comment}不能为空"
                                                   id="${field.name}" name="${field.name}" value=""
                                                   placeholder="请输入${field.comment}" dateType="day"  readonly data-date-format="yyyy-mm-dd"/>
                                        </div>
                                    <#else>
                                        <div class="form-group col-md-12 mx_validator">
                                            <label for="${field.name}">${field.comment}</label><span class="required">* </span>
                                            <input type="text" class="form-control mx_required" mx_required_msg="${field.comment}不能为空"
                                                   id="${field.name}" name="${field.name}" value=""
                                                   placeholder="请输入${field.comment}"/>
                                        </div>
                                    </#if>
                                    </#list>
                                </form>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                                <button type="button" class="btn btn-primary mx_validator_button" id="submit">保存
                                </button>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-lg-12">
                        <div class="card">
                            <header class="card-header">
                                <div class="card-title">${modelName}</div>
                            </header>
                            <div class="card-body">
                                <div class="card-toolbar clearfix">
                                    <form class="form-inline" id="searchForm" method="post">
                                        <div class="mx_search">

<#--                                            <div class="fom-group m-b-5 m-r-10">-->
<#--                                                <input class="form-control" type="text" name="name"-->
<#--                                                       placeholder="输入检索..">-->
<#--                                            </div>-->
<#--                                            <div class="form-group m-b-5 m-r-10">-->
<#--                                                <select class="form-control" name="type">-->
<#--                                                    <option value="" selected="selected">类型</option>-->
<#--                                                    <option value="0">类型</option>-->
<#--                                                    <option value="1">级别</option>-->
<#--                                                </select>-->
<#--                                            </div>-->
                                            <#list dtoFieldList as field>
                                            <div class="fom-group m-b-5 m-r-10">
                                                 <input class="form-control" type="text" name="${field.name}" placeholder="请输入${field.comment}..">
                                             </div>
                                            </#list>

                                        </div>
                                        <div>
                                            <a class="btn btn-primary m-b-5 m-r-5" id="searchButton"><i
                                                        class="mdi mdi-magnify"></i> 搜索</a>
                                            <button class="btn btn-secondary m-b-5" id="resetButton"><i
                                                        class="mdi mdi-delete"></i> 重置查询
                                            </button>
                                        </div>
                                    </form>
                                </div>
                                <div class="card-toolbar clearfix">
                                    <div class="form-inline">
                                        <a class="btn btn-primary m-b-5 m-r-5 creates" data-toggle="modal"
                                           data-target="#commonDiv" href="#!"><i class="mdi mdi-plus"></i> 新增</a>
                                        <a class="btn btn-danger m-b-5 m-r-5 deletes" data-toggle="modal"
                                           data-target=".removeDio" href="#!"><i class="mdi mdi-window-close"></i>
                                            删除</a>
                                    </div>
                                </div>

                                <table id="tableAjaxId">
                                </table>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </main>
        <!--End 页面主要内容-->
    </div>
</div>

<script type="text/javascript" src="/statics/js/apis/${modelName?uncap_first}.js"></script>
<script type="text/javascript" src="/statics/js/custom/table.js?_v=1"></script>
<script type="text/javascript">


    $(function () {
        Table.setUrl(${modelNameUp}_LIST, ${modelNameUp}_CREATE, ${modelNameUp}_UPDATE, ${modelNameUp}_DETAIL, ${modelNameUp}_DELETE, null);
        Table.init();
    });

    columns = [
        {
            checkbox: true, // 显示一个勾选框
            align: 'center' // 居中显示
        },
        <#list voFieldList as field>
        <#if field.name?contains("Time") || field.name?contains("time")>
        {
            field: '${field.name}',
            title: '${field.comment}',
            align: 'center',
            valign: 'middle',
            formatter: function (value, row, index) { // 单元格格式化函数
                return MX.changeTimeToString(value);
            }
        },
        <#elseif field.name == 'thumb' || field.name == 'logo' || field.name == 'photoUri'>
        {
            field: '${field.name}', // 返回json数据中的name
            title: '${field.comment}', // 表格表头显示文字
            align: 'center', // 左右居中
            valign: 'middle', // 上下居中
            formatter: function (value, row, index) { // 单元格格式化函数
                return Table.initPic(value);
            }
        },
        <#elseif field.name?contains("Amount") || field.name?contains("amount") || field.name?contains("price") || field.name?contains("Price")>
        {
            field: '${field.name}', // 返回json数据中的name
            title: '${field.comment}', // 表格表头显示文字
            align: 'center', // 左右居中
            valign: 'middle', // 上下居中
            formatter: function (value, row, index) { // 单元格格式化函数
                return MX.addPriceZero(MX.getPrice(value,100));
            }
        },
        <#elseif field.name != 'id'>
        {
            field: '${field.name}', // 返回json数据中的name
            title: '${field.comment}', // 表格表头显示文字
            align: 'center', // 左右居中
            valign: 'middle' // 上下居中
        },
        </#if>
        </#list>
         {
            title: "操作",
            align: 'center',
            valign: 'middle',
            width: 160, // 定义列的宽度，单位为像素px
            formatter: option
        }
    ]

    function option(value, row, index) {
        return Table.tableOptionDefault(value, row, index);
    }
</script>
</body>
</html>
