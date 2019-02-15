package com.chuan.mail;

import java.net.*;
import java.util.concurrent.*;

/**
 * author:jc
 * Date:2019/2/14
 * Time:14:30
 * ���Դ����Ƿ�ͨ�Ĵ���
 */
public class Test {


    public static final String PRXOY_HOST = "116.62.131.137";
    public static final String PROXY_PORT = "1080";
    public static final String TEST_ADDRESS = "http://www.google.com.tw";
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

    /**
     * @param args
     */
    public static void main(String[] args) {
        // �û���֤
        if (args.length == 2) {
            Authenticator.setDefault(new LoginAuthenticator(args[0], args[1]));
        }

        Callable<Boolean> oCallback = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                HttpURLConnection oConn = null;
                try {
                    Proxy oProxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(PRXOY_HOST, Integer.parseInt(PROXY_PORT)));
                    URL url = new URL("http://www.aastocks.com");
                    oConn = (HttpURLConnection) url.openConnection(oProxy);
                    oConn.setConnectTimeout(5000);
                    oConn.connect();
                } catch (Exception e) {
                    e.printStackTrace();
                    // ������쳣֤�����������ò�����
                    return false;
                } finally {
                    if (oConn != null) {
                        oConn.disconnect();
                    }
                }
                return true;
            }
        };

        FutureTask<Boolean> oTask = new FutureTask<Boolean>(oCallback) {
            @Override
            protected void done() {
                try {
                    Boolean bCheckSuccess = (Boolean)this.get();
                    if (bCheckSuccess) {
                        // ������Գɹ�
                        System.err.println("����ɹ�+++++");
                        // ���Գɹ�����ʹ��"ȫ���������"
                        System.setProperty("proxySet", "true");
                        System.setProperty("http.proxyHost", PRXOY_HOST);
                        System.setProperty("http.proxyPort", PROXY_PORT);
                    } else {
                        //��������ʧ��
                        System.err.println("�޷����ӵ����磬�����������!!");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };
        EXECUTOR.submit(oTask);
        EXECUTOR.shutdown();
    }

    static class LoginAuthenticator extends Authenticator {
        /** User name **/
        private String m_sUser;
        /** Password **/
        private String m_sPsw;

        public LoginAuthenticator(String sUser, String sPsw) {
            m_sUser = sUser;
            m_sPsw = sPsw;
        }

        public PasswordAuthentication getPasswordAuthentication() {
            return (new PasswordAuthentication(m_sUser, m_sPsw.toCharArray()));
        }
    }

}
