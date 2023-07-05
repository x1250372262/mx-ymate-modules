### selectgroup使用
```js
 SELECT.initGroup($("#commonForm"),"id","name",null,baseUrl + "/select/select/group",null,null,"childList","name");
```
json格式
```json
{
  "code": "00000",
  "msg": "操作成功",
  "data": [
    {
      "name": "辽宁",
      "childList": [
        {
          "name": "大连",
          "pid": "liaoning",
          "id": "dalian"
        },
        {
          "name": "沈阳",
          "pid": "liaoning",
          "id": "shenyawng"
        },
        {
          "name": "丹东",
          "pid": "liaoning",
          "id": "dandong"
        }
      ],
      "id": "liaoning"
    },
    {
      "name": "吉林",
      "childList": [
        {
          "name": "吉林",
          "pid": "jilin",
          "id": "jilin"
        },
        {
          "name": "长春",
          "pid": "jilin",
          "id": "changchun"
        }
      ],
      "id": "jilin"
    }
  ]
}
```

### 级联使用使用
html

cascadePid下级的关联id （json中关联的id）

cascadeId 下级的元素id （省份下级城市，cityId）

cascadeKey下级的指定键名（下级的json名）

类名”select-init“只在级联第一级加

类名“select-cascade”每一级都加

```html

<div class="form-row col-md-12">
    <div class="form-group col-md-4 mx_validator">
        <label for="provinceId">省份</label><span class="required">* </span>
        <input type="hidden" name="id" value="">
        <input type="hidden" name="lastModifyTime" value="">
        <select class="select-init select-cascade form-control mx_required" isDefault="1"
                defaultOptionText="请选择省份" cascadePid="pid" cascadeId="cityId" cascadeKey="city" id="provinceId" name="provinceId"
                mx_required_msg="请选择省份" mx_required_type="select"></select>
    </div>
    <div class="form-group col-md-4 mx_validator">
        <label for="cityId">城市</label><span class="required">* </span>
        <select class="form-control select-cascade mx_required" cascadePid="pid" cascadeId="memberId" cascadeKey="member"
                isDefault="1" defaultOptionText="请选择城市" id="cityId" name="cityId"
                mx_required_msg="请选择城市" mx_required_type="select"></select>
    </div>
    <div class="form-group col-md-4 mx_validator">
        <label for="memberId">客户</label><span class="required">* </span>
        <select class="form-control mx_required" id="memberId" name="memberId" isDefault="1" defaultOptionText="请选择客户"
                mx_required_msg="请选择客户" mx_required_type="select"></select>
    </div>
</div>
```
js
```js
   SELECT.initCascade("provinceId","id","name",baseUrl + "/select/select",null,null,"province",null);
```
json

注意:/select/select接口需要返回三个级联需要的参数 比如
```json
{
  "code": "00000",
  "msg": "操作成功",
  "data": {
    "province": [
      {
        "name": "辽宁",
        "id": "liaoning"
      },
      {
        "name": "吉林",
        "id": "jilin"
      }
    ],
    "city": [
      {
        "name": "大连",
        "pid": "liaoning",
        "id": "dalian"
      },
      {
        "name": "沈阳",
        "pid": "liaoning",
        "id": "shenyawng"
      },
      {
        "name": "丹东",
        "pid": "liaoning",
        "id": "dandong"
      },
      {
        "name": "吉林",
        "pid": "jilin",
        "id": "jilin"
      },
      {
        "name": "长春",
        "pid": "jilin",
        "id": "changchun"
      }
    ],
    "member": [
      {
        "name": "张三",
        "pid": "dalian",
        "id": "zhangsan"
      },
      {
        "name": "李四",
        "pid": "dandong",
        "id": "lisi"
      }
    ]
  }
}
```

