package com.chuan.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.*;

/**
 * author:jc
 * Date:2019/2/14
 * Time:15:57
 */
public class Mail {


    private final static String default_charset = "UTF-8";

    public static enum EncryptionTypes {
        Default, TLS, SSL
    }

    private static final Logger logger = LoggerFactory.getLogger(Mail.class);
    private String smtpPort = "25";
    private String sslSmtpPort = "465";
    private String hostName ;
    private int encryptionType = EncryptionTypes.Default.ordinal();
    private boolean auth = false;
    private String mailUserName;
    private String mailPassword;
    private String senderAddress;
    private String senderName;
    private String socksProxyHost = "116.62.131.137";
    private String socksProxyPort = "1080";
    private String subject;
    private Boolean socksProxySet = false;
    private boolean sslOnConnect;
    private boolean isHtml = true;
    private boolean debug = false;
    private int socketTimeout = 60000;
    private int socketConnectionTimeout = 60000;


    public void sendEmail(String receiverAddress, String msg) throws Exception {
        List recipients = getSplitResult(receiverAddress);
        this.sendEmail( recipients,  msg);
    }

    private void sendEmail(List recipients, String msg) throws SendFailedException {
        this.sendEmail( recipients,  msg, null);
    }

    public void sendEmail(String receiverAddress, String msg, Collection attachments) throws Exception {
        List recipients = getSplitResult(receiverAddress);
        this.sendEmail( recipients,  msg, attachments);
    }


