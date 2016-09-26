package qianfeng.qiubaiappapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2016/9/2 0002.
 */
public class MyBaseAdapter extends BaseAdapter{
    private Context context;
    private List<User> userList;
    private LayoutInflater inflater;
    private ClickState[] states;

    private int  sum1 = 0;
    private ViewHolder holder;


    public MyBaseAdapter(Context context, List<User> userList, ClickState[] states) {
        this.context = context;
        this.userList = userList;
        this.states = states;
        inflater = LayoutInflater.from(context);
    }

    public MyBaseAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
        inflater = LayoutInflater.from(context);
    }

    public MyBaseAdapter() {
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {





        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.listview_item,parent,false);
            holder = new ViewHolder();
            holder.iv_userface = (ImageView) convertView.findViewById(R.id.iv_userface);
            holder.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
            holder.iv_type = (ImageView) convertView.findViewById(R.id.iv_type);
            holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            holder.iv_content = (ImageView) convertView.findViewById(R.id.iv_content);
            holder.tv_count_fun = (TextView) convertView.findViewById(R.id.tv_count_fun);
            holder.tv_count_comments = (TextView) convertView.findViewById(R.id.tv_count_comments);
            holder.tv_count_share = (TextView) convertView.findViewById(R.id.tv_count_share);
            holder.ib_good = (ImageButton)convertView.findViewById(R.id.ib_good);
            holder.ib_nogood = (ImageButton)convertView.findViewById(R.id.ib_nogood);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        User user = userList.get(position);

        if(user.getFormat().equals("image")) // 现在是有content的图片
        {
            holder.iv_content.setVisibility(View.VISIBLE);
            // 截取后4位，直接/一万就好了,刚好少了4个0
            /*
              //内容图片获取(+图片名所有数字去掉后4位+"/"+图片名从数字开始数全部+"/"+"/"+small或者medium+"/"+图片名)
    //====图片Url=http://pic.qiushibaike.com/system/pictures/7128/71288069/small/app71288069.jpg
    public final static String URL_IMAGE= "http://pic.qiushibaike.com/system/pictures/%s/%s/%s/%s";
             */
            String s = user.getImage(); // 现在获取到图片的名字了。"app117424273.jpg",
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

            //再截取后四位
            int j = i/10000;
            String URL_IMAGE = "http://pic.qiushibaike.com/system/pictures/%s/%s/%s/%s";
            // 下载开启Piccsso网络请求
            String httpUrl = String.format(URL_IMAGE,j+"",i+"","small",s);
            Log.d("google-my:", "getView: 这是image——content，是内容的图片：" + httpUrl);
            Picasso.with(context).load(httpUrl).into(holder.iv_content);

        }else
        {
            // 否则就是没图片嘛
            holder.iv_content.setVisibility(View.GONE);
            // 有了GONE这个属性后，待会要记得调用  lv.setRequest();
        }

        // 拼接用户头像
        {
            /**/
            // 现在是要拿用户头像
            //icon
            String s = user.getId(); // 现在获取到用户id 用于获取用户头像。icon='201607271844408.JPEG
            String icon = user.getIcon();
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
            Picasso.with(context).load(httpUrl).into(holder.iv_userface);
            //头像获取(+ id所有数字去掉后4位 + "/" + id + "/thumb/" + icon图片名.jpg)
           /* //userIcon======http://pic.qiushibaike.com/system/avtnew/1499/14997026/thumb/20140404194843.jpg
            public final static String URL_USER_ICON="http://pic.qiushibaike.com/system/avtnew/%s/%s/thumb/%s";
            */

        }

        // 加载用户名
        {
            String login = user.getLogin();
            Log.d("google-my:", "getView:  用户名：" + login);
            holder.tv_username.setText(login);
        }

        // 加载内容
        {
            String content = user.getContent();
            Log.d("google-my:", "getView: 内容：" + content);
            holder.tv_content.setText(content);
        }

//        //加载觉得搞笑的次数
//        if(isClick.equals(ClickState.DEFAULT))
//        {
//            int i = Integer.parseInt(user.getUp());
//            int i1 = Integer.parseInt(user.getDown());
//            int sum = i + i1;
//            Log.d("google-my:", "getView: 觉得搞笑的次数：" + sum);
//            holder.tv_count_fun.setText("好笑 "+sum+"");
//        }else if(isClick.equals(ClickState.CLICK))
//        {
//            int i = Integer.parseInt(user.getUp());
//            int i1 = Integer.parseInt(user.getDown());
//            int sum = i + i1 + 1;
//            Log.d("google-my:", "getView: 觉得搞笑的次数：" + sum);
//            holder.tv_count_fun.setText("好笑 "+sum+"");
//        }else if(isClick.equals(ClickState.NOTCLICK))
//        {
//            int i = Integer.parseInt(user.getUp());
//            int i1 = Integer.parseInt(user.getDown());
//            int sum = i + i1 - 1;
//            Log.d("google-my:", "getView: 觉得搞笑的次数：" + sum);
//            holder.tv_count_fun.setText("好笑 "+sum+"");
//        }

        // 加载觉得搞笑的次数
        {

            // 下面是判断第一次点击的数据



            // 先遍历加载数组里面的状态数据，如果点击了，那就在数组里面存储点击了的数据
            // 先遍历数组

                if(states[position].equals(ClickState.DEFAULT))
                {
                    int j  = Integer.parseInt(user.getUp());
                   int i1 = Integer.parseInt(user.getDown());
                   int sum = j + i1;
                    sum1 = sum;
                   Log.d("google-my:", "getView: 觉得搞笑的次数：" + sum);
                   holder.tv_count_fun.setText("好笑 "+sum+"");

                }else if(states[position].equals(ClickState.CLICK))
                {
                    int j  = Integer.parseInt(user.getUp());
                    int i1 = Integer.parseInt(user.getDown());
                    int sum = j + i1 + 1;
                    sum1 = sum;
                    Log.d("google-my:", "getView: 觉得搞笑的次数：" + sum);
                    holder.tv_count_fun.setText("好笑 "+sum+"");
                }else if(states[position].equals(ClickState.NOTCLICK))
                {
                    int j  = Integer.parseInt(user.getUp());
                    int i1 = Integer.parseInt(user.getDown());
                    int sum = j + i1 - 1;
                    sum1 = sum;
                    Log.d("google-my:", "getView: 觉得搞笑的次数：" + sum);
                    holder.tv_count_fun.setText("好笑 "+sum+"");
                }

            holder.ib_good.setOnClickListener(new View.OnClickListener() {
                boolean isClick = true;
                @Override
                public void onClick(View v) {

                    if(states[position].equals(ClickState.DEFAULT)) {
                        states[position] = ClickState.CLICK;
                        holder.tv_count_fun.setText("好笑 " + sum1 + "");
                        notifyDataSetChanged();
                        Log.d("google-my:", "onClick: ---------------------》" + "ib_good这个方法成功执行");
                    }else if(states[position].equals(ClickState.CLICK))
                    {
                        states[position] = ClickState.CLICK;
                        Log.d("google-my:", "onClick: ---------------------》222" + "ib_good这个方法有执行吗？");
                    }else if(states[position].equals(ClickState.NOTCLICK))
                    {
                        states[position] = ClickState.NOTCLICK;
                    }
                }
            });

            holder.ib_nogood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(states[position].equals(ClickState.DEFAULT)) {
                        states[position] = ClickState.NOTCLICK;
                        holder.tv_count_fun.setText("好笑 " + sum1 + "");
                        notifyDataSetChanged();
                        Log.d("google-my:", "onClick: ----------->" + "nogood --->成功执行");
                        Log.d("google-my:", "onClick: ---------------------》111111" + "ib_nogood这个方法有执行吗？");
                    }else if(states[position].equals(ClickState.CLICK))
                    {
                        states[position] = ClickState.CLICK;
                        Log.d("google-my:", "onClick: ---------------------》222" + "ib_nogood这个方法有执行吗？");
                    }else if(states[position].equals(ClickState.NOTCLICK))
                    {
                        states[position] = ClickState.NOTCLICK;
                    }


                }
            });


            }



        // 加载评论数
        {
            String comments_count = user.getComments_count();
            Log.d("google-my:", "getView: 评论数：" + comments_count);
            holder.tv_count_comments.setText("评论 "+comments_count);
        }

        //加载分享数
        {
            String share_count = user.getShare_count();
            Log.d("google-my:", "getView: 分享数:"+share_count);
            holder.tv_count_share.setText("分享 "+share_count);
        }

        // 判断是否热门，type = hot|fresh
        {
            if(user.getType()!=null)
            {
                if(user.getType().equals("fresh"))
                {
                    holder.tv_type.setText("新鲜");
                    holder.iv_type.setImageResource(R.drawable.a5);
                }else if(user.getType().equals("hot"))
                {
                    holder.tv_type.setText("热门");
                    holder.iv_type.setImageResource(R.drawable.a5);
                }
            }else
            {
                    // 否则就不设置了
                    holder.tv_type.setText("默认");
                    holder.iv_type.setImageResource(R.mipmap.ic_launcher);
            }


        }





        return convertView;
    }

    class ViewHolder
    {
        ImageView iv_userface;// 用户头像icon
        TextView tv_username; // 用户名login

        ImageView iv_type; // 火热程度 type
        TextView tv_type;
        TextView tv_content;
        ImageView iv_content;
        TextView tv_count_fun;
        TextView tv_count_comments;
        TextView tv_count_share;

        ImageButton ib_good;
        ImageButton ib_nogood;

    }
}
