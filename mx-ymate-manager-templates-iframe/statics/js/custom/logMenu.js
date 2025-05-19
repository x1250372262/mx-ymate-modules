const LOG_MENU_LIST = "log_menu_list";
const DEFAULT_INDEX_NAME = "首页";
const DEFAULT_INDEX_URL = "/admin/home.html";

class LogMenu {
    constructor() {
        this.menuListParam = [];
    }

    static init() {
        let menuListStr = Store.get(LOG_MENU_LIST);
        if (menuListStr !== undefined && menuListStr !== null && menuListStr !== "" && menuListStr !== "[]") {
            let menuList = JSON.parse(menuListStr);
            let url =  window.location.pathname;
            let index = menuList.findIndex(function (item) {
                return item.url === url;
            });
            if(index > -1){
                $.each(menuList,function(index,item){
                    item.show = item.url === url;
                })
            }
            this.menuListParam = menuList;
        } else {
            this.menuListParam = [
                {
                    show: true,
                    title: DEFAULT_INDEX_NAME,
                    url: DEFAULT_INDEX_URL
                }
            ];
        }
        Store.set(LOG_MENU_LIST, JSON.stringify(this.menuListParam))
        return this;
    }


    static addMenu(menu = {}) {
        if (menu === undefined || menu === null) {
            return;
        }
        let menuList = JSON.parse(Store.get(LOG_MENU_LIST));
        let isAdd = false;
        menuList.map((item, index) => {
            if (item.title === menu.title) {
                item.show = true
                isAdd = true;
            } else {
                item.show = false
            }
        });
        if (!isAdd) {
            menuList.push(menu);
        }
        this.menuListParam = menuList;
        Store.set(LOG_MENU_LIST, JSON.stringify(this.menuListParam))
    }

    static setMenuList(menuList = []) {
        this.menuListParam = menuList;
        Store.set(LOG_MENU_LIST, JSON.stringify(this.menuListParam))
    }

    static getMenuList(menu = []) {
        return this.menuListParam;
    }

    static showMenu() {
        let menuList = this.menuListParam;
        if (menuList != null && menuList.length > 0) {
            let html = '';
            $.each(menuList, function (index, item) {
                if (item.title === DEFAULT_INDEX_NAME) {
                    html += `<div><div class="suspension1 item ${item.show ? 'historySelect' : ''}"><a class="hrefA text"  style="color: #000" href="${item.url}" style="text-align: center">${item.title}</a></div></div>`;
                } else {
                    html += `<div> <div class="suspension1 item ${item.show ? 'historySelect' : ''}"><a class="hrefA text" style="color: #000" href="${item.url}">${item.title}</a><div class="ms-2 f-18 removeLabel">×</div></div></div>`;
                }
            });
            $("#logMenu").html(html);
        }
    }

    static selectMenu(text) {
        let menuList = this.menuListParam;
        menuList.map((item, index) => {
            item.show = item.title === text;
        });
        this.setMenuList(menuList);
    }

    static deleteMenu(dom) {
        let menuList = this.menuListParam;
        menuList.map((item, index) => {
            if (item.title === dom.text()) {
                menuList.splice(index, 1);
                if (dom.parent().hasClass('historySelect')) {
                    menuList[index - 1].show = true
                    window.location.href = menuList[index - 1].url
                } else {
                    window.location.reload()
                }
            }
        });
        this.setMenuList(menuList);
    }
}

