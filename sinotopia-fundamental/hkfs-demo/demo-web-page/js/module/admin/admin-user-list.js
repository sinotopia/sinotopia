/**
 * @angular 管理员
 * @name DucApp
 * @description
 * @auth yanhui he
 * @time 2016/12/19 16:20
 */

adminUserListCtrl = function ($scope, $ducTools, $compile, http){
    'use strict';

    $scope.status = 1;
    $scope.deleteDataTable = $ducTools.deleteDataTable;
    var adminListTable = $('#adminListTable').DataTable({
        sAjaxSource: '/admin-web/admin/user/getUserList',
        fnServerData:  $ducTools.retrieveData,
        columns: [
            {"data": "id"},
            {"data": "name"},
            {"data": null},
            {"data": "phone"},
            {"data": "email"},
            {"data": null},
            {"data": null}
        ],
        columnDefs: [
            {
                targets: 2,
                render: function (c) {
                    return c.status == 1 ? "<span class='btn btn-success btn-xs'>正常</span>" : "<span class='btn btn-primary btn-xs'>禁用</span>";
                }
            },
            {
                targets: 5,
                render: function (c) {
                    var roleNames = "";
                    if (c.roles != undefined) {
                        $.each(c.roles, function(index, value){
                            if (roleNames != "") {
                                roleNames = roleNames + " ";
                            }
                            roleNames = roleNames + value.name;
                        });
                    }
                    return "<span class='btn btn-success btn-xs'>" + roleNames + "</span>";
                }
            },
            {
                targets: 6,
                render: function (c) {
                    return "<button ng-click=adminUpdate("+c.id+") class='btn btn-info btn-xs'>" +
                           "<i class='fa fa-pencil'></i> 修改 " +
                           "</button> &nbsp;" +
                           "<button ng-click=deleteDataTable('/admin-web/admin/user/deleteUser','id'," + c.id + ",'adminListTable') class='btn btn-danger btn-xs'>" +
                           "<i class='fa fa-trash-o'></i> 删除 " +
                           "</button> &nbsp;" +
                           "<button ng-click=empowerment(" + c.id + ") class='btn btn-success btn-xs'>" +
                           "<i class='fa fa-pencil-square-o'></i> 分配权限 " +
                           "</button>";
                 }
            }
        ],
        createdRow: function( row, data, dataIndex ) {
            $compile(row)($scope);
        }
    });

    $scope.pointsDraw = function() {
        adminListTable.draw();
    };


    //修改获取分类详情
    $scope.adminUpdate = function (id){
        $('#adminListModal').modal('show')
            .find('.modal-title')
            .html('修改管理员');

        http.post({id: id}, '/admin-web/admin/user/getUser').then(function (res){
            $scope.password = res.password;
            $scope.adminId = res.id;
            $scope.username = res.username;
            $scope.name = res.name;
            $scope.phone = Number(res.phone);
            $scope.email = res.email;
            $scope.status = res.status;
        });
    };

    jQuery.validator.addMethod('isMobile', function(value, element){
        var length = value.length;
        var mobile = /^1[34578]\d{9}$/;
        return this.optional(element) || (length == 11 && mobile.test(value))
    });

    //提交管理员数据
    $("#addAdminForm").validate({
        rules:{
            username: 'required',
            //password: 'required',
            email: {
                required : true,
                email: true
            },
            phone: {
                required : true,
                isMobile: true
            }
        },
        messages:{
            username:{
                required:"请输入用户名"
            },
            //password:{
            //    required:"请输入密码"
            //},
            email:{
                required:"请输入Email",
                email: "请输入正确请输入Email"
            },
            phone:{
                required:"请输入手机号码",
                isMobile: "请输入正确手机号"
            }
        },
        focusInvalid: false,
        onkeyup: false,
        submitHandler: function(){
            var _url = $scope.adminId == undefined ? '/admin-web/admin/user/addUser':'/admin-web/admin/user/updateUser';
            var data = {
                'id': $scope.adminId,
                'username': $scope.username,
                'password': $scope.password,
                'name': $scope.name,
                'phone': $scope.phone,
                'email': $scope.email,
                'status': $scope.status
            };

            http.post(data, _url).then(function () {
                $scope.adminId = undefined;

                $('#adminListModal').modal('hide')
                    .find('.modal-title')
                    .html("新增管理员");

                $ducTools.refreshDataTable("adminListTable");
            });
        },
        errorPlacement: function(error, element) {
            var $addAdminFormErrorMsg = $("#addAdminFormErrorMsg");
            if(!$addAdminFormErrorMsg.find('label').is(':visible')){
                $addAdminFormErrorMsg.html(error);
            }
            return false;
        }
    });

    //分配权限
    $scope.empowerment = function (id) {
        $('#empowerment').modal('show');
        $scope.userId = id;
        http.post({"pageSize" : "200"}, '/admin-web/admin/role/getRoleList')
            .then(function (result) {
                $scope.resRoles = result
            })
    };

    $scope.submitEmpowerment =  function(){
         $('#empowerment').modal('hide');
        http.post({
            'userId': $scope.userId,
            'roleId': $scope.roleId
        },'/admin-web/admin/role/addUserRole').then(function() {
            swal({
                title: "分配成功",
                animation: "slide-from-top"
            });

            $ducTools.refreshDataTable("adminListTable");
        })
    }

};
