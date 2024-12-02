<template>
  <nav class="navbar navbar-default navbar-fixed-top" role="navigation">
    <div class="container-fluid">
      <div class="navbar-header">
        <a class="navbar-brand" href="">Find</a>
        <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-collapse"
                aria-expanded="false">
          <span class="sr-only">Toggle navigation</span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
        </button>
      </div>
      <div class="collapse navbar-collapse" id="navbar-collapse">
        <ul class="nav navbar-nav">
          <li class=""><a href="/">首页广场</a></li>
          <li class=""><a href="">队伍列表</a></li>
          <li class=""><a href="">用户列表</a></li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
          <li><a href="#">我的邀请</a></li>
          <li><a href="#">我的申请</a></li>
          <li class="dropdown">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
               aria-expanded="false">个人中心<span class="caret"></span></a>
            <ul class="dropdown-menu">
              <li><a href="#">详细信息</a></li>
              <li><a href="#">我的队伍</a></li>
              <li role="separator" class="divider"></li>
              <li><a href="#">退出登录</a></li>
            </ul>
          </li>
        </ul>
        <form class="navbar-form navbar-right">
          <div class="form-group">
            <input type="text" class="form-control" placeholder="Search" id="page-search-input">
          </div>
          <button type="submit" class="btn btn-default">Submit</button>
        </form>
      </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
  </nav><!--导航栏-->
</template>

<script>
import '../assets/jquery-1.12.4.min.js';
import '../assets/bootstrap.min.js';

export default {
  name: 'Navbar'
}
</script>

<style scoped>
@import '../assets/bootstrap.min.css';

.container-fluid {
  height: 70px;
  font-size: 20px;
}
</style>