$(function () {
    let LOG_MENU_DOM = $("#logMenu");
    let RIGHT_MENU_DOM = $("#contextMenu");
    let rightMenu;
    // 向前滚动
    $('#scrollUpButton').click(function () {
        let currentScroll = LOG_MENU_DOM.scrollLeft();
        LOG_MENU_DOM.scrollLeft(currentScroll - 100); // 向前滚动100像素
    });

    // 向后滚动
    $('#scrollDownButton').click(function () {
        let currentScroll = LOG_MENU_DOM.scrollLeft();
        LOG_MENU_DOM.scrollLeft(currentScroll + 100); // 向后滚动100像素
    });
    // 删除
    LOG_MENU_DOM.on("click", ".removeLabel", function () {
        LogMenu.deleteMenu($(this).prev())
    });
    //点击
    LOG_MENU_DOM.on("click", ".hrefA", function () {
        LogMenu.selectMenu($(this).text());
    })
    // 重新加载
    RIGHT_MENU_DOM.on('click', '.newAgain', function () {
        window.location.reload();
    })
    // 关闭标签页
    RIGHT_MENU_DOM.on('click', '.closeTabs', function () {
        LogMenu.deleteMenu(rightMenu.find(".text"))
    })
    // 关闭左
    RIGHT_MENU_DOM.on('click', '.closeLeft', function () {
        let menuList = LogMenu.getMenuList();
        let text = rightMenu.find(".text").text();
        let index = menuList.findIndex(function (item) {
            return item.title === text;
        });
        menuList = menuList.splice(index, menuList.length - 1);
        menuList.unshift({
            show: false,
            title: DEFAULT_INDEX_NAME,
            url: DEFAULT_INDEX_URL
        })
        LogMenu.setMenuList(menuList);
        window.location.reload();
    })
    // 关闭右
    RIGHT_MENU_DOM.on('click', '.closeRight', function () {
        let menuList = LogMenu.getMenuList();
        let text = rightMenu.find(".text").text();
        let index = menuList.findIndex(function (item) {
            return item.title === text;
        });
        menuList = menuList.splice(0, index + 1);
        LogMenu.setMenuList(menuList);
        window.location.reload();
    })
    //关闭所有标签
    RIGHT_MENU_DOM.on('click', '.mt-close-all-tabs', function () {
        LogMenu.setMenuList([
            {
                show: true,
                title: DEFAULT_INDEX_NAME,
                url: DEFAULT_INDEX_URL
            }
        ]);
        window.location.href = DEFAULT_INDEX_URL;
    })
    //关闭其他标签
    RIGHT_MENU_DOM.on('click', '.mt-close-other-tabs', function () {
        let menuList = LogMenu.getMenuList();
        let text = rightMenu.find(".text").text();
        let item = menuList.find(function (item) {
            return item.title === text;
        });
        menuList = [];
        if(item.title!==DEFAULT_INDEX_NAME){
            menuList.push({
                show: false,
                title: DEFAULT_INDEX_NAME,
                url: DEFAULT_INDEX_URL
            });
        }
        menuList.push(item);
        LogMenu.setMenuList(menuList);
        window.location.reload();
    })

    // 右键菜单
    $(document).on('contextmenu', '.suspension1', function (e) {
        RIGHT_MENU_DOM.css('display', 'none');
        e.preventDefault();
        let boxPosition = $(this).offset();
        let boxLeft = boxPosition.left;
        let boxTop = boxPosition.top;
        let endRight = false;
        let endLeft = false;
        let home = false
        rightMenu = $(this);
        if (rightMenu.text() === DEFAULT_INDEX_NAME) {
            home = true
        }
        let menuList = LogMenu.getMenuList();
        menuList.map((item, index) => {
            if (item.title === rightMenu.find('.text').text()) {
                if (index === 0) {
                    endLeft = true
                }
                if (index === menuList.length - 1) {
                    endRight = true
                }
            }
        });
        RIGHT_MENU_DOM.css({
            display: 'block',
            left: boxLeft,
            top: boxTop + 45
        });
        let hasSelect = rightMenu.hasClass('historySelect');
        RIGHT_MENU_DOM.html(` <div class="${hasSelect ? 'newAgain' : 'prohibit'}"> <i class="mdi mdi-autorenew"></i> 重新加载</div>
                    <div class="${!home ? 'closeTabs' : 'prohibit'}"> <i class="mdi mdi-close"></i> 关闭标签页</div>
                    <li class="dropdown-divider"></li>
                    <div class="${hasSelect && !endLeft ? 'closeLeft' : 'prohibit'}"> <i class="mdi mdi-arrow-expand-left"></i> 关闭左侧标签</div>
                    <div class="${hasSelect && !endRight ? 'closeRight' : 'prohibit'}"> <i class="mdi mdi-arrow-expand-right"></i> 关闭右侧标签</div>
                    <li class="dropdown-divider"></li>
                    <div class="${hasSelect ? 'mt-close-other-tabs' : 'prohibit'}"> <i class="mdi mdi-format-horizontal-align-center"></i> 关闭其他标签页</div>
                    <div class="mt-close-all-tabs"> <i class="mdi mdi-home-outline"></i> 关闭所有标签页</div>
                    `)
    });
    $(document).on('click', function () {
        $('#contextMenu').css('display', 'none');
    });
});