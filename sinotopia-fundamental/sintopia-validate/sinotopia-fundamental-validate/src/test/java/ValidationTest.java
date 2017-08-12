import com.hkfs.fundamental.validate.annotaion.validation.*;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class ValidationTest {
    
    private static class Client {
        
        @NotBlank
        private String clientId;

        @Chinese(message = "请输入中文字符啊啊啊啊 啊 啊 啊啊啊 啊啊")
        private String chinese;

        @IdentityNo
        private String identityNo;

        @Mobile
        private String mobile;

        @Email
        private String email;

        @Telephone
        private String telephone;

        @NegativeNum
        private String negativeNum;

        @PositiveNum
        private String positiveNum;

        @Num
        private String num;

        @DateValue
        private String date;

        @QQ
        private String qq;

        @IP
        private String ip;

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getChinese() {
            return chinese;
        }

        public void setChinese(String chinese) {
            this.chinese = chinese;
        }

        public String getIdentityNo() {
            return identityNo;
        }

        public void setIdentityNo(String identityNo) {
            this.identityNo = identityNo;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getNegativeNum() {
            return negativeNum;
        }

        public void setNegativeNum(String negativeNum) {
            this.negativeNum = negativeNum;
        }

        public String getPositiveNum() {
            return positiveNum;
        }

        public void setPositiveNum(String positiveNum) {
            this.positiveNum = positiveNum;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }
    }
    
    public static void main(String[] args) {
        ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = vFactory.getValidator();
        
        Client client = new Client();
        client.setChinese("???");
        client.setIdentityNo("429");
        client.setMobile("18737297441");
        client.setEmail("ddd@166.com");
        client.setPositiveNum("123456");
        client.setNegativeNum("-123456");
        client.setNum("xxx");
        client.setTelephone("xxxx");
        client.setDate("2016-06-01");
        client.setQq("fsdfesf");
        client.setIp("www.baidu.com");
        Set<ConstraintViolation<Client>> v = validator.validate(client);

        for(ConstraintViolation<Client> cv : v){
            System.out.println("message:"+cv.getMessage()+"====="+cv.getMessageTemplate());
        }
        System.out.println(v.size());
        System.out.println(v);
    }
    
}
