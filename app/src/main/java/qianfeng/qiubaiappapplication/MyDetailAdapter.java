package qianfeng.qiubaiappapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2016/9/3 0003.
 */
public class MyDetailAdapter extends BaseAdapter{

    private Context context;
    private List<DetailComments> detailCommentsList;
    private LayoutInflater inflater;

    public MyDetailAdapter(Context context, List<DetailComments> detailCommentsList) {
        this.context = context;
        this.detailCommentsList = detailCommentsList;
        inflater = LayoutInflater.from(context);
    }

    public MyDetailAdapter() {
    }



    @Override
    public int getCount() {
        return detailCommentsList.size();
    }

    @Override
    public Object getItem(int position) {
        return detailCommentsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.detail_item,parent,false);
            holder = new ViewHolder();
            holder.tv_floorCount = (TextView) convertView.findViewById(R.id.tv_floorCount);
            holder.iv_detailLogin = (ImageView) convertView.findViewById(R.id.iv_detailLogin);
            holder.tv_detailLogin = (TextView) convertView.findViewById(R.id.tv_detailLogin);
            holder.tv_detailComments = (TextView) convertView.findViewById(R.id.tv_detailComments);
            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        DetailComments detailComments = detailCommentsList.get(position);
        // 记住这个严重异常
        holder.tv_floorCount.setText(++position+""); // 用户楼数
        holder.tv_detailLogin.setText(detailComments.getLogin());  // 用户名
        holder.tv_detailComments.setText(detailComments.getContent()); // 用户评论

        // 图片的获取，待会要用icon,id , 这里的id，应该是该位评论用户的id
        /*
         //头像获取(+ id所有数字去掉后4位 + "/" + id + "/thumb/" + icon图片名.jpg)
    //userIcon======http://pic.qiushibaike.com/system/avtnew/1499/14997026/thumb/20140404194843.jpg
    public final static String URL_USER_ICON="http://pic.qiushibaike.com/system/avtnew/%s/%s/thumb/%s";
         */
        {
            String s = detailComments.getId();
            String icon = detailComments.getIcon();
            Log.d("google-my:", "getView: 2图片名字:" + icon);
            Log.d("google-my:", "getView:  2id 截取到的名字：" + s);
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
            Log.d("google-my:", "getView:  2转换为int型之后是什么" + i);
            //再截取后四位
            int j = i/10000;
            Log.d("google-my:", "getView:  2转换为int型去除后4位是什么" + j);
            String URL_USER_ICON="http://pic.qiushibaike.com/system/avtnew/%s/%s/thumb/%s";
            // 下载开启Piccsso网络请求
            String httpUrl = String.format(URL_USER_ICON,j+"",i+"",icon);
            Log.d("google-my:", "getView: 2截取后的网址是什么？正确吗？" + httpUrl);
            Picasso.with(context).load(httpUrl).into(holder.iv_detailLogin);

          /*  //头像获取(+ id所有数字去掉后4位 + "/" + id + "/thumb/" + icon图片名.jpg)
            //userIcon======http://pic.qiushibaike.com/system/avtnew/1499/14997026/thumb/20140404194843.jpg
            public final static String URL_USER_ICON="http://pic.qiushibaike.com/system/avtnew/%s/%s/thumb/%s";
            */
        }

        return convertView;
    }

    class ViewHolder
    {
        TextView tv_floorCount; // 楼数
        ImageView iv_detailLogin; // 评论的用户头像
        TextView tv_detailLogin; // 评论的用户的名字
        TextView tv_detailComments; // 该用户的评论
    }


}
