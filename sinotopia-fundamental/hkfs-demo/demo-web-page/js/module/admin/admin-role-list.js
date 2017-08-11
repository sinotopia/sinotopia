/**
 * @angular 管理员
 * @name DucApp
 * @description
 * @auth yanhui he
 * @time 2016/12/19 16:20
 */

adminRoleListCtrl = function ($scope, $ducTools, $compile, http){
    'use strict';

    $scope.status = 1;
    $scope.deleteDataTable = $ducTools.deleteDataTable;
    var authManageTable = $('#authManageTable').DataTable({
        sAjaxSource: '/admin-web/admin/role/getRoleList',
        fnServerData:  $ducTools.retrieveData,
        columns: [
            {"data": "id"},
            {"data": "name"},
            {"data": "code"},
            {"data": null},
            {"data": "description"},
            {"data": null}
        ],
        columnDefs: [
            {
                targets: 3,
                render: function (c) {
                    return c.status == 1 ? "<span class='btn btn-success btn-xs'>正常</span>" : "<span class='btn btn-primary btn-xs'>禁用</span>";
                }
            },
            {
                targets: 5,
                render: function (c) {
                    return "<button ng-click=roleUpdate("+c.id+") class='btn btn-info btn-xs'>" +
                           "<i class='fa fa-pencil'></i> 修改 " +
                           "</button> &nbsp;" +
                           "<button ng-click=deleteDataTable('/admin-web/admin/role/deleteRole','id'," + c.id + ",'authManageTable') class='btn btn-danger btn-xs'>" +
                           "<i class='fa fa-trash-o'></i> 删除 " +
                           "</button> &nbsp;" +
                           "<button ng-click=empowermentAuth('" + c.id + "') class='btn btn-success btn-xs'>" +
                           "<i class='fa fa-pencil-square-o'></i> 赋权 " +
                           "</button>";
                 }
            }
        ],
        createdRow: function( row, data, dataIndex ) {
            $compile(row)($scope);
        }
    });

    $scope.pointsDraw = function() {
        authManageTable.draw();
    };


    $scope.roleUpdate = function (id){
        $('#authManageModal').modal('show')
            .find('.modal-title')
            .html('修改角色');

        $scope.showCode = false;
        http.post({id: id}, '/admin-web/admin/role/getRole').then(function (res){
            $scope.authId = res.id;
            $scope.name = res.name;
            $scope.code = res.code;
            $scope.description = res.description;
            $scope.status = res.status;
        });

    };

    $("#addAuthManageForm").validate({
        rules:{
            name: 'required',
            code: 'required',
            description: 'required'
        },
        messages:{
            name:{
                required:"请输入角色名称"
            },
            code:{
                required:"请输入角色代号"
            },
            description:{
                required:"请输入角色描述"
            }
        },
        focusInvalid: false,
        onkeyup: false,
        submitHandler: function(){
            var _url = $scope.authId == undefined ?'/admin-web/admin/role/addRole':'/admin-web/admin/role/updateRole';
            var data = {
                'id': $scope.authId,
                'name': $scope.name,
                'code': $scope.code,
                'description': $scope.description,
                'status': $scope.status
            };


            http.post(data, _url).then(function () {
                $scope.authId = undefined;
                $scope.showCode = true;

                $('#authManageModal').modal('hide')
                    .find('.modal-title')
                    .html("新增角色");

                $ducTools.refreshDataTable("authManageTable");

            })
        },
        errorPlacement: function(error, element) {
            $("#addAuthManageErrorMsg").html(error);
            return false;
        }
    });


    $scope.empowermentAuth = function (id) {
        $('#empowermentAuth').modal('show');
        $scope.empIdAuth = id;


        http.post({}, '/admin-web/admin/menu/getMenuList')
            .then(function (data) {
                var $menusp = $("#menusp");
                $menusp.html("");
                for(var i = 0;i < data.length; i++) {
                    var result = data[i];
                    if(result.parentId==0){
                        $menusp.append(
                            "<li name='" + result.id + "'class= 'authList'>" +
                            "<input ng-click='cancelChildChosed($event)' type='checkbox' value=" + result.id + " id=role_menu_" + result.id+" />"
                            + result.name +
                            "</li>"
                        );
                    } else{
                        $("li[name='" + result.parentId + "']").after(
                            "<li>" +
                            "<input parentId='"+result.parentId+"' ng-click='choseParent($event)' type='checkbox' value=" + result.id+" id=role_menu_" + result.id+" />" +
                            "" + result.name + " " +
                            "</li>"
                        );
                    }
                }
                $compile($("#menusp li"))($scope);

                http.post({"roleId" : id}, '/admin-web/admin/menu/getRoleMenuList')
                    .then(function (data) {
                        for(var i = 0;i < data.length; i++) {
                            var roleMenuId = "#role_menu_" +data[i].menuId;
                            $(roleMenuId).attr("checked", "true");
                        }
                    });

            });
    };

    $scope.submitAuthEmpowerment =  function(){
        var checkeds = $("input[name!='all']:checked"), menuIds = "";
        for(var i = 0; i < checkeds.length; i++){
            if(i == (checkeds.length*1-1)){
                menuIds += checkeds.eq(i).val();
            }else{
                menuIds += checkeds.eq(i).val()+",";
            }
        }
        http.post({
            'menuIds': menuIds,
            'roleId': $scope.empIdAuth
        },'/admin-web/admin/menu/addRoleMenu')
            .then(function() {
                $('#empowermentAuth').modal('hide');
                $scope.empIdAuth = undefined;
                swal({
                    title: "分配成功",
                    animation: "slide-from-top"
                });

                $ducTools.refreshDataTable("authManageTable");
            })

    };


    $scope.actionAll = function ($event){
        var allSelect= $("#menusp").find("input[name!='all']");
        $($event.target).prop("checked")?allSelect.prop("checked", true):allSelect.prop("checked", false);
    };

    $scope.cancelChildChosed = function ($event){
        var id = $($event.target).parent().attr("name");
        $("input[parentid='"+id+"']").prop("checked", false);
    };

    $scope.choseParent = function ($event){
        if( $($event.target).prop("checked")){
            var parentId = $($event.target).attr("parentid");
            $("li[name='"+parentId+"']").find("input[type='checkbox']").prop("checked", true);
        }
    }

};
