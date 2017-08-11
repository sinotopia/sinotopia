/**
 * @angular 管理员
 * @name DucApp
 * @description
 * @auth yanhui he
 * @time 2016/12/19 16:20
 */

adminMenuListCtrl = function ($scope, $ducTools, $compile, http){
    'use strict';

    $scope.status = 1;
    $scope.deleteDataTable = $ducTools.deleteDataTable;
    var adminMenuTable = $('#adminMenuTable').DataTable({
        sAjaxSource: '/admin-web/admin/menu/getMenuList',
        fnServerData:  $ducTools.retrieveData,
        fnServerParams: function (aoData) {
            aoData.push(
                { "name": "id", "value": $scope.idQuery},
                { "name": "name", "value": $scope.nameQuery},
                { "name": "type", "value": 1}
            )
        },
        columns: [
            {"data": "id"},
            {"data": "name"},
            {"data": "parentId"},
            {"data": "url"},
            {"data": null},
            {"data": "sequence"},
            {"data": null}
        ],
        columnDefs: [
            {
                targets: 4,
                render: function (c) {
                    return c.status == 1 ? "<span class='btn btn-success btn-xs'>正常</span>" : "<span class='btn btn-primary btn-xs'>禁用</span>";
                }
            },
            {
                targets: 6,
                render: function (c) {
                    return "<button ng-click=deleteDataTable('/admin-web/admin/menu/deleteMenu','id'," + c.id + ",'adminMenuTable') class='btn btn-danger btn-xs'>删除</button> &nbsp;" +
                           "<button ng-click='menuUpdate("+c.id+")'  class='btn btn-info btn-xs'>修改</button>";
                 }
            }
        ],
        createdRow: function( row, data, dataIndex ) {
            $compile(row)($scope);
        }
    });

    $scope.adminMenuDraw = function() {
        adminMenuTable.draw();
    };



    $scope.menuUpdate = function (id){
        $('#adminMenuModal').modal('show')
            .find('.modal-title')
            .html('修改菜单');

        http.post({id: id}, '/admin-web/admin/menu/getMenu').then(function (res){
            $scope.name = res.name;
            $scope.adminMenuId = res.id;
            $scope.parentId = res.parentId;
            $scope.sequence = Number(res.sequence);
            $scope.url = res.url;
            $scope.status = res.status;
        });

    };


    $("#adminMenuForm").validate({
        rules:{
            name: 'required',
            parentId: 'required',
            url: 'required',
            sequence: 'required'
        },
        messages:{
            name:{
                required:"请输入菜单名称"
            },
            parentId:{
                required:"请输入父菜单Id"
            },
            url:{
                required:"请输入url"
            },
            sequence:{
                required:"请输入排序号"
            }
        },
        focusInvalid: false,
        onkeyup: false,
        submitHandler: function(){
            var _url = $scope.adminMenuId == undefined ? '/admin-web/admin/menu/addMenu': '/admin-web/admin/menu/updateMenu';
            var data = {
                'id': $scope.adminMenuId,
                'name': $scope.name,
                'parentId': $scope.parentId,
                'url': $scope.url,
                'sequence': $scope.sequence,
                'status': $scope.status
            };

            http.post(data, _url).then(function () {
                $scope.adminMenuId = undefined;

                $('#adminMenuModal').modal('hide')
                    .find('.modal-title')
                    .html("新增菜单");

                $ducTools.refreshDataTable("adminMenuTable");

            })
        },
        errorPlacement: function(error, element) {
            var $addAdminMenuFormErrorMsg = $("#addAdminMenuFormErrorMsg");
            if(!$addAdminMenuFormErrorMsg.find('label').is(':visible')){
                $addAdminMenuFormErrorMsg.html(error);
            }
            return false;
        }
    });


};
