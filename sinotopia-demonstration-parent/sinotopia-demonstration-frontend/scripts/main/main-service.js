/**
 * @angular通用服务层
 * @name DucApp
 * @description
 * @auth yanhui he
 * @time 2016/12/19 16:20
 */

(function($){
    'use strict';

    var mainService = angular.module("app.service", []);

    mainService.factory('http', ['$http', '$q', function ($http, $q) {
        function ajax(params,apiurl,urlTag){
            var params = params || {};
            var defer = $q.defer();
            var serverGetUrl = urlTag == 'export' ? "" : serverUrl;
            $http({
                url: serverGetUrl + apiurl,
                method: 'get',
                params: params
            }).success(function (data) {
                if(urlTag == 'export' && !data.retCode){
                    defer.resolve(data);
                }else if (data.retCode == '1') {
                    var res = data.data || {};
                    res.retCode =  data.retCode;
                    defer.resolve(res);
                } else {
                    defer.reject({
                        retMsg: data.retMsg
                    });
                }
            }).error(function (data) {
                defer.reject({
                    retMsg: data.retMsg || '数据连接失败...'
                });
            });

            return defer.promise;
        }

        function post(params,apiurl,successCallback){
            successCallback = successCallback || function (data) {
                if (data.retCode == '1') {
                    var res = data.data || {};
                    // res.retCode =  data.retCode;
                    defer.resolve(res);
                } else {
                    defer.reject({
                        retMsg: data.retMsg
                    });
                }

            };

            var params = params || {};
            var defer = $q.defer();
            $http({
                url: serverUrl + apiurl,
                method: 'POST',
                data: params
            }).success(successCallback)
            .error(function (data) {
                defer.reject({
                    retMsg: data.retMsg || '数据连接失败...'
                });

            });

            return defer.promise;
        }
        return {
            ajax: ajax,
            post: post
        };
    }]);

    mainService.factory('locals',['$window',function($window){
        return {
            setItem :function(key,value){
                $window.localStorage[key]=value;
            },
            getItem:function(key,defaultValue){
                return  $window.localStorage[key] || defaultValue;
            },
            setObject:function(key,value){
                $window.localStorage[key]=JSON.stringify(value);
            },
            getObject: function (key) {
                return JSON.parse($window.localStorage[key] || '{}');
            }
        }
    }]);



    mainService.factory("login", ["$http", "$q", "http", "$window", function($http, $q, http, $window) {
        function getUserInfo(params) {
            var params = params || {};
            var url = "/accounting/statistics/indexFlowStatistics";
            return http.post(params, url);
        }

        function getSessionMenuList(params) {
            var params = params || {};
            var url = "/admin-web/admin/menu/getSessionMenuList";
            return http.post(params, url);
        }

        function logout(params) {
            var params = params || {};
            var url = "/admin-web/admin/user/logoutUser";
            return http.post(params, url, function(data) {
                location.href = '/login.html'
            });
        }

        function updatePw(params) {
            var params = params || {};
            var url = "/accounting/admin/user/modifyPassword";
            return http.post(params, url);
        }

        return {
            getUserInfo: getUserInfo,
            getSessionMenuList: getSessionMenuList,
            logout: logout,
            updatePw: updatePw
        };
    }]);

    mainService.factory("$ducTools", ["http", "$q",'$window',function(http, $q, $window){
        function retrieveData (sSource, aoData, fnCallback) {
           $.ajax({
                type: "POST",
                url:  serverUrl + sSource,
                dataType: "json",
                data: aoData,
               cache: false,
               success: function (resp) {
                    if(!resp){
                        resp = {};
                        resp.data = {list: [], totalCount: 0};
                    }
                    responseDatatableData = resp.data;
                    var responseData = {};
                    if(resp.retCode == "4005"){
                        $window.location.href = "/login.html";
                        return false;
                    }
                    if(resp.retCode != "1"){
                        $.gritter.add({
                            text: !!resp.retMsg ? resp.retMsg :'数据连接失败...',
                            time: 2000,
                            sticky: false,
                            class_name: 'my-sticky-class'
                        });
                    }

                    if(resp.data.totalCount != undefined && resp.data.dataList != undefined){
                        responseData.totalCount = resp.data.totalCount;
                        responseData.retCode = resp.retCode;
                        responseData.data = resp.data.dataList;
                        responseData.data.otherData = resp.data.dataMap;
                    }else if(resp.data.totalCount != undefined && resp.data.list != undefined){
                        responseData.totalCount = resp.data.totalCount;
                        responseData.retCode = resp.retCode;
                        responseData.data = resp.data.list;
                    }else {
                        responseData = resp
                    }
                    fnCallback (responseData);
                }
            });
        }

        function deleteDataTable(_url, idName, id, classId, title, data){
            var name = idName == "" ? 'id' : idName;
            var titleText = !title ? "删除" : title;
            swal({
                    title: "是否确认" + titleText,
                    showCancelButton: true,
                    closeOnConfirm: false,
                    confirmButtonText: titleText,
                    confirmButtonColor: "#046dae",
                    cancelButtonText: "取消",
                    animation: "slide-from-top"
                },
                function(inputValue){
                    if (inputValue === true){
                        http.post(data,_url + '?' + name + '=' + id)
                            .then(function (result) {
                                swal({
                                        title: titleText + "成功！",
                                        confirmButtonText: "确定"
                                    },
                                    function(inputValue){
                                        if (inputValue === true) {
                                            if(classId != "") {
                                                refreshDataTable(classId)
                                            }
                                            swal.close();
                                        }
                                    });

                            });
                    }
                });
        }

        function refreshDataTable (classId) {
            var $class = jQuery("#"+classId);
            var start = $class.dataTable().fnSettings()._iDisplayStart;
            var total = $class.dataTable().fnSettings().fnRecordsDisplay();
            $class.DataTable().draw(false);
            if((total-start)==1){
                if (start > 0) {
                    $("#" + classId).dataTable().fnPageChange( 'previous', true );
                }
            }
        }

        function getQueryString(paras) {
            var url = $window.location.href, j;
            var paraString = url.substring(url.indexOf("?") + 1, url.length).split("&");
            var paraObj = {};
            for (var i = 0; j = paraString[i]; i++) {
                paraObj[j.substring(0, j.indexOf("=")).toLowerCase()] = j.substring(j.indexOf("=") + 1, j.length);
            }
            var returnValue = paraObj[paras.toLowerCase()];
            if (typeof (returnValue) == "undefined") {
                return "";
            } else {
                return returnValue;
            }
        }

        function exportFile(data, url) {
            var arr = [];
            var fmt = function(s) {
                if (typeof s == 'object' && s != null) return json2str(s);
                return /^(string|number)$/.test(typeof s) ? s : s;
            };
            for (var i in data) arr.push(i + "=" + fmt(data[i]));
            $("#exportFileForm")
                .attr("action", "/web/export?url=" + url+ arr.join('&'))
                .submit();
        }

        return {
            getQueryString : getQueryString,
            retrieveData: retrieveData,
            deleteDataTable: deleteDataTable,
            refreshDataTable: refreshDataTable,
            exportFile: exportFile
        }
    }]);

    mainService.factory("$dataTableSet", ["http", "$q",'$window',function(http, $q, $window){
        function dataTableRowShow(id){
            $('#'+ id +' tbody td').live( 'click', function () {
                var nTrs = $('#'+ id).dataTable().fnGetNodes();
                var getRow = $(this).context._DT_CellIndex.row;
                var selAttr = $(this).data('keyName');
                if (!!selAttr)
                    var content = $('#'+ id).dataTable().fnGetData(nTrs[getRow])[selAttr];
                    if(!!content)
                        swal(content)
            });
        }

        return {
            dataTableRowShow : dataTableRowShow
        }
    }])


})(jQuery);