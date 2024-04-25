$(function () {
    // 设置主题配色
    $(".dropdown-skin").on("click", ".mxTheme", function () {
        let input_name = $(this).attr("name");
        let data_name = $(this).attr("data-name");
        $('body').attr(data_name, $(this).val());
        $.isFunction($.cookie) && $.cookie('the_' + input_name, $(this).val());
    });
    // 读取cookie中的主题设置
    if ($.isFunction($.cookie)) {
        let the_logo_bg = $.cookie('the_logo_bg'),
            the_header_bg = $.cookie('the_header_bg'),
            the_sidebar_bg = $.cookie('the_sidebar_bg'),
            the_site_theme = $.cookie('the_site_theme');

        if (the_logo_bg) {
            $('body').attr('data-logobg', the_logo_bg);
        }
        if (the_header_bg) {
            $('body').attr('data-headerbg', the_header_bg);
        }
        if (the_sidebar_bg) {
            $('body').attr('data-sidebarbg', the_sidebar_bg);
        }
        if (the_site_theme) {
            $('body').attr('data-theme', the_site_theme);
        }

        // 处理主题配色下拉选中
        $(".dropdown-skin :radio").each(function () {
            let $this = $(this),
                radioName = $this.attr('name');
            switch (radioName) {
                case 'site_theme':
                    $this.val() === the_site_theme && $this.prop("checked", true);
                    break;
                case 'logo_bg':
                    $this.val() === the_logo_bg && $this.prop("checked", true);
                    break;
                case 'header_bg':
                    $this.val() === the_header_bg && $this.prop("checked", true);
                    break;
                case 'sidebar_bg':
                    $this.val() === the_sidebar_bg && $this.prop("checked", true);
            }
        });
    }

});