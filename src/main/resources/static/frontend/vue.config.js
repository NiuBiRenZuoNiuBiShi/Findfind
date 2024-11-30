const { defineConfig } = require('@vue/cli-service')
module.exports = {
  // 你的配置...
  pluginOptions: {
    'html-webpack-plugin': {
      // html-webpack-plugin 配置...
      template: 'src/index-template.html', // 指定你的自定义模板文件
      filename: 'index.html', // 输出文件名
    }
  }
};
