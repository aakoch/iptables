package com.adamkoch.iptables

import com.jcraft.jsch.JSch
import com.jcraft.jsch.UIKeyboardInteractive
import com.jcraft.jsch.UserInfo
import javax.swing.JOptionPane

/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */ /**
 * This program enables you to connect to sshd server and get the shell prompt.
 * $ CLASSPATH=.:../build javac Shell.java
 * $ CLASSPATH=.:../build java Shell
 * You will be asked username, hostname and passwd.
 * If everything works fine, you will get the shell prompt. Output may
 * be ugly because of lacks of terminal-emulation, but you can issue commands.
 *
 */
object Shell {
    @JvmStatic
    fun main(arg: Array<String>) {
        try {
            val jsch = JSch()

            //jsch.setKnownHosts("/home/foo/.ssh/known_hosts");
            var host: String? = null
            host = if (arg.size > 0) {
                arg[0]
            } else {
                JOptionPane.showInputDialog(
                    "Enter username@hostname",
                    System.getProperty("user.name") +
                            "@localhost"
                )
            }
            val user = host!!.substring(0, host.indexOf('@'))
            host = host.substring(host.indexOf('@') + 1)
            val session = jsch.getSession(user, host, 22)
            val passwd = JOptionPane.showInputDialog("Enter password")
            session.setPassword(passwd)
            val ui: UserInfo = object : MyUserInfo() {
                override fun showMessage(message: String) {
                    JOptionPane.showMessageDialog(null, message)
                }

                override fun promptYesNo(message: String): Boolean {
                    val options = arrayOf<Any>("yes", "no")
                    val foo = JOptionPane.showOptionDialog(
                        null,
                        message,
                        "Warning",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null, options, options[0]
                    )
                    return foo == 0
                } // If password is not given before the invocation of Session#connect(),
                // implement also following methods,
                //   * UserInfo#getPassword(),
                //   * UserInfo#promptPassword(String message) and
                //   * UIKeyboardInteractive#promptKeyboardInteractive()
            }
            session.userInfo = ui

            // It must not be recommended, but if you want to skip host-key check,
            // invoke following,
            // session.setConfig("StrictHostKeyChecking", "no");

            //session.connect();
            session.connect(30000) // making a connection with timeout.
            val channel = session.openChannel("shell")

            // Enable agent-forwarding.
            //((ChannelShell)channel).setAgentForwarding(true);
            channel.inputStream = System.`in`
            /*
      // a hack for MS-DOS prompt on Windows.
      channel.setInputStream(new FilterInputStream(System.in){
          public int read(byte[] b, int off, int len)throws IOException{
            return in.read(b, off, (len>1024?1024:len));
          }
        });
       */channel.outputStream = System.out

            /*
      // Choose the pty-type "vt102".
      ((ChannelShell)channel).setPtyType("vt102");
      */

            /*
      // Set environment variable "LANG" as "ja_JP.eucJP".
      ((ChannelShell)channel).setEnv("LANG", "ja_JP.eucJP");
      */

            //channel.connect();
            channel.connect(3 * 1000)
        } catch (e: Exception) {
            println(e)
        }
    }

    abstract class MyUserInfo : UserInfo, UIKeyboardInteractive {
        override fun getPassword(): String {
            return ""
        }

        override fun promptYesNo(str: String): Boolean {
            return false
        }

        override fun getPassphrase(): String {
            return ""
        }

        override fun promptPassphrase(message: String): Boolean {
            return false
        }

        override fun promptPassword(message: String): Boolean {
            return false
        }

        override fun showMessage(message: String) {}
        override fun promptKeyboardInteractive(
            destination: String,
            name: String,
            instruction: String,
            prompt: Array<String>,
            echo: BooleanArray
        ): Array<String> {
            return emptyArray()
        }
    }
}