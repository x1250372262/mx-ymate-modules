(function ($){
    if (typeof $.fn.each2 == "undefined"){
        $.extend($.fn,{
            /*
             * 4-10 times faster .each replacement
             * use it carefully, as it overrides jQuery context of element on each iteration
             */
            each2 : function (c){
                var j = $([0]), i = - 1, l = this.length;
                while (
                    ++i < l
                        && (j.context = j[0] = this [i])
                        && c.call(j[0], i, j) !== false //"this"=DOM, i=index, j=jQuery object
                    );
                return this;
            }
        });
    }
})(jQuery);


(function ($, undefined){
    var AbstractSelect2, MultiSelect2, $document, $document = $(document);
    function reinsertElement(element) {
        var placeholder = $(document.createTextNode(''));

        element.before(placeholder);
        placeholder.before(element);
        placeholder.remove();
    }
    function killEvent(event){
        event.preventDefault();
        event.stopPropagation();
    }
    //改变dest的class值
    function syncCssClasses(dest, src, adapter){
        var classes, replacements = [], adapted;
        classes = $.trim(dest.attr("class"));
        if (classes){
            classes = '' + classes;// for IE which returns object
            // "/\s+/" ==  " ' '"该正则表达式等价于一个空格
            $(classes.split(/\s+/)).each2(function (){
                if (this.indexOf("select2-") === 0) {
                    replacements.push(this);
                }
            });
        }
        classes = $.trim(src.attr("class"));
        if (classes){
            classes = '' + classes;
            // for IE which returns object
            $(classes.split(/\s+/)).each2(function ()
            {
                if (this.indexOf("select2-") !== 0) {
                    adapted = adapter(this);
                    if (adapted) {
                        replacements.push(adapted);
                    }
                }
            });
        }
        dest.attr("class", replacements.join(" "));
    }
    function clazz(SuperClass, methods)
    {
        var constructor = function () {};
        constructor.prototype = new SuperClass;
        constructor.prototype.constructor = constructor;
        constructor.prototype.parent = SuperClass.prototype;
        constructor.prototype = $.extend(constructor.prototype, methods);
        return constructor;
    }
    AbstractSelect2 = clazz(Object,{
        // abstract
        bind : function (func){
            var self = this;
            return function (){
                func.apply(self, arguments);
            };
        },
        // abstract
        init : function (opts){
            var results, search, resultsSelector = ".select2-results";
            this.opts = opts = this.prepareOpts(opts);
            this.id = opts.id;
            this.container = this.createContainer();
            this.body = $("body");
            syncCssClasses(this.container, this.opts.element, this.opts.adaptContainerCssClass);
            // swap container for the element
            this.opts.element.data("select2", this).attr("tabindex", "-1").before(this.container).on("click.select2", killEvent);
            // do not leak click events  不能漏掉单击事件？
            this.container.data("select2", this);
            this.dropdown = this.container.find(".select2-drop");
            syncCssClasses(this.dropdown, this.opts.element, this.opts.adaptDropdownCssClass);
            this.dropdown.data("select2", this);
            this.dropdown.on("click", killEvent);
            //dropdown 点击input框调用的事件
            this.results = results = this.container.find(resultsSelector);
            this.search = search = this.container.find("select.select2-input");
            // initialize the container
            this.initContainer();
        },
        // abstract
        prepareOpts : function (opts){
            var element;
            element = opts.element;
            //此处引用了$.fn.select2.defaults
            opts = $.extend({}, $.fn.select3.defaults, opts);
            return opts;
        },
        // abstract
        opened : function (){
            return (this.container) ? this.container.hasClass("select2-dropdown-open") : false;
        },
        // abstract
        //规定弹出框的样式
        positionDropdown : function (){
            var $dropdown = this.dropdown,
                offset = this.container.offset(),
                height = this.container.outerHeight(false),
                width = this.container.outerWidth(false),
                dropHeight = $dropdown.outerHeight(false),
                $window = $(window),
                windowHeight = $window.height(),
                viewportBottom = $window.scrollTop() + windowHeight,
                dropTop = offset.top + height,
                dropLeft = offset.left,
                enoughRoomBelow = dropTop + dropHeight <= viewportBottom,
                enoughRoomAbove = (offset.top - dropHeight) >= $window.scrollTop(),
                aboveNow = $dropdown.hasClass("select2-drop-above"),
                above, changeDirection, css;
            //窗口总是下拉显示，除非没有足够的空间
            if (aboveNow){
                above = true;
                if (!enoughRoomAbove && enoughRoomBelow) {
                    changeDirection = true;
                    above = false;
                }
            }else{
                above = false;
                if (!enoughRoomBelow && enoughRoomAbove) {
                    changeDirection = true;
                    above = true;
                }
            }
            css = {
                left : dropLeft, width : width+1
            };
            if (above){
                css.top = offset.top - dropHeight;
                css.bottom = 'auto';
                this.container.addClass("select2-drop-above");
                $dropdown.addClass("select2-drop-above");
            }else{
                css.top = dropTop;
                css.bottom = 'auto';
                this.container.removeClass("select2-drop-above");
                $dropdown.removeClass("select2-drop-above");
            }
            $dropdown.css(css);
        },

        // abstract
        clearDropdownAlignmentPreference: function() {
            // 选择是在上打开还是在下打开
            this.container.removeClass("select2-drop-above");
            this.dropdown.removeClass("select2-drop-above");
        },

        // abstract
        //显示弹出框
        open : function (){
            this.opening();
            return true;
        },
        // abstract
        opening: function() {
            var cid = this.containerEventName,
                scroll = "scroll." + cid,
                resize = "resize."+cid,
                orient = "orientationchange."+cid,
                mask;

            this.container.addClass("select2-dropdown-open").addClass("select2-container-active");

            this.clearDropdownAlignmentPreference();

            if(this.dropdown[0] !== this.body.children().last()[0]) {
                this.dropdown.detach().appendTo(this.body);
            }

            // create the dropdown mask if doesn't already exist
            mask = $("#select2-drop-mask");
            if (mask.length == 0) {
                mask = $(document.createElement("div"));
                mask.attr("id","select2-drop-mask").attr("class","select2-drop-mask");
                mask.hide();
                mask.appendTo(this.body);
                mask.on("mousedown touchstart click", function (e) {
                    // Prevent IE from generating a click event on the body
                    reinsertElement(mask);

                    var dropdown = $("#select2-drop"), self;
                    if (dropdown.length > 0) {
                        self=dropdown.data("select2");
                        if (self.opts.selectOnBlur) {
                            self.selectHighlighted({noFocus: true});
                        }
                        self.close();
                        e.preventDefault();
                        e.stopPropagation();
                    }
                });
            }

            // ensure the mask is always right before the dropdown
            if (this.dropdown.prev()[0] !== mask[0]) {
                this.dropdown.before(mask);
            }

            // move the global id to the correct dropdown
            $("#select2-drop").removeAttr("id");
            this.dropdown.attr("id", "select2-drop");

            // show the elements
            mask.show();

            this.positionDropdown();
            this.dropdown.show();
            this.positionDropdown();

            this.dropdown.addClass("select2-drop-active");

            // attach listeners to events that can change the position of the container and thus require
            // the position of the dropdown to be updated as well so it does not come unglued from the container
            var that = this;
            this.container.parents().add(window).each(function () {
                $(this).on(resize+" "+scroll+" "+orient, function (e) {
                    if (that.opened()) that.positionDropdown();
                });
            });
        },
        // abstract
        //关闭窗口
        close : function (){
            if (!this.opened()) {
                return;
            }
            $("#select2-drop-mask").hide();
            this.dropdown.removeAttr("id");
            // only the active dropdown has the select2-drop id
            this.dropdown.hide();
            this.container.removeClass("select2-dropdown-open").removeClass("select2-container-active");
            this.results.empty();
            this.search.removeClass("select2-active");
            this.opts.element.trigger($.Event("select2-close"));
        }
    });
    MultiSelect2 = clazz(AbstractSelect2,{
        // multi
        createContainer : function (){
            var container = $(document.createElement("div")).attr({
                "class" : "select2-container select2-container-multi"
            }).html([ "<ul class='select2-choices'>", "  <li class='select2-search-field'>", "    <label for='' class='select2-offscreen'></label>",
                "    <select type='text' autocomplete='off' autocorrect='off' autocapitalize='off' spellcheck='false' class='select2-input'></select>",
                "  </li>", "</ul>", "<div class='select2-drop select2-drop-multi select2-display-none'>",
                "   <div id='select2-results'>", "   </div>", "</div>"].join(""));
            return container;
        },
        // multi
        initContainer : function (){
            var selector = ".select2-choices", selection;
            this.searchContainer = this.container.find(".select2-search-field");
            this.searchContainer.css({"float": "right","marginTop":"6px"});
            this.searchContainer.children("select").css({"border":"none"});
            this.selection = selection = this.container.find(selector);
            //给input的id赋值s2id_autogen，（this.search等于input）
            this.search.attr("id", "s2id_autogen");
            this.container.on("click", selector, this.bind(function (e){
                //点击显示弹出框
                this.open();
            }));
            this.opts.element.remove();
        }
    });
    $.fn.select3 = function (){
        var args = Array.prototype.slice.call(arguments, 0), opts, select2;
        this.each(function (){
            opts = args.length === 0 ? {} : $.extend({}, args[0]);
            opts.element = $(this);
            select2 = new window.Select2["class"].multi();
            select2.init(opts);
        });
        return this;
    };
    // plugin defaults, accessible to users
    $.fn.select3.defaults ={
        formatResult : function (result, container, query, escapeMarkup){
            var markup = [];
            return markup.join("");
        },
        formatResultCssClass : function (data){
            return data.css;
        },
        id : function (e){
            return e == undefined ? null : e.id;
        },
        adaptContainerCssClass : function (c){
            return c;
        },
        adaptDropdownCssClass : function (c){
            return null;
        }
    };
    // exports
    window.Select2 = {
        "class" : {
            "abstract" : AbstractSelect2, "multi" : MultiSelect2
        }
    };
}(jQuery));