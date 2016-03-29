 Date.prototype.weekdays = "日一二三四五六".split("");
 Date.prototype.months = "一,二,三,四,五,六,七,八,九,十,十一,十二".split(",");
 Date.prototype.format = function(format){
     var o = {
         "Y"  : this.months[this.getMonth()], //汉字月份
         "M+" : this.getMonth()+1, //month
         "d+" : this.getDate(),    //day
         "h+" : this.getHours(),   //hour
         "m+" : this.getMinutes(), //minute
         "s+" : this.getSeconds(), //second
         "q+" : Math.floor((this.getMonth()+3)/3), //quarter
         "S"  : this.getMilliseconds(), //millisecond
         "D"  : this.weekdays[this.getDay()] //星期
     }
     if(/(y+)/.test(format)) format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
     for(var k in o)
         if(new RegExp("("+ k +")").test(format))
             format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));
     return format;
 }

window.CC = {
    ver: "1.0",
    callback : {},
    loadArticle: function(id) {
        alert(id);
    },
    parseNews : function(source, limit) {
        var url_tag_begin = "<URL>", url_tag_end = "</URL>", title_tag_begin = "<TITLE><![CDATA[", title_tag_end = "]]></TITLE>";
        var list = [];
        var url_begin, url_end, title_begin, title_end;
        url_begin = url_end = title_begin = title_end = 0;

        if(!source) return;
        if(!(limit > 0 && limit <= 10)) limit = 5;
        for(var i=0;i<limit;i++) {
            url_begin = source.indexOf(url_tag_begin, url_begin+1);
            url_end = source.indexOf(url_tag_end, url_end+1);
            title_begin = source.indexOf(title_tag_begin, title_begin+1);
            title_end = source.indexOf(title_tag_end, title_end+1);
            var url = source.substring(url_begin + url_tag_begin.length, url_end);
            url = url.replace("xml/site1", "site1").replace(".xml", ".html");

            list.push("<a href='"+url+"'>"+source.substring(title_begin + title_tag_begin.length, title_end)+"</a>");
            //list.push({index:i, title: source.substring(url_begin + url_tag_begin.length, url_end),
            //    url:url});
        }
        return list.join("");
        // return {
        //     length:i,
        //     list: list
        // };
    },
    getDateTime: function() {
        return new Date().format("yyyy年MM月dd日 星期D");
    },
    parseWeather: function(raw) {
        var fileNames = {"阵雨":"rain1","中雨":"rain2","中雪":"snow2","多云":"cloudy","大雨":"rain3",
            "大雪":"snow3","小雨":"rain1","小雪":"snow1","晴":"sunny","暴雨":"rain4",
            "暴雪":"snow4","阴":"gloomy","雷阵雨":"thunder"}, info;

        if(raw == "error") return raw;

        info = JSON.parse(raw);

        if(info && info.weatherinfo)
            info = info.weatherinfo;
        else
             return "error";
         
        info.icon = "img/weather/" + fileNames[info.weather.split("转")[0]] + ".png";
        
        //var raw = window.jsAPI.getWeather(false);
        return info;
    }
}

$(function(){
    if(typeof window.jsAPI != "object") return;
    $("a").on("tap", function(){
        window.jsAPI.openPage(this.href);
        return false;
    });
})