package com.example.administrator.circlemenu;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.circlemenu.util.ToastUtil;

public class MainActivity extends AppCompatActivity {


    private CircleMenuLayout mCircleMenuLayout;

    private String[] mItemTexts = new String[]{"白天模式", "夜间模式", "0",
            "0", "0", "0", "0", "0"};
    private int[] mItemImgs = new int[]{R.drawable.icon_sun,
            R.drawable.icon_moon, 0,
            0, 0,
            0, 0,
            0};
    private int[] mItemImgsPress = new int[]{R.drawable.home_mbank_1_normal,
            R.drawable.home_mbank_2_normal, 0,
            0, 0,
            0, 0,
            0};
    private Button shake;
    private CustomLayout customLayout;
    private TextView new_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //自已切换布局文件看效果
        setContentView(R.layout.activity_main);
        mCircleMenuLayout = (CircleMenuLayout) findViewById(R.id.id_menulayout);
        customLayout = (CustomLayout) findViewById(R.id.customLayout);
        new_item = (TextView) findViewById(R.id.new_item);
        mCircleMenuLayout.setMenuItemIconsAndTexts(mItemImgs, mItemTexts);
        mCircleMenuLayout.setMenuItemIconsPress(mItemImgsPress);
        mCircleMenuLayout.setOnMenuItemClickListener(new CircleMenuLayout.OnMenuItemClickListener() {

            @Override
            public void itemClick(View view, int pos) {
                Toast.makeText(MainActivity.this, mItemTexts[pos],
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void itemCenterClick(View view) {
                Toast.makeText(MainActivity.this,
                        "you can do something just like ccb  ",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, Main2Activity.class));
                startActivity(new Intent(MainActivity.this, volidateActivity.class));

            }
        });
        customLayout.setCustomLayoutListener(new CustomLayout.customLayoutListener() {
            @Override
            public void onRingRelease(int location) {
                ToastUtil.show(MainActivity.this, location + "");
                showAlert();
            }

            @Override
            public void outRingRelease() {

            }
        });

        shake = (Button) findViewById(R.id.shake);
        //震动布局监听
        findViewById(R.id.shake).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCircleMenuLayout.startShake();
            }
        });
        //停止震动
        findViewById(R.id.stop_shake).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCircleMenuLayout.stopShake();
            }
        });
    }

    private void showAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("提示");
        alertDialog.setMessage("是否替换该项???");
        alertDialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new_item.setVisibility(View.VISIBLE);
            }
        });
        alertDialog.create();
        alertDialog.show();
    }

}
