lceview 使用
=======

首先明确几个关键概念


1. LCE （loading，content，error）这三种状态
2. refresh，loadmore，这两种功能

在以往开发过程中，我们使用recyclerview的场景是很多的，特别是获取数据，然后填充到列表中。

现在我们来看看使用这个lceview 有多快吧
我们模拟网络请求，并显示如下
![](https://github.com/chaochuandea/LCEView/blob/master/screen/device-2015-12-31-112557.png)

1. 在build.gradle中

    ```
    dataBinding {
        enabled true
    }
    ```
    
    ```
     compile project(":lceview")
    ```
2. 在layout中

    ```
    <com.chaochuandea.lceview.lcerecyclerview.LCERecyclerView
           android:layout_width="match_parent"
           android:id="@+id/recyclerview"
           android:layout_height="match_parent"/>
    ```
3. 声明数据源

    ```
     public class MyDataEntityt{
        int code;
        String text;
        List<MyItem> data = new ArrayList();
        
        ...set and get method
        
    }
    
    public class MyItem{
            String name;
            String face;
            
            ...set and get method
    }
    
    public class HeaderEntity{
        
    }
    ```
4. 声明两个layout,header.xml,item.xml

    header.xml    
    ```
    <?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <data>
        <variable
            name="data"
            type="com.chaochuandea.lcerecyclerview.MainActivity.HeaderEntity"/>
    </data>
    <LinearLayout
        android:orientation="vertical" android:layout_width="match_parent"
        android:layout_height="wrap_content">
            <TextView
                android:layout_width="match_parent"
                android:text="i am header"
                android:layout_height="wrap_content" />
    </LinearLayout>
</layout>
    ```
    item.xml
    ```
    <?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <data>
        <variable
            name="data"
            type="com.chaochuandea.lcerecyclerview.MainActivity.MyItem"/>
    </data>
    <LinearLayout
        android:orientation="vertical" android:layout_width="match_parent"
        android:layout_height="wrap_content"
       >
        <TextView
            android:layout_width="wrap_content"
            android:text="@{data.name}"
            android:layout_height="wrap_content" />
        <TextView
            android:layout_width="wrap_content"
            android:id="@+id/face"
            android:layout_height="wrap_content" />
        <View
            android:layout_width="match_parent"
            android:background="#a2a2a2"
            android:layout_height="0.5dp"/>
    </LinearLayout>
</layout>
    ```
5. 开始绑定了
    ```
    LCERecyclerView<MyDataEntityt> lceRecyclerView = (LCERecyclerView<MyDataEntityt>) findViewById(R.id.recyclerview);
        lceRecyclerView.setAdapter(new DataSourceAdapter<MyDataEntityt>() {
            @Override
            public void getData(String url, HashMap<String, Object> params, Class<MyDataEntityt> clazz, final DataSource.RequestDataCallBack<MyDataEntityt> requestDataCallBack) {
                //获取数据的地方
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
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
            public void getViewTypeAndLayout(MV<Class, Integer> mv) {
                //item 数据和layout 的对应关系
                mv.with(MyItem.class,R.layout.item)
                .with(HeaderEntity.class,R.layout.header);
            }

            @Override
            public List<Object> onDataChange(List<MyDataEntityt> myDataEntityts) {
                List<Object> deals = new ArrayList<Object>();

                deals.add(new HeaderEntity());
                for (int i = 0; i < myDataEntityts.size(); i++) {
                    deals.addAll(myDataEntityts.get(i).getData());
                }
                return deals;
            }

            @Override
            public void onBind(BindingHolder holder, Object data, Class data_type, int position) {
                if (data instanceof MyItem){
                    MyItem item = (MyItem) data;
                    ItemBinding itemBinding = (ItemBinding) holder.getBinding();
                    itemBinding.face.setText(item.getFace());
                }
            }
        });
    ```
