$(function() {
    $("nav a").on("click", function(e) {
        var t = $(this);this.blur();
        $("nav a").removeClass("on");
        t.addClass("on");
        var s = this.href;
        s = s.substr(s.indexOf("#")+1);
        $(".block").hide();
        $("."+s).show();
    });
});