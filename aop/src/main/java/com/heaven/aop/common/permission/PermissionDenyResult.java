package com.heaven.aop.common.permission;

import java.util.List;

/**
 * FileName: com.heaven.aop.common.permission.PermissionDeny.java
 * author: Heaven
 * email: heavenisme@aliyun.com
 * date: 2019-08-30 15:53
 *
 * @version V1.0 TODO <描述当前版本功能>
 */
public class PermissionDenyResult {
    public int requestCode = -1;
    public boolean isNeverAskDeny = false;
    public List<String> denyPermissionList;
}
