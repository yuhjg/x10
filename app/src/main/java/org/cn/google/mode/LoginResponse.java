package org.cn.google.mode;


public class LoginResponse {

    private UserInfo userinfo;

    public UserInfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserInfo userinfo) {
        this.userinfo = userinfo;
    }

    public static class UserInfo {

        private int id;//":1,
        private String username;//":"1234",
        private String nickname;//":"1256",
        private String mobile;//":"15654212321",
        private String token;//":"eedc8421-ae25-4c09-b6c9-ef5694dcec30",
        private int user_id;//":1,
        private Long createtime;//":1616772724,
        private Long expiretime;//":1619364724,
        private Long expires_in;//":2592000

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public Long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(Long createtime) {
            this.createtime = createtime;
        }

        public Long getExpiretime() {
            return expiretime;
        }

        public void setExpiretime(Long expiretime) {
            this.expiretime = expiretime;
        }

        public Long getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(Long expires_in) {
            this.expires_in = expires_in;
        }
    }
}


