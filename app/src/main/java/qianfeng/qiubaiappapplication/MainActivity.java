package qianfeng.qiubaiappapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String httpUrl = "http://m2.qiushibaike.com/article/list/text?page=%d";

    private int pageCount = 1;

    private MyBaseAdapter myAdapter;

    private List<User> userList;
    private ListView lv;

    private int top;
    private int firstVisiblePosition;

    private ClickState isClick = ClickState.DEFAULT; // 这个是用于界面传递的，是传递给BaseAdapter的信息

    private ClickState[] states;

    private boolean isgood = true; // 如果isgood被点击了，那isNogood就不能被点击
    private boolean isNogood = true; // 如果isNogood被点击了，那么isgood就不能被点击
                                       // 交叉进行，有点像生产者和消费者模式。

    private boolean temp = true;  // 一个临时变量，避免isgood和isNogood之间只能执行一个的尴尬

    private int num;

    MyBaseAdapter adapter;

    private ImageButton ib_good;
    private ImageButton ib_nogood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView)findViewById(R.id.lv);

//        // 这两行代码都抢不到焦点的。
//        ib_good = ((ImageButton) findViewById(R.id.ib_good));
//        ib_nogood = ((ImageButton) findViewById(R.id.ib_nogood));

//        LayoutInflater inflater = LayoutInflater.from(this);
//        View inflate = (View) inflater.inflate(R.layout.listview_item,null);
//        ib_good = ((ImageButton) inflate.findViewById(R.id.ib_good));

        userList = new ArrayList<>();
        //   userList = new ArrayList<>();
        new MyTask().execute(String.format(httpUrl,pageCount));

//        -----------------
        /*
        现在获取一页的数据成功了，接下来要获取多页
         */
    }

