package com.cathy.sensor;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.and.middleware.AndActivity;

public class DetailActivity extends AndActivity {

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 生成窗口（Activty）， 子窗口（dialog）
     * 窗口里 根视图  ——》DecorView（ViewGroup）
     * 默认界面setConteView （） 都是往DecorView 添加或者移除子View
     * 触摸屏幕  ——》》   按下 acitondown
     *
     * _>根视图——》 触摸的屏幕坐标 落在 视图内的某个子view 类的化 把事件 发送给子View
     * View 的事件坐标就是自身坐标系，与父布局无关
     *
     * 责任链模式
     *
     * 老板接了个活 -> 经理——》主管-》员工
     *
     * 员工干不了——》主管 ——》经理——》老板（根视图）（肯定干）
     *
     * 老板接了个活 -> 经理——》主管(认为员工干不了，自己干，干失败了)   |||-》员工
     * 主管 ——》经理——》老板（根视图）（肯定干）
     *
     *
     * 事件 是 一个系列操作
     *
     * MotionEvent.Action_down  -> action_move -> action_up/action_cancel
     *
     *
     * disppatchTouchEvent（）{
     *
     *    if( lastTargetView ==null &&notdown){
     *        return this.onTouchEvent();
     *
     *    }
     *
     *    if（interceptTouchEvent()||disallowIntercept）{
     *         return onTouchEvent();
     *     }else{
     *         if( view.dispatchTouchEvent()){
     *             return true;
     *         }else{
     *             return onTouchEvent();
     *         }
     *     }
     * }
     *
     *
     * view 拿到了 系列事件且 ActionDown 为结束
     *
     * 默认逻辑是  判断 落下 时间 和抬起时间且move 近似没有移动  ，如果时间 小于某一 阈值  -> 点击事件 即Click事件
     * 如果时间 大于某一 阈值  -> 点击事件 即onLongClick事件——》 如果长安没有消费该事件 ——》 onClick
     *
     *
     *
     *
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_detail);

        LinearLayout contView = new LinearLayout(this);


        TextView textView = findViewById(R.id.textView);
        ViewGroup group = (ViewGroup) textView.getParent();
//        textView.onTouchEvent()
        // 先 onTouchListner 是否消费，-》onClick

        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(v.getContext(),"event= 长安 longClick",Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"event= Click",Toast.LENGTH_SHORT).show();
//                group.removeView(v);
//                ImageView imageView = new ImageView(v.getContext());
//                imageView.setImageResource(R.mipmap.ic_launcher);
//                group.addView(imageView);
//                getIntent().putExtra("value","第二个界面传的值2");
//                setResult(RESULT_OK,getIntent());
//                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
