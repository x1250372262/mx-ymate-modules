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
```html
<div class="form-row col-md-12">
    <div class="form-group col-md-4 mx_validator">
        <label for="provinceId">省份</label><span class="required">* </span>
        <input type="hidden" name="id" value="">
        <input type="hidden" name="lastModifyTime" value="">
        <select class="select-init select-cascade form-control mx_required" isDefault="1"
                defaultOptionText="请选择省份" cascadePid="pid" cascadeId="cityId" cascadeKey="city" id="provinceId" name="provinceId"
                mx_required_msg="请选择省份" mx_required_type="select"
        ></select>
    </div>
    <div class="form-group col-md-4 mx_validator">
        <label for="cityId">城市</label><span class="required">* </span>
        <select class="form-control select-cascade mx_required" cascadePid="pid" cascadeId="memberId"  cascadeKey="member"
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
```js
   SELECT.initCascade("provinceId","id","name",baseUrl + "/select/select",null,null,"province",null);
```
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