### 添加一行使用
```html
<div class="form-group col-md-12" >
                                        <button type="button" class="btn btn-primary btn-sm m-r-sm" id="newRow"><i
                                                class="fa fa-plus m-r-xs"></i>新增一行
                                        </button>
                                        <div class="hr-line-dashed"></div>
                                        <div class="table-responsive" style="max-height: 500px">
                                            <table class="table table-stripped table-bordered">
                                                <thead>
                                                    <tr style="background-color: #F5F5F6;text-align: center;">
                                                        <th>产品</th>
                                                        <th>数量</th>
                                                        <th>操作</th>
                                                    </tr>
                                                </thead>
                                                <tbody id="rowTemplate" dataKey="content">
                                                    <tr style="text-align: center;" class="rowValueDom rowValueUpdate">
                                                        <td>
                                                            <select class="select-init form-control mx_required" isDefault="1"
                                                                    defaultOptionText="请选择产品"  id="provinceId" name="productId"
                                                                    mx_required_msg="请选择产品" mx_required_type="select"
                                                            ></select>
                                                        </td>
                                                        <td> <input type="text" class="form-control" name="num" placeholder="请输入重量"></td>
                                                        <td><button class="btn btn-danger deleteRow btn-sm" >删除</button></td>
                                                    </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                        <div class="hr-line-dashed"></div>
                                    </div>
```

### 富文本编辑器使用
```html
 <div class="form-group col-md-12 mx_validator">
    <label for="remark">备注</label>
    <div class="wangDiv">
        <div id="bar" class="wangBar"></div>
        <div id="remark" name="remark" mx_required_type="wangEditor" mx_required_msg="备注不能为空" class="wangContent mx_required"></div>
    </div>
</div>

<link rel="stylesheet" href="/statics/plugins/wangeditor/wangEditor.css">
<link rel="stylesheet" href="/statics/plugins/wangeditor/nfew/css/wangeditor.css">

<script src="/statics/plugins/wangeditor/nfew/wangeditor.js"></script>
<script src="/statics/js/custom/wangEditor.js"></script>
```

### 导入excel使用
```html
  <a class="btn btn-primary m-l-10 importExcel" data-toggle="modal"
                                               data-target="#import" href="#!"><i class="mdi mdi-file-import"></i> 导入</a>



<div class="modal fade bs-example-modal-lg" id="import" tabindex="-1" role="dialog"
     aria-labelledby="myLargeModalLabel" data-backdrop="static">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h6 class="modal-title" id="import1">添加</h6>
                <div class="float-right">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </div>
            <div class="modal-body">
                <form action="" id="importForm">
                    <div class="form-group col-md-12">
                        <label>导入文件</label>
                        <div>
                            <input type="file" name="file" id="file" class="form-control fileInputExcel">
                        </div>
                    </div>
                    <div class="form-group col-md-12">
                        <label for="name">名称</label>
                        <input type="text" class="form-control"
                               name="name" value=""
                               placeholder="请输入名称"/>
                    </div>
                    <div class="form-group col-md-12">
                        <label for="remark">备注</label>
                        <textarea class="form-control"name="remark"
                                  placeholder="请输入备注"></textarea>
                    </div>

                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary mx_validator_button uploadExcelButton">保存
                </button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript" src="/statics/js/custom/import_excel.js?_v=1"></script>

EXCEL_IMPORT.init(dom,url,"xlsx,xls")
```

### select使用
```html
 <div class="form-group col-md-12 mx_validator serviceId">
    <label for="serviceId">服务商</label><span class="required">
    * </span>
    <select class="form-control select-init mx_required" mx_required_type="select"
            mx_required_msg="请选择服务商" id="serviceId" name="serviceId"
            isDefault="1">
    </select>
</div>

SELECT.init($(".serviceId"), "id", "name", url, null, null, "resultData")
```

### 时间插件使用
```html
 <div class="form-group col-md-12 mx_validator">
    <label for="startTime">有效期开始</label><span class="required">
										* </span>
    <input class="form-control js-datetimepicker dates mx_required" autocomplete="off"
           data-date-format="yyyy-mm-dd" type="text" id="startTime"
           dateType="day" mx_required_msg="请选择有效期开始"
           name="startTime" placeholder="选择有效期">
</div>

```