package com.example.administrator.circlemenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {

    private RoundView roundView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        roundView = (RoundView) findViewById(R.id.roundView);

        addMenu();

    }

    private void addMenu() {
        RoundView.RoundMenu menu0 = new RoundView.RoundMenu();
        menu0.resID = R.drawable.home_mbank_1_normal;
        menu0.solidColor = this.getResources().getColor(R.color.trans);
        menu0.strokeColor = this.getResources().getColor(R.color.gray_00000);
        menu0.selectSolidColor = this.getResources().getColor(R.color.gray_9999);
        menu0.text = "menu5";
//        menu0.onClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(Main2Activity.this, "第0个", Toast.LENGTH_SHORT).show();
//            }
//        };
        roundView.addRoundMenu(menu0);

        RoundView.RoundMenu menu1 = new RoundView.RoundMenu();
        menu1.resID = R.drawable.home_mbank_1_normal;
        menu1.solidColor = this.getResources().getColor(R.color.trans);
        menu1.strokeColor = this.getResources().getColor(R.color.gray_00000);
        menu1.selectSolidColor = this.getResources().getColor(R.color.gray_9999);
        menu1.text = "menu0";
//        menu1.onClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(Main2Activity.this, "menu1", Toast.LENGTH_SHORT).show();
//            }
//        };
        roundView.addRoundMenu(menu1);

        RoundView.RoundMenu menu2 = new RoundView.RoundMenu();
        menu2.resID = R.drawable.home_mbank_1_normal;
        menu2.solidColor = this.getResources().getColor(R.color.trans);
        menu2.strokeColor = this.getResources().getColor(R.color.gray_00000);
        menu2.selectSolidColor = this.getResources().getColor(R.color.gray_9999);
        menu2.text = "menu1";
//        menu2.onClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(Main2Activity.this, "menu2", Toast.LENGTH_SHORT).show();
//            }
//        };
        roundView.addRoundMenu(menu2);

        RoundView.RoundMenu menu3 = new RoundView.RoundMenu();
        menu3.resID = R.drawable.home_mbank_1_normal;
        menu3.solidColor = this.getResources().getColor(R.color.trans);
        menu3.strokeColor = this.getResources().getColor(R.color.gray_00000);
        menu3.selectSolidColor = this.getResources().getColor(R.color.gray_9999);
        menu3.text = "menu2";
//        menu3.onClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(Main2Activity.this, "menu3", Toast.LENGTH_SHORT).show();
//            }
//        };
        roundView.addRoundMenu(menu3);

        RoundView.RoundMenu menu4 = new RoundView.RoundMenu();
        menu4.resID = R.drawable.home_mbank_1_normal;
        menu4.solidColor = this.getResources().getColor(R.color.trans);
        menu4.strokeColor = this.getResources().getColor(R.color.gray_00000);
        menu4.selectSolidColor = this.getResources().getColor(R.color.gray_9999);
        menu4.text = "menu3";
//        menu4.onClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(Main2Activity.this, "menu4", Toast.LENGTH_SHORT).show();
//            }
//        };
        roundView.addRoundMenu(menu4);

        RoundView.RoundMenu menu5 = new RoundView.RoundMenu();
        menu5.resID = R.drawable.home_mbank_1_normal;
        menu5.solidColor = this.getResources().getColor(R.color.trans);
        menu5.strokeColor = this.getResources().getColor(R.color.gray_9999);
        menu5.selectSolidColor = this.getResources().getColor(R.color.gray_00000);
        menu5.text = "menu4";
        roundView.addRoundMenu(menu5);

        RoundView.RoundMenu menu6 = new RoundView.RoundMenu();
        menu6.resID = R.drawable.home_mbank_1_normal;
        menu6.solidColor = this.getResources().getColor(R.color.trans);
        menu6.strokeColor = this.getResources().getColor(R.color.gray_9999);
        menu6.selectSolidColor = this.getResources().getColor(R.color.gray_00000);
        menu6.text = "menu5";
        roundView.addRoundMenu(menu6);

        RoundView.RoundMenu menu7 = new RoundView.RoundMenu();
        menu7.resID = R.drawable.home_mbank_1_normal;
        menu7.solidColor = this.getResources().getColor(R.color.trans);
        menu7.strokeColor = this.getResources().getColor(R.color.gray_9999);
        menu7.selectSolidColor = this.getResources().getColor(R.color.gray_00000);
        menu7.text = "menu6";
        roundView.addRoundMenu(menu7);


        roundView.setOnMenuItemClickListener(new RoundView.OnMenuItemClickListener() {
            @Override
            public void itemClick(int pos) {
                Toast.makeText(Main2Activity.this, "第" + pos + "个", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void itemCenterClick() {
                Toast.makeText(Main2Activity.this, "点击了中心位置", Toast.LENGTH_SHORT).show();
            }
        });
        roundView.setCoreMenu(this.getResources().getColor(R.color.gray_9999),
                this.getResources().getColor(R.color.colorAccent), this.getResources().getColor(R.color.trans),
                1, 0.63, null, "中心位置", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(Main2Activity.this, "点击了中心位置", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
