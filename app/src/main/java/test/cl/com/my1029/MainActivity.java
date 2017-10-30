package test.cl.com.my1029;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public MediaPlayer mediaPlayer;
    public Button button1,button2,button3,button4;
    public EditText ed;
    public SimpleAdapter sa;//简单适配器
    public ListView lv;
    public static List<Map<String,Object>> list;  //这个集合用来存音频文件路径的
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1= (Button) findViewById(R.id.button1);
        button2= (Button) findViewById(R.id.button2);
        button3= (Button) findViewById(R.id.button3);
        button4= (Button) findViewById(R.id.button4);
        lv= (ListView) findViewById(R.id.listv);
        ed= (EditText) findViewById(R.id.ed);

        list=getlist();

        String[] from = {"abc"};//Map对象的键数组
        int[] to = {R.id.textView};//从这个组件开始顺序填充list数据
        sa = new SimpleAdapter(this, list, R.layout.listview, from, to);

  /*SimpleAdapter的参数说明
         * 第一个参数 表示访问整个android应用程序接口，基本上所有的组件都需要
         * 第二个参数表示生成一个Map(String ,Object)列表选项
         * 第三个参数表示界面布局的id  表示该文件作为列表项的组件
         * 第四个参数表示该Map对象的哪些key对应value来生成列表项
         * 第五个参数表示来填充的组件 Map对象key对应的资源一依次填充组件 顺序有对应关系
*/
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() { //建立监听ListView点击事件，i变量为点击item的位置
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String,Object> map =list.get(i);  //获取那一行的Map数据，开始处理
                String filepath=null;
//  想在没有键的情况下获取值，Map类提供了一个称为entrySet()的方法，这个方法返回一个Map.Entry实例化后的对象集。接着，Map.Entry类提供了一个getKey()方法和一个getValue()方法
                for(Map.Entry<String,Object> e: map.entrySet()){
                    filepath=((File)e.getValue()).getPath().toString();//强转为File类，用getPath获取路径
                }

                if(mediaPlayer!=null&&mediaPlayer.isPlaying()){  //判断是否在播放音乐
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer=null;
                }


                Musicplay(filepath);//路径放入音乐播放函数
                Toast.makeText(MainActivity.this,"当前显示为"+i,Toast.LENGTH_SHORT).show();
            }


        });

    }




    public List<Map<String,Object>> getlist() {  //获取指定文件夹下所有音频文件路径的
        List<Map<String,Object>> list2 =new ArrayList<>();
        String sdString= Environment.getExternalStorageState();  //获取存储卡状态
        if(sdString.equals(Environment.MEDIA_MOUNTED)){  //MEDIA_MOUNTED为正常可用
            File sdpath=new File((Environment.getExternalStorageDirectory()+"/netease/cloudmusic/Music"));//拿到根目录添加文件路径
            if(sdpath.listFiles().length>0){//判断是否有文件
                for (File file : sdpath.listFiles()){    //遍历添加到Map集合中
                    Map<String,Object> map=new HashMap<>();
                    map.put("abc",file);
                    list2.add(map);
                }
            }
        }
        return list2;
    }



    public void Musicplay(String music){ //播放音乐函数
        try{
            mediaPlayer=new MediaPlayer();
            mediaPlayer.setDataSource(music);  //添加数据源
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);   //设置流媒体的类型，AudioManager可以修改系统的情景模式为音乐 可以后台播放
            mediaPlayer.prepareAsync();//异步准备
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {//准备完毕后调用
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();//开始播放音乐
                    button1.setEnabled(false);//设置按钮为隐藏
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {//放完一首歌后调用
                @Override
                public void onCompletion(MediaPlayer mp) {
                    button1.setEnabled(true);//设置按钮为显示
                }
            });

        }catch (Exception e){}
    }


    public void mv(View view){//视频监听Button
        startActivity(new Intent().setClass(MainActivity.this,Main2Activity.class));//跳转视频Activity
    }

    public void sm(View view){
        lv.setAdapter(sa);//ListView获取适配器
    }


    public  void play(View view){//播放按钮监听

        Musicplay(ed.getText().toString());//文本编辑框的数据放入音乐播放函数

    }
    public  void pause(View view){//暂停按钮监听
        if("继续".equals(button2.getText().toString())){//对比按钮Test属性，"继续"说明音乐是暂停的
            mediaPlayer.start();
            button2.setText("暂停");
            return;
        }
        if(mediaPlayer!=null&&mediaPlayer.isPlaying()){  //音乐在播放，用paus函数暂停
            mediaPlayer.pause();
            button2.setText("继续");
        }


    }
    public  void stop(View view){//停止按钮监听
        if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
        }
        button2.setText("暂停");
        button1.setEnabled(true);

    }
    public  void replay(View view){//重新播放按钮监听
        if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
            mediaPlayer.seekTo(0); //重音频最开始的地方播放
        }else {
            play(view);
        }
        button2.setText("暂停");

    }




}
