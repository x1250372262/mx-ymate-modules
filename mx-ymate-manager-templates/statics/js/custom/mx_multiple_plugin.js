var MMP = function () {

    return {
        create: function (createBtnFunc,deleteBtnFunc) {
            CREATE_BTN_FUNCTION = createBtnFunc;
            DELETE_BTN_FUNCTION = deleteBtnFunc;
        },
        getValues:function(dom){
            var data = [];
            $(dom).each(function () {
               var d = FORM.getValues($(this));
                data.push(d);
            })
            return JSON.stringify(data);
        },
        setValues:function(dom="rowValueDom",data){
            if(!$.trim(TEMPLATE_HTML)){
                TEMPLATE_HTML = $("#rowTemplate").html();
            }
            if(data){
                var dataJson = JSON.parse(data);
                $.each(dataJson,function(index,item){
                    var html = TEMPLATE_HTML;
                    html = html.replace("rowValueUpdate","rowValueUpdate"+index)
                    $("#rowTemplate").append(html);
                     FORM.setValues($(".rowValueUpdate"+index+""),item);
                });

            }
        },
        clearValues:function(){
            if(!$.trim(TEMPLATE_HTML)){
                TEMPLATE_HTML = $("#rowTemplate").html();
            }
            $("#rowTemplate").html("");
        },
        addTag:function(){
            $("#rowTemplate").append(TEMPLATE_HTML);
        }
    };

}();

var  CREATE_BTN_FUNCTION = null;
var  DELETE_BTN_FUNCTION = null;
var  TEMPLATE_HTML = "";
$(function(){

    $(document).on("click","#newRow",function(){
        if(CREATE_BTN_FUNCTION != null){
            CREATE_BTN_FUNCTION();
        }else{
            if(!$.trim(TEMPLATE_HTML)){
                TEMPLATE_HTML = $("#rowTemplate").html();
            }
            $("#rowTemplate").append(TEMPLATE_HTML);
        }
    })

    $(document).on("click",".deleteRow",function(){
        if(DELETE_BTN_FUNCTION != null){
            DELETE_BTN_FUNCTION();
        }else{
            $(this).parent().parent().remove();
        }
    })
})