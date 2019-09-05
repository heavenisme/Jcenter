package com.heaven.aop.common.permission;

import java.util.List;

/**
 * FileName: com.heaven.aop.common.permission.IHpermission.java
 * author: Heaven
 * email: heavenisme@aliyun.com
 * date: 2019-08-30 15:19
 *
 * @author heaven
 * @version V1.0 TODO <描述当前版本功能>
 */
public interface IHpermission {
    //同意权限
    void permissionGranted();

    //拒绝权限并且选中不再提示
    void permissionNeverAskDenied(int requestCode, List<String> denyNoAskList);

    //取消权限
    void permissionDenied(int requestCode, List<String> denyList);
}
