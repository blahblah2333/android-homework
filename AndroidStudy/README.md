# AndroidStudy
在GLIDE IMAGE中添加了权限管理：
点击“CHECK SDCARD PERMISSION”可查看是否拥有权限。
若没有，则会申请访问内存，在得到权限后，会重新载入Activity，使SDcard中的图片可以显示
若已有权限，也会提示相应内容
若手动取消授权，则图片无法继续显示

在IJKPLAYER中添加了音量控制：
使用自定义View实现竖直方向的SeekBar
能实现对音量的调节