/**
 * @angular通用控制器
 * @name DucApp
 * @description
 * @auth yanhui he
 * @time 2016/12/19 16:20
 */

(function($){
    var mainFilter = angular.module("app.filter", []);

    mainFilter.filter("leftMenuIco", function() {
        return function(str){
            var menuIco = ['money', 'clipboard', 'users', 'align-left', 'btc', 'star', 'user', 'bar-chart-o', 'file', 'flag', 'suitcase', 'shield', 'cny', 'list-ul', 'heart', 'archive', 'comments']
            return menuIco[str]
        }
    });

    mainFilter.filter("toFixed2", function() {
        return function(str){
            str = str|| 0;
            return str.toFixed(2) || 0
        }
    });

    mainFilter.filter("isChildMenu", function($location) {
        return function(str){
            if ($location.url().indexOf(str.replace('/#/', '')) > -1 ) {
                return true;
            }else {
                return false;
            }
        }
    });

    mainFilter.filter("companyCardType", function() {
        return function(index){
            var str = ['非浙商卡', '浙商卡'];
            return str[index - 1]
        }
    });

    mainFilter.filter("companyDepositoryName", function() {
        return function(index){
            var str = ['姓名', '企业法人'];
            return str[index - 1]
        }
    });

    mainFilter.filter("companyDepositoryCard", function() {
        return function(index){
            var str = ['身份证号', '法人身份证号'];
            return str[index - 1]
        }
    });


    mainFilter.filter("showPicDepository", function() {
        return function(str){
            return !str ? "/images/no-pic.png" : str
        }
    });

    mainFilter.filter("supportDepositoryText", function() {
        return function(index){
            var str = ['否', '是'];
            return str[index-1]
        }
    });

    mainFilter.filter("investmentStatus", function() {
        return function(index){
            var str = ['等待付款', '付款成功', '付款失败', '取消付款'];
            return str[index-1]
        }
    });

    mainFilter.filter("repayAssignTypeText", function() {
        return function(index){
            var str = ['到期还本付息', '等额本息', '先息后本'];
            return str[index-1]
        }
    });

    mainFilter.filter("formatNumDigit", function(){
       return function(str) {
           str = str.toString();
           var newStr = "";
           var count = 0;
           if (str.indexOf(".") == -1) {
               for (var i = str.length - 1; i >= 0; i--) {
                   if (count % 3 == 0 && count != 0) {
                       newStr = str.charAt(i) + "," + newStr;
                   } else {
                       newStr = str.charAt(i) + newStr;
                   }
                   count++;
               }
               str = newStr;
               return (str)
           } else {
               for (var i = str.indexOf(".") - 1; i >= 0; i--) {
                   if (count % 3 == 0 && count != 0) {
                       newStr = str.charAt(i) + "," + newStr;
                   } else {
                       newStr = str.charAt(i) + newStr;
                   }
                   count++;
               }
               str = newStr + (str + "00").substr((str + "00").indexOf("."), 3);
               return (str)
           }
       }
    })
})(jQuery);