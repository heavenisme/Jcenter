package com.heaven.aop.common.permission;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import com.heaven.aop.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

/**
 * FileName: com.heaven.aop.common.permission.AopPermissionActivity.java
 * author: Heaven
 * email: heavenisme@aliyun.com
 * date: 2019-08-30 15:13
 *
 * @author heaven
 * @version V1.0 TODO <描述当前版本功能>
 */
public class AopPermissionActivity extends Activity {
    private static PermissionInfo permissionInfo;


    public static void requestPermission(Context context, PermissionInfo permissionInfo) {
        if(permissionInfo != null) {
            AopPermissionActivity.permissionInfo = permissionInfo;
            Intent intent = new Intent(context, AopPermissionActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
            if (context instanceof Activity) {
                ((Activity) context).overridePendingTransition(0, 0);
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hpermission);
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        if(permissionInfo != null && permissionInfo.permissionListener != null
                && permissionInfo.permissions != null && permissionInfo.permissions.length > 0) {
            requestPermission(permissionInfo);
        } else {
            finish();
        }
    }

    /**
     * 申请权限
     *
     * @param permissionInfo 权限信息
     */
    private void requestPermission(PermissionInfo permissionInfo) {

        if (PermissionUtil.hasSelfPermissions(this, permissionInfo.permissions)) {
            //all permissions granted
            permissionInfo.permissionListener.permissionGranted();
            permissionInfo.permissionListener = null;
            finish();
            overridePendingTransition(0, 0);
        } else {
            boolean shouldShowRequestPermissionRationale = PermissionUtil
                    .shouldShowRequestPermissionRationale(this, permissionInfo.permissions);
            if (TextUtils.isEmpty(permissionInfo.rationale)){
                //request permissions
                ActivityCompat.requestPermissions(this, permissionInfo.permissions, permissionInfo.requestCode);
            }else {
                if (shouldShowRequestPermissionRationale){
                    showRequestPermissionRationale(this, permissionInfo.permissions,permissionInfo.rationale);
                }else {
                    //request permissions
                    ActivityCompat.requestPermissions(this, permissionInfo.permissions, permissionInfo.requestCode);
                }
            }
        }
    }

    public void showRequestPermissionRationale(Activity activity, String[] permissions, String rationale){
        new AlertDialog.Builder(activity)
                .setMessage(rationale)
                .setPositiveButton(R.string.btn_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(activity, permissions, permissionInfo.requestCode);
                    }
                })
                .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                        overridePendingTransition(0, 0);
                    }
                })
                .create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (PermissionUtil.verifyPermissions(grantResults)) {
            //所有权限都同意
                permissionInfo.permissionListener.permissionGranted();
        } else {
            if (!PermissionUtil.shouldShowRequestPermissionRationale(this, permissions)) {
                //权限被拒绝并且选中不再提示
                if (permissions.length != grantResults.length) {
                    return;
                }
                List<String> denyNoAskList = new ArrayList<>();
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        denyNoAskList.add(permissions[i]);
                    }
                }
                permissionInfo.permissionListener.permissionNeverAskDenied(requestCode, denyNoAskList);
            } else {
                //权限被取消
                if (permissionInfo.permissionListener != null) {
                    List<String> denyList = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            denyList.add(permissions[i]);
                        }
                    }
                    permissionInfo.permissionListener.permissionDenied(requestCode, denyList);
                }
            }

        }
        permissionInfo.permissionListener = null;
        finish();
        overridePendingTransition(0, 0);
    }
}
