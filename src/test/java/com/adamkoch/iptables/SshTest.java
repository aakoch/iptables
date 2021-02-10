package com.adamkoch.iptables;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import org.junit.jupiter.api.Test;

public class SshTest {

  public void test(final List<String> commandList) {

    final Properties properties = new Properties();

    try {
      properties.load(new FileReader("secrets.properties"));
    }
    catch (IOException e) {
      e.printStackTrace();
    }

    try {
      JSch jsch = new JSch();

      jsch.addIdentity(new File(properties.getProperty("keyfile.path")).getAbsolutePath());

      String user = properties.getProperty("ssh.user");
      String host = properties.getProperty("ssh.host");

      Session session = jsch.getSession(user, host, 22);

      // username and passphrase will be given via UserInfo interface.
      UserInfo ui = new UserInfo() {
        @Override
        public String getPassphrase() {

          return properties.getProperty("passphrase");
        }

        @Override
        public String getPassword() {
          return null;
        }

        @Override
        public boolean promptPassword(final String s) {
          return false;
        }

        @Override
        public boolean promptPassphrase(final String s) {
          return true;
        }

        @Override
        public boolean promptYesNo(final String s) {
          return true;
        }

        @Override
        public void showMessage(final String s) {

        }
      };

      session.setUserInfo(ui);
      session.connect();

//      Channel channel = session.openChannel("shell");
//
//      channel.setInputStream(System.in);
//      channel.setOutputStream(System.out);
//
//      channel.connect();




      for (String command : commandList) {

        Channel channel = session.openChannel("exec");

        ((ChannelExec) channel).setCommand(command);

        // X Forwarding
        // channel.setXForwarding(true);

        //channel.setInputStream(System.in);
        channel.setInputStream(null);

        //channel.setOutputStream(System.out);

        //FileOutputStream fos=new FileOutputStream("/tmp/stderr");
        //((ChannelExec)channel).setErrStream(fos);
        ((ChannelExec) channel).setErrStream(System.err);

        InputStream in = channel.getInputStream();
        channel.connect();

        byte[] tmp = new byte[1024];
        while (true) {
          while (in.available() > 0) {
            int i = in.read(tmp, 0, 1024);
            if (i < 0) {
              break;
            }
            System.out.print(new String(tmp, 0, i));
          }
          if (channel.isClosed()) {
            if (in.available() > 0) {
              continue;
            }
            System.out.println("exit-status: " + channel.getExitStatus());
            break;
          }
          try {
            Thread.sleep(1000);
          }
          catch (Exception ee) {
          }
        }
        channel.disconnect();
      }
      session.disconnect();


    }
    catch (Exception e) {
      System.out.println(e);
    }
  }


}