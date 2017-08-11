/**
 * @angular通用配置
 * @name DucApp
 * @description 路由，通用拦截器，通用表格渲染等配置
 * @auth yanhui he
 * @time 2016/12/19 16:20
 */
'use strict';

var serverUrl = "";
angular.module('DucApp',  [
    'ngRoute',
    'ngAnimate',
    'app.service',
    'app.filter',
    'app.controller',
    'app.directive'
])
    .factory('appInterceptor', ['$q', '$window', function ($q, $window) {
        return {
            request:function(config){
                return config;
            },
            response:function(response) {
                var retCode = response.data.retCode;
                if(!!retCode && retCode == "4005") {
                    $window.location.href = '/login.html';
                    return false;
                }else if(!!retCode && retCode != "1") {
                   var msg = !!response.data.retMsg ? response.data.retMsg : response.retMsg;
                    $.gritter.add({
                        text: !!msg ? msg :'数据连接失败...',
                        time: 2000,
                        sticky: false,
                        class_name: 'my-sticky-class'
                    });
                }
                return response;
            },
            responseError:function(response){
                var msg = !!response.data.retMsg ? response.data.retMsg : response.retMsg;
                $.gritter.add({
                    text: !!msg ? msg :'数据连接失败...',
                    time: 2000,
                    sticky: false,
                    class_name: 'my-sticky-class'
                });
            }
        };
    }])


    .run(['$rootScope',  '$location', 'login', '$http', 'locals', '$window',  function($rootScope, $location, login, $http, locals, $window){
        $rootScope.$on('$routeChangeStart', function($event){
            $rootScope.childMenu = $location.$$path;
        });

        //login.getUserInfo().then(function(data){
        //    $rootScope.userId =data.userId;
        //    $rootScope.loginInfo = data;
        $rootScope.personName = locals.getItem("personName") || "admin";
        //    $rootScope.$broadcast('resUserInfo', data);
        //});


        $rootScope.logout = function($event){
            login.logout().then(function(){
                $window.location.href = '/login.html'
            })
        };

    }])

    .config(function ($routeProvider, $httpProvider, $locationProvider) {
        $locationProvider.html5Mode(false);
                    $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';
                    if (!$httpProvider.defaults.headers.get) {
                        $httpProvider.defaults.headers.get = {};
                    }
                    $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
                    $httpProvider.defaults.headers.get['Cache-Control'] = 'no-cache';
                    $httpProvider.defaults.headers.get['Pragma'] = 'no-cache';
                    $httpProvider.defaults.transformRequest = [function (data) {
                        var param = function (obj) {
                            var query = '';
                            var name, value, fullSubName, subName, subValue, innerObj, i;

                            for (name in obj) {
                    value = obj[name];
                    if (value instanceof Array) {
                        for (i = 0; i < value.length; ++i) {
                            subValue = value[i];
                            fullSubName = name + '[]';
                            innerObj = {};
                            innerObj[fullSubName] = subValue;
                            query += param(innerObj) + '&';
                        }
                    } else if (value instanceof Object) {
                        for (subName in value) {
                            subValue = value[subName];
                            fullSubName = subName;
                            innerObj = {};
                            innerObj[fullSubName] = subValue;
                            query += param(innerObj) + '&';
                        }
                    } else if (value !== undefined && value !== null) {
                        query += encodeURIComponent(name) + '='
                        + encodeURIComponent(value) + '&';
                    }
                }
                return query.length ? query.substr(0, query.length - 1) : query;

            };
            return angular.isObject(data) && String(data) !== '[object File]' ? param(data) : data;
        }];


        //添加拦截器
        $httpProvider.interceptors.push('appInterceptor');
        $routeProvider
            .when('/',{
                templateUrl:'/welcome.html'
            })

            .when('/admin_user_list.html', {
                templateUrl: '/admin_user_list.html',
                controller: 'adminUserListCtrl'
            })
            .when('/admin_role_list.html', {
                templateUrl: '/admin_role_list.html',
                controller: 'adminRoleListCtrl'
            })
            .when('/admin_menu_list.html', {
                templateUrl: '/admin_menu_list.html',
                controller: 'adminMenuListCtrl'
            })



            .otherwise({
                redirectTo: '/'
            })
    });




