package com.heaven.aop.common.permission;

/**
 * FileName: com.heaven.aop.common.permission.PermissionInfo.java
 * author: Heaven
 * email: heavenisme@aliyun.com
 * date: 2019-08-30 15:20
 *
 * @version V1.0 TODO <描述当前版本功能>
 */
public class PermissionInfo {
    public int requestCode;//请求码
    public String[] permissions;//请求权限
    public boolean isForce;//是否是强制 false：继续执行方法 true:中断方法执行
    public String rationale;//提示语
    public IHpermission permissionListener;//权限执行监听

}
