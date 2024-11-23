document.addEventListener("DOMContentLoaded", function() {
    var backToTopButton =document.getElementById("backToTopButton");
    // 监听滚动事件
    window.addEventListener("scroll", function() {
        if (document.body.scrollTop > 100 || document.documentElement.scrollTop > 20) {
            backToTopButton.style.opacity = "0.5"; // 当滚动超过20px时显示按钮
        } else {
            backToTopButton.style.opacity = "0"; // 否则隐藏按钮
        }
    });
    // 点击按钮时，平滑滚动到页面顶部
    backToTopButton.addEventListener("click", function() {
        document.body.scrollTop = 0; // 对于Safari
        document.documentElement.scrollTop = 0; // 对于Chrome, Firefox, IE和Opera
    });
});