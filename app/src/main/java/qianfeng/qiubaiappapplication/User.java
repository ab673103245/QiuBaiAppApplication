package qianfeng.qiubaiappapplication;

/**
 * Created by Administrator on 2016/9/2 0002.
 */
public class User {

    private String format;
    private String image;
    private String login; // login
    private String id;
    private String icon;
    private String down;
    private String up;
    private String content;
    private String comments_count;
    private String share_count;
    private String type;

    public User(String format, String image, String login, String id, String icon, String down, String up, String content, String comments_count, String share_count) {
        this.format = format;
        this.image = image;
        this.login = login;
        this.id = id;
        this.icon = icon;
        this.down = down;
        this.up = up;
        this.content = content;
        this.comments_count = comments_count;
        this.share_count = share_count;

    }


    public User(String format, String image, String login, String id, String icon, String down, String up, String content, String comments_count, String share_count, String type) {
        this.format = format;
        this.image = image;
        this.login = login;
        this.id = id;
        this.icon = icon;
        this.down = down;
        this.up = up;
        this.content = content;
        this.comments_count = comments_count;
        this.share_count = share_count;
        this.type = type;
    }

    public User() {
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getDown() {
        return down;
    }

    public void setDown(String down) {
        this.down = down;
    }

    public String getUp() {
        return up;
    }

    public void setUp(String up) {
        this.up = up;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getComments_count() {
        return comments_count;
    }

    public void setComments_count(String comments_count) {
        this.comments_count = comments_count;
    }

    public String getShare_count() {
        return share_count;
    }

    public void setShare_count(String share_count) {
        this.share_count = share_count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "用户{" +
                "format='" + format + '\'' +
                ", image='" + image + '\'' +
                ", login='" + login + '\'' +
                ", id='" + id + '\'' +
                ", icon='" + icon + '\'' +
                ", down='" + down + '\'' +
                ", up='" + up + '\'' +
                ", content='" + content + '\'' +
                ", comments_count='" + comments_count + '\'' +
                ", share_count='" + share_count + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