    private void sendEmail(List recipients, String msg, Collection attachments) throws SendFailedException {
        Transport transport = null;
        try {
            Properties props = this.getProperties();
            Session session = this.getSession(props);
            MimeMessage message = new MimeMessage(session);
            if (this.getDefaultIsHtml()) {
                message.addHeader("Content-type", "text/html");
            } else {
                message.addHeader("Content-type", "text/plain");
            }
            message.setSubject(subject, default_charset);
            message.setFrom(new InternetAddress(senderAddress, senderName));
            for (Iterator it = recipients.iterator(); it.hasNext();) {
                String email = (String) it.next();
                message.addRecipients(Message.RecipientType.TO, email);
            }
            Multipart mp = new MimeMultipart();
            MimeBodyPart contentPart = new MimeBodyPart();
            if (this.getDefaultIsHtml()) {
                contentPart.setContent("<meta http-equiv=Content-Type content=text/html; charset=" + default_charset + ">" + msg, "text/html;charset=" + default_charset);
            } else {
                contentPart.setText(msg, default_charset);
            }
            mp.addBodyPart(contentPart);
            if (attachments != null) {
                MimeBodyPart attachPart;
                for (Iterator it = attachments.iterator(); it.hasNext();) {
                    attachPart = new MimeBodyPart();
                    FileDataSource fds = new FileDataSource(it.next().toString().trim());
                    attachPart.setDataHandler(new DataHandler(fds));
                    if (fds.getName().indexOf("$") != -1) {
                        attachPart.setFileName(fds.getName().substring(fds.getName().indexOf("$") + 1, fds.getName().length()));
                    } else {
                        attachPart.setFileName(fds.getName());
                    }
                    mp.addBodyPart(attachPart);
                }
            }
            message.setContent(mp);
            message.setSentDate(new Date());
            if (sslOnConnect) {
                Transport.send(message);
            } else {
                transport = session.getTransport("smtp");
                transport.connect(this.hostName, Integer.parseInt(getSmtpPort()), this.mailUserName, this.mailPassword);
                transport.sendMessage(message, message.getAllRecipients());
            }
        } catch (Exception e) {
            logger.error("send mail error", e);
            throw new SendFailedException(e.toString());
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (Exception ex) {
                }
            }
        }
    }



    private Properties getProperties() {

        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.port", this.smtpPort);
        properties.setProperty("mail.smtp.host", this.hostName);
        properties.setProperty("mail.debug", String.valueOf(this.debug));
        properties.put("mail.smtp.auth", String.valueOf(this.auth));

        if (this.socketTimeout > 0) {
            properties.setProperty("mail.smtp.timeout", Integer.toString(this.socketTimeout));
        }

        if (this.socketConnectionTimeout > 0) {
            properties.setProperty("mail.smtp.connectiontimeout", Integer.toString(this.socketConnectionTimeout));
        }
        //使用代理
        if(socksProxySet) {
            properties.setProperty("proxySet", "true");
            properties.setProperty("socksProxyHost", socksProxyHost);
            properties.setProperty("socksProxyPort", socksProxyPort);
        }
        //ssl
        if(sslOnConnect) {
            properties.setProperty("mail.smtp.port", this.sslSmtpPort);
            properties.setProperty("mail.smtp.socketFactory.port", this.sslSmtpPort);
            properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.setProperty("mail.smtp.socketFactory.fallback", "false");
            properties.setProperty("mail.smtp.ssl.checkserveridentity", "true");
        }

        return properties;
    }


    private Session getSession(Properties props) {
        logger.info("mailUserName {}, mailPassword {}",mailUserName,mailPassword);
        return Session.getInstance(props, new MyAuthenticator(this.mailUserName, this.mailPassword));
    }

    private boolean getDefaultIsHtml() {
        return this.isHtml;
    }

    private class MyAuthenticator extends Authenticator {
        String user;
        String password;

        public MyAuthenticator() {
        }

        public MyAuthenticator(String user, String password) {
            this.user = user;
            this.password = password;
        }

        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(this.user, this.password);
        }
    }


    private List getSplitResult(String receiverAddress) {
        if(receiverAddress == null || "".equals(receiverAddress)) {
            throw new IllegalArgumentException("receiverAddress is  null ");
        }
        String[] address = receiverAddress.split(";");
        List recipients = new ArrayList();
        for (int i = 0; i < address.length; i++) {
            if (address[i].trim().length() > 0) {
                recipients.add(address[i]);
            }
        }
        return recipients;
    }

    public static String getDefault_charset() {
        return default_charset;
    }

    public static Logger getLogger() {
        return logger;
    }

    public String getSmtpPort() {
        return sslOnConnect ? sslSmtpPort: smtpPort;
    }

    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

    public String getSslSmtpPort() {
        return sslSmtpPort;
    }

    public void setSslSmtpPort(String sslSmtpPort) {
        this.sslSmtpPort = sslSmtpPort;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }


    public int getEncryptionType() {
        return encryptionType;
    }

    public void setEncryptionType(int encryptionType) {
        this.encryptionType = encryptionType;
    }

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public void setMailUserName(String mailUserName) {
        this.mailUserName = mailUserName;
    }

    public void setMailPassword(String mailPassword) {
        this.mailPassword = mailPassword;
    }


    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }


    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }


    public void setSocksProxyHost(String socksProxyHost) {
        this.socksProxyHost = socksProxyHost;
    }


    public void setSocksProxyPort(String socksProxyPort) {
        this.socksProxyPort = socksProxyPort;
    }

    public void setSocksProxySet(Boolean socksProxySet) {
        this.socksProxySet = socksProxySet;
    }


    public void setSslOnConnect(boolean sslOnConnect) {
        this.sslOnConnect = sslOnConnect;
    }


    public void setHtml(boolean html) {
        isHtml = html;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public void setSocketConnectionTimeout(int socketConnectionTimeout) {
        this.socketConnectionTimeout = socketConnectionTimeout;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }



    public static void main(String[] args) throws Exception {
        Mail mail = new Mail();
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("<html><head></head><body>");
            sb.append("<p><span style=\"font-size:16px; color:#009900 ;font: bold;\">亲爱的用户：test</span></p>");
            sb.append("<p>点击以下链接完成注册哈哈哈  ：</p>");
            sb.append("<p></p>");
            sb.append("<p>如果你的邮箱不支持链接点击，请将以上链接地址拷贝到你的浏览器地址栏中。</p>");
            sb.append("<p>此邮件由系统自动产生，请勿回复。(该链接仅在7日内点击有效，如果超过7日未点击确认，请你登录菩提蛮，并申请重发激活邮件)。</p>");
            sb.append("</body></html>");
            //116.62.131.137  172.16.75.78
            mail.setHostName("smtp.163.com");
            //开启代理
            mail.setSocksProxySet(true);
            mail.setSslOnConnect(true);
            mail.setMailUserName("jichuandada@163.com");
            mail.setMailPassword("zhu18848977541");
            mail.setSenderAddress("jichuandada@163.com");
            mail.setSubject("标题");
            mail.setSenderName("串串");
            mail.setAuth(true);
            mail.setDebug(true);
            mail.sendEmail("zhujichuan@coseast.com", sb.toString());
            System.out.println("发送成功！");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("发送失败！");
        }
    }

}
