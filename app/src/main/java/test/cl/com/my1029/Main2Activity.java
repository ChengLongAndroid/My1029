package test.cl.com.my1029;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

public class Main2Activity extends AppCompatActivity {

    public EditText edtv;
    public Button bt;
    JZVideoPlayerStandard jzVideoPlayerStandard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
         jzVideoPlayerStandard= (JZVideoPlayerStandard) findViewById(R.id.tv);
        edtv= (EditText) findViewById(R.id.edtv);

        mvplay(edtv.getText().toString());

    }

    public void cctv(View view){
        mvplay(edtv.getText().toString());

    }

    public void mvplay(String mv){
        jzVideoPlayerStandard.setUp(edtv.getText().toString(),JZVideoPlayerStandard.SCREEN_LAYOUT_NORMAL,"哈哈哈哈");  //设置路径,模式，标题就可以开始播放了
    }

    @Override
    public void onBackPressed() {// 点击返回
        if (JZVideoPlayer.backPress()){  //backPress方法，如果是全屏就会退回小屏幕，并return结束onBackPressed方法，而不是关掉Activity
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() { //被覆盖后释放所有的Videos
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }
}
