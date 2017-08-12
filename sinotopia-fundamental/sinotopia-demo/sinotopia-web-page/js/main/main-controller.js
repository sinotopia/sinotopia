/**
 * @angular通用控制器
 * @name DucApp
 * @description
 * @auth yanhui he
 * @time 2016/12/19 16:20
 */

(function($) {
    'use strict';
   var mainCtr = angular.module("app.controller", ["app.service"]);

    //公用控制器
    mainCtr.controller("commonCtr", ["$scope", "$location", "login", "$timeout", function($scope, $location, login, $timeout) {
        $scope.$on("$routeChangeSuccess", function ($event) {
            $scope.currentPage = $location.path();
        });

        login.getSessionMenuList({}).then(function(response){
            var listData = [], childMenu = [];
            $.each(response, function(index, value) {
                value.childMenu = [];
                value.parentId == 0 ? listData.push(value) : childMenu.push(value)
            });

            $.each(childMenu, function(index, childMenu) {
                var parentId = childMenu.parentId;
                $.each(listData, function(index, listData) {
                    if(listData.id == parentId){
                        listData.childMenu.push(childMenu)
                    }
                });
            });
            $scope.menuList = listData;
        })

        $scope.showModal = function(id) {
            $(id).modal('show').find("form")[0].reset()
        };

        $scope.hideModal = function(id) {
            $(id).modal('hide').find("form")[0].reset()
        };

        $scope.wdatePickerFocus = function(id, fmt){
            WdatePicker({
                errDealMode:1,
                el: id,
                dateFmt: fmt || 'yyyy-MM-dd HH:mm:ss',
                onpicked: function(){
                    $(this).trigger('change')
                }
            })
        };
        $scope.menuToggle = function(){
            $('.menu-list > a').click(function() {
                var parent = $(this).parent();
                var sub = parent.find('> ul');
                if(!$('body').hasClass('left-side-collapsed')) {
                    if(sub.is(':visible')) {
                        sub.slideUp(200, function(){
                            parent.removeClass('nav-active');
                            $('.main-content').css({height: ''});
                            mainContentHeightAdjust();
                        });
                    } else {
                        visibleSubMenuClose();
                        parent.addClass('nav-active');
                        sub.slideDown(200, function(){
                            mainContentHeightAdjust();
                        });
                    }
                }
                return false;
            });
            $timeout(function() {
                $('.menu-list').each(function() {
                    var $parent = $(this);
                    $parent.find('.sub-menu-list li').each(function () {
                        if($(this).attr("class").indexOf("active") > -1) {
                            $parent.addClass("nav-active");
                            return false
                        }
                    })
                });
            },100);

            function visibleSubMenuClose() {
                $('.menu-list').each(function() {
                    var t = $(this);
                    if(t.hasClass('nav-active')) {
                        t.find('> ul').slideUp(200, function(){
                            t.removeClass('nav-active');
                        });
                    }
                });
            }
            $('.toggle-btn').click(function(){
                $(".left-side").getNiceScroll().hide();
                if ($('body').hasClass('left-side-collapsed')) {
                    $(".left-side").getNiceScroll().hide();
                }
                var body = $('body');
                var bodyposition = body.css('position');
                if(bodyposition != 'relative') {
                    if(!body.hasClass('left-side-collapsed')) {
                        body.addClass('left-side-collapsed');
                        $('.custom-nav ul').attr('style','');
                        $(this).addClass('menu-collapsed');
                    } else {
                        body.removeClass('left-side-collapsed chat-view');
                        $('.custom-nav li.active ul').css({display: 'block'});
                        $(this).removeClass('menu-collapsed');
                    }
                } else {
                    if(body.hasClass('left-side-show'))
                        body.removeClass('left-side-show');
                    else
                        body.addClass('left-side-show');
                    mainContentHeightAdjust();
                }
            });

            function mainContentHeightAdjust() {
                var docHeight = $(document).height();
                //if(docHeight > $('.main-content').height())
                //    $('.main-content').height(docHeight);
            }

            $('.custom-nav > li').hover(function(){
                $(this).addClass('nav-hover');
            }, function(){
                $(this).removeClass('nav-hover');
            });
        }


    }]);

    mainCtr.controller("updatePwCtr", ["$scope", "$location", "login", function($scope, $location, login){
        $("#passwordForm").validate({
            rules:{
                originPassword: 'required',
                newPassword: 'required'
            },
            messages:{
                originPassword:{
                    required:"请输入原密码"
                },
                newPassword:{
                    required:"请输入新密码"
                }
            },
            focusInvalid: false,
            onkeyup: false,
            submitHandler: function(){
                login.updatePw({
                    originPassword: $scope.originPassword,
                    newPassword: $scope.newPassword
                }).then(function(){
                    $('#updatePwModal').modal('hide').find("form")[0].reset();
                    swal({
                        title: "修改密码成功",
                        animation: "slide-from-top"
                    });
                },function(data){
                })
            },
            errorPlacement: function(error, element) {
                $('#pwErrorMsg').html(error);
                return false;
            }
        });
    }]);


})(jQuery);