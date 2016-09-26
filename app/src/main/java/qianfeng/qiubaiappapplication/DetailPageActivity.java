package qianfeng.qiubaiappapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DetailPageActivity extends AppCompatActivity {

    private String detailurl = "http://m2.qiushibaike.com/article/%s/comments?page=1&count=50&rqcnt=19&r=d0dc8ad41456830331669";

    private ListView lv_detail;

    private ImageView iv_detailUserface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);
        lv_detail = (ListView) findViewById(R.id.lv_detail);
        TextView tv_detailContent = (TextView) findViewById(R.id.tv_detailContent);
        TextView tv_detailUsername = (TextView) findViewById(R.id.tv_detailUsername);
        ImageView iv_detailContentImage = (ImageView) findViewById(R.id.iv_detailContentImage);
        iv_detailUserface = (ImageView) findViewById(R.id.iv_detailUserface);


        // 获取从另一个界面传过来的东西
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String id = extras.getString("id");
        String content = extras.getString("content");
        String icon = extras.getString("icon"); // 有了id和icon，我们就可以下载 楼主的头像图片了
        String login = extras.getString("login");


        // 拼接用户头像网址
        String httpUrl1 = getHttpUrl(id, icon);
        new downloadImage().execute(httpUrl1); // 启动子线程，下载楼主的用户头像


        tv_detailContent.setText(content); // 设置从另一个界面传过来的内容content,这里直接写，不用下载
        tv_detailUsername.setText(login);  // 这里设置从另一个界面传过来的用户名

        String httpUrl = String.format(detailurl,"115333399");  // 获取要解析的JSON的网址


        Log.d("google-my:", "getView: 截取后的网址------------->是什么？正确吗？" + httpUrl);

//        new MyAsyncTask().execute(httpUrl); // 启动子线程，下载要解析的JOSN，里面包含详情，就是评论

//        String url = "http://m2.qiushibaike.com/article/115333399/comments?page=1&count=50&rqcnt=19&r=d0dc8ad41456830331669";
//        new MyAsyncTask().execute(url);

        new MyAsyncTask().execute(httpUrl);
    }

    private String getHttpUrl(String id, String icon1)
    {

            /**/
            // 现在是要拿用户头像
            //icon
            String s = id; // 现在获取到用户id 用于获取用户头像。icon='201607271844408.JPEG
            String icon =icon1;
            Log.d("google-my:", "getView: 图片名字:" + icon);
            Log.d("google-my:", "getView:  id 截取到的名字：" + s);
            char[] chars = s.toCharArray();
            String st = "";
            for (int i = 0; i < chars.length; i++) {
                if(chars[i]>47 && chars[i] < 58)
                {
                    st += (char)chars[i];
                }
            }
            // 返回的就是纯数字
            int i = Integer.parseInt(st);
            Log.d("google-my:", "getView:  转换为int型之后是什么" + i);
            //再截取后四位
            int j = i/10000;
            Log.d("google-my:", "getView:  转换为int型去除后4位是什么" + j);
            String URL_USER_ICON="http://pic.qiushibaike.com/system/avtnew/%s/%s/thumb/%s";
            // 下载开启Piccsso网络请求
            String httpUrl = String.format(URL_USER_ICON,j+"",i+"",icon);
            Log.d("google-my:", "getView: 截取后的网址是什么？正确吗？" + httpUrl);

        return httpUrl;
    }

    private class downloadImage extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            // 这里就是很简单的下载Bitmap就可以了
            HttpURLConnection con = null;
            InputStream is = null;
            try {
                URL url = new URL(params[0]);
                 con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(5*1000);
                if(con.getResponseCode() == 200)
                {
                    is = con.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    return  bitmap;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            iv_detailUserface.setImageBitmap(bitmap); // 下载完之后，就在这里设置图片就好了

        }
    }


    private class MyAsyncTask extends AsyncTask<String, Void, List<DetailComments>> {
        @Override
        protected List<DetailComments> doInBackground(String... params) {
            // 进行Http请求

            byte[] http = HttpUtils.getHttp(params[0]);
            String s = new String(http, 0, http.length);
            List<DetailComments> detailCommentses = parseJSON(s);



            return detailCommentses;
        }

        private List<DetailComments> parseJSON(String s)
        {
            List<DetailComments> detail_list = new ArrayList<>();

            // 开始JSON解析把
            try {
                JSONObject object = new JSONObject(s);
                JSONArray items = object.getJSONArray("items");
                for (int i = 0; i < items.length(); i++) {
                    JSONObject js = items.getJSONObject(i);
                    String content = js.getString("content");

                    JSONObject user = js.getJSONObject("user");
                    String login = user.getString("login");
                    String id = user.getString("id");
                    String icon = user.getString("icon");

                    detail_list.add(new DetailComments(content,login,id,icon));

                }
                return detail_list;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return detail_list;
        }


        @Override
        protected void onPostExecute(List<DetailComments> detailCommentses) {

            Log.d("google-my:", "onPostExecute: " + detailCommentses); // 解析没问题

            // 下面开始适配数据吧
            lv_detail.setAdapter(new MyDetailAdapter(DetailPageActivity.this,detailCommentses));


            super.onPostExecute(detailCommentses);
        }
    }





}
