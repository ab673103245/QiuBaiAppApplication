package qianfeng.qiubaiappapplication;

/**
 * Created by Administrator on 2016/9/3 0003.
 */
public class DetailComments {

    private String content;

    private String login;
    private String id;
    private String icon;


    public DetailComments(String content, String login, String id, String icon) {
        this.content = content;
        this.login = login;
        this.id = id;
        this.icon = icon;
    }

    public DetailComments() {
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    @Override
    public String toString() {
        return "用户----------------------------->评论:{" +
                "content='" + content + '\'' +
                ", login='" + login + '\'' +
                ", id='" + id + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
