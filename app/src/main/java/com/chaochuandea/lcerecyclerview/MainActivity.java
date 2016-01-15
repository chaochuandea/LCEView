package com.chaochuandea.lcerecyclerview;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.chaochuandea.lcerecyclerview.databinding.ItemBinding;
import com.chaochuandea.lcerecyclerview.databinding.OtherItemBinding;
import com.chaochuandea.lceview.inner.*;
import com.chaochuandea.lceview.inner.Error;
import com.chaochuandea.lceview.lcerecyclerview.BindingHolder;
import com.chaochuandea.lceview.lcerecyclerview.DataSourceAdapter;
import com.chaochuandea.lceview.lcerecyclerview.LCERecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        LCERecyclerView<MyDataEntityt> lceRecyclerView = (LCERecyclerView<MyDataEntityt>) findViewById(R.id.recyclerview);
        lceRecyclerView.setAdapter(new DataSourceAdapter<MyDataEntityt>() {

            @Override
            public void getData(boolean refresh, String url, HashMap<String, Object> params, Class<MyDataEntityt> clazz,final DataSource.RequestDataCallBack<MyDataEntityt> requestDataCallBack) {
                //获取数据的地方
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                            final  MyDataEntityt myDataEntityt = new MyDataEntityt();
                            List<MyItem> data = new ArrayList<MyItem>();
                            for (int i = 0; i < 5; i++) {
                                MyItem item = new MyItem();
                                item.setFace("face--"+i);
                                item.setName("name--" + i);
                                data.add(item);
                            }
                            myDataEntityt.setData(data);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    requestDataCallBack.success(myDataEntityt);
                                }
                            });
                        } catch (InterruptedException e) {
                            requestDataCallBack.error(new Error());
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

            @Override
            public String getUrl() {
                return "这是网络获取的地址";
            }

            @Override
            public HashMap<String, Object> getParams() {
                //这是需求参数
                return null;
            }

            @Override
            public void getMVC(List<MyDataEntityt> myDataEntityts, DealCenter dealCenter) {
                for (int i = 0; i < myDataEntityts.size(); i++) {

                    dealCenter.add(new ModelName<HeaderEntity>("header", new HeaderEntity()), R.layout.header, new Controller<HeaderEntity>() {
                        @Override
                        public void bind(BindingHolder holder, HeaderEntity data, Class<HeaderEntity> data_type, int position) {

                        }
                    });



                    dealCenter.add(new ItemContent(myDataEntityts.get(i).getData()));




                    dealCenter.add(new OtherContent(myDataEntityts.get(i).getData()));

                }
            }
        });
    }
    public class OtherContent extends MVC<MyItem>{

        public OtherContent(MyItem data) {
            super(data);
        }

        public OtherContent(List<MyItem> datas) {
            super(datas);
        }

        @Override
        public String getName() {
            return "other";
        }

        @Override
        public Integer getView() {
            return R.layout.other_item;
        }

        @Override
        public Controller<MyItem> getController() {
            return new Controller<MyItem>() {
                @Override
                public void bind(BindingHolder holder, MyItem data, Class<MyItem> data_type, int position) {
                    OtherItemBinding binding = (OtherItemBinding) holder.getBinding();
                    binding.face.setText("other--"+data.getFace());
                }
            };
        }
    }

    public class ItemContent extends MVC<MyItem>{

        public ItemContent(MyItem data) {
            super(data);
        }

        public ItemContent(List<MyItem> datas) {
            super(datas);
        }

        @Override
        public String getName() {
            return "content";
        }

        @Override
        public Integer getView() {
            return R.layout.item;
        }

        @Override
        public Controller<MyItem> getController() {
            return new Controller<MyItem>() {
                @Override
                public void bind(BindingHolder holder, MyItem data, Class<MyItem> data_type, int position) {
                    ItemBinding binding = (ItemBinding) holder.getBinding();
                    binding.face.setText(data.getFace());
                }
            };
        }
    }
    public class HeaderEntity{

    }
    public class MyDataEntityt{
        int code;
        String text;
        List<MyItem> data = new ArrayList();

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public List<MyItem> getData() {
            return data;
        }

        public void setData(List<MyItem> data) {
            this.data = data;
        }
    }
    public  class MyItem{
        String name;
        String face;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFace() {
            return face;
        }

        public void setFace(String face) {
            this.face = face;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
