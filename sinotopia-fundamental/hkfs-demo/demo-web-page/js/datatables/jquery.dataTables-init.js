//初始化分页
var responseDatatableData = {};  //表格初始化数据 修改要慎重
$.fn.dataTable.defaults.dom= '<"toolbar">frtip'; //分页显示
$.fn.dataTable.defaults.ordering=  false;      //排序
$.fn.dataTable.defaults.searching=  false;   //搜索显示
$.fn.dataTable.defaults.showRowNumber=  true;
$.fn.dataTable.defaults.bAutoWidth =  false;  //自适应
$.fn.dataTable.defaults.iDisplayLength=  15;    //单页数据条数
$.fn.dataTable.defaults.sPaginationType=  "full_numbers";  //分页策略
$.fn.dataTable.defaults.bProcessing=  true;      //开启读取服务器数据时显示正在加载中……特别是大数据量的时候，开启此功能比较好
$.fn.dataTable.defaults.serverSide= true;
$.fn.dataTable.defaults.scrollX= true;
$.fn.dataTable.defaults.oLanguage = {
    "oAria": {
        "sSortAscending": ": 升序排列",
        "sSortDescending": ": 降序排列"
    },
    "oPaginate": {
        "sFirst": "首页",
        "sLast": "末页",
        "sNext": "下页",
        "sPrevious": "上页"
    },
    "sEmptyTable": "没有相关记录",
    "sInfo": "第 _START_ 到 _END_ 条记录，共 _TOTAL_ 条",
    "sInfoEmpty": "第 0 到 0 条记录，共 0 条",
    "sInfoFiltered": "(从 _MAX_ 条记录中检索)",
    "sInfoPostFix": "",
    "sDecimal": "",
    "sThousands": ",",
    "sLengthMenu": "每页显示条数: _MENU_",
    "sLoadingRecords": "正在载入...",
    "sProcessing": "正在载入...",
    "sSearch": "搜索:",
    "sSearchPlaceholder": "",
    "sUrl": "",
    "sZeroRecords": "没有相关记录"
};