//    public void nogood_click(View view) {
//
//        lv.setAdapter(myAdapter);
//        myAdapter.notifyDataSetChanged();
//        Log.d("google-my:", "nogood_click: " + "ongood_click_Main成功执行");
//
//    }
//
//    public void good_click(View view) {
//
//        lv.setAdapter(myAdapter);
//        myAdapter.notifyDataSetChanged();
//        Log.d("google-my:", "nogood_click: " + "onNotgood_click_Main成功执行");
//    }


    private class MyTask extends AsyncTask<String,Void,List<User>>
    {



        @Override
        protected List<User> doInBackground(String... params) {
            byte[] http = HttpUtils.getHttp(String.format(params[0]));
            String str = new String(http,0,http.length);
            Log.d("google-my:", "doInBackground: " + str); // 网络请求没有问题

            List<User> users = parseJSON(str);

            return parseJSON(str);
        }

        private List<User> parseJSON(String s)
        {
            List<User> list = new ArrayList<>();
          // 开始解析
            try {
                JSONObject object = new JSONObject(s);
                JSONArray items = object.getJSONArray("items");
                for (int i = 0; i < items.length(); i++) {
                    JSONObject jsonObject = items.getJSONObject(i);
                    String format = jsonObject.getString("format");
                    String image = jsonObject.getString("image");
                    String content = jsonObject.getString("content");
                    String comments_count = jsonObject.getString("comments_count");
                    String share_count = jsonObject.getString("share_count");


                    String type = null;
                    try {
                        type = jsonObject.getString("type");
                        if( type != null)
                        {
                             type = jsonObject.getString("type");
                        }else
                        {
                            type = null;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    String login = null;
                    String id = null;
                    String icon = null;
                    JSONObject  user = jsonObject.getJSONObject("user");
                    if(user != null) {

                         login = user.getString("login");
                         id = user.getString("id");
                         icon = user.getString("icon");

                    }


                    JSONObject votes = jsonObject.getJSONObject("votes");
                    String down = null;
                    String up = null;
                    if(votes != null) {
                         down = votes.getString("down");
                         up = votes.getString("up");
                    }
                    User us = new User(format,image,login,id,icon,down,up,content,comments_count,share_count,type);
                    Log.d("google-my:", "parseJSON: " + us);
                    list.add(us);
                }
                return list;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<User> users) {

            userList.addAll(users);  // 全局List

            states = new ClickState[userList.size()];
            for (int i = 0; i < states.length; i++) {
                states[i] = ClickState.DEFAULT;
            }

            Log.d("google-my:", "onPostExecute: ");
            Log.d("google-my:", "onPostExecute: ");
            Log.d("google-my:", "onPostExecute: ");
            Log.d("google-my:", "onPostExecute: ");
            Log.d("google-my:", "onPostExecute: users--------------------> " + users); // 解析也成功


            MyBaseAdapter adapter = new MyBaseAdapter(MainActivity.this, userList,states);
            lv.setAdapter(adapter); // 加载数据

            myAdapter = adapter;

            // 不能在滑动监听里面设置位置，只能在外面设置。
            lv.setSelectionFromTop(firstVisiblePosition,top);

            // 那滑动完，又加载完数据，就设置点击item的监听吧
            // 可以在这里设置点击事件
            // 因为一开始进来就可以点击，而不是一开始进来要滑倒底部才可以点击，注意这两种逻辑的差别
            // 点击事件是 解析完数据并设置好适配器，就可以发生的。所以要在滑动事件之前就要进行处理
            // 一进来肯定是可以点击的，你可以选择滑不滑，万一有些item的数量不够的话，你根本没得滑嘛
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // 我在这个点击事件里面，就有一个position，来查看我点击的是第几个序号的item了。！！！

//                     如果点击了一个item，要做什么事情？
//                     里面要有上一个页面的content，还要有用户的评论（记得写上 楼数 ），评论用户的头像，昵称, 用户头像的获取要通过id
//                     每次一点击item，我就要携带当前这个item的信息，跳转到另一个界面，要求的参数有id，还有content，携带这两个信息跳转
                    User user = userList.get(position);
                    Intent intent = new Intent(MainActivity.this, DetailPageActivity.class);
                    Bundle extra = new Bundle();
                    extra.putString("id",user.getId());
                    Log.d("google-my:", "onItemClick: -------------> 传过去另一个界面的id究竟是哪个id？"+userList.get(position).getId());
                    extra.putString("content",user.getContent());
                    Log.d("google-my:", "onItemClick: -------------> 传过去另一个界面的content究竟是哪个content？"+userList.get(position).getContent());
                    extra.putString("icon",user.getIcon());
                    Log.d("google-my:", "onItemClick: ------------> 传过去另一个界面的icon是哪个icon："+ user.getIcon());
                   extra.putString("login",user.getLogin());
                    Log.d("google-my:", "onItemClick: ------------> 传过去另一个界面的用户名Login是哪个icon："+ user.getLogin());
//                    extra.putString("image",user.getImage());
//                    Log.d("google-my:", "onItemClick: ------------> 传过去另一个界面的用户名Image是哪个image："+ user.getImage());

//                    num = position;
//                    ib_good = ((ImageButton) lv.getChildAt(position).findViewById(R.id.ib_good));
//

                    intent.putExtras(extra);
                    startActivity(intent);



                }

            });

            //处理长按事件，长按是删除
            // 要remove从集合中，还要让适配器去更新控件显示的数据，用notifySetData()
//            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//                @Override
//                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//
//                    return true; // 表示item点击 事件处理完，就不再处理单击事件。
//                }
//            });



            // 这里是获取并加载好数据。
            // 我只能在这里设置，拉到最底部的时候可以进行刷新
//             各种记录当前位置及偏移量，各种新开一个http请求
                // 在这里再开一个 子线程，传入新的参数,用一个全局的list记录全部数据

            // 设置对lv滑动的监听，只要一滑动，就调用这个方法




            lv.setOnScrollListener(new AbsListView.OnScrollListener() {


                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    // 这个才是只要滑动了，就会调用这个方法
                    if(firstVisibleItem+visibleItemCount == totalItemCount)
                    {
//                        // 只要到达最后一栏，我就先加载底部刷新视图
//                        View footer = null;
//                        if(footer == null) // 底部视图第一次加载
//                        {
//                            footer = LayoutInflater.from(MainActivity.this).inflate(R.layout.footer_item, null);
//                            lv.addFooterView(footer); // 一旦到达底部，就给lv添加一个底部视图
//                            // 同时，加载更多数据,开启网络请求
//                        }

                        //那我就不加底部视图了,直接拉到最后就自动请求数据
                        // 先记录，再加载
                        firstVisiblePosition = lv.getFirstVisiblePosition(); // 记录第一个可见的item的位置
                        top = lv.getChildAt(0).getTop();  // 记录偏移量

                        new MyTask().execute(String.format(httpUrl,++pageCount)); // 成功了，要开始设置位置了！
                        // 只要到达底部，我就记录当前的位置，以及偏移量。然后再设置用setSrollStateChanged方法，来设置ListView的滚动位置

                    }else  // else就是已经请求到数据了嘛
                    {
                        // 不能在滑动监听里面设置位置
                    }

                }
            });



        }
    }
}
