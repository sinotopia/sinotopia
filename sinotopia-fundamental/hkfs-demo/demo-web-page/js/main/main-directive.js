/**
 * @angular通用控制器
 * @name DucApp
 * @description 路由，通用拦截器，通用表格渲染等配置
 * @auth yanhui he
 * @time 2016/12/19 16:20
 */
(function($){
    'use strict';
    var mainDirective = angular.module("app.directive", []);

    mainDirective.directive("leftMenu", ["$location", function($location) {
        return {
            restrict: "AE",
            templateUrl: "/left_menu.html",
            controller: function ($scope) {
                $scope.currentPage = $location.path();
            },
            link: function (scope, element, attr) {
            }
        }
    }]);

    mainDirective.directive("repeatMenu", function(){
        return {
            link: function(scope,element,attr){
                if(scope.$last == true){
                    scope.$eval(attr.repeatMenu)
                }
            }
        }
    });





})(jQuery);