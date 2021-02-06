package com.adamkoch.iptables

import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.JSch
import com.jcraft.jsch.UIKeyboardInteractive
import com.jcraft.jsch.UserInfo
import java.awt.Container
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.*

/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */ /**
 * This program will demonstrate remote exec.
 * $ CLASSPATH=.:../build javac Exec.java
 * $ CLASSPATH=.:../build java Exec
 * You will be asked username, hostname, displayname, passwd and command.
 * If everything works fine, given command will be invoked
 * on the remote side and outputs will be printed out.
 *
 */
object Exec {
    @JvmStatic
    fun main(arg: Array<String>) {
        try {
            val jsch = JSch()
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

            /*
      String xhost="127.0.0.1";
      int xport=0;
      String display=JOptionPane.showInputDialog("Enter display name", 
                                                 xhost+":"+xport);
      xhost=display.substring(0, display.indexOf(':'));
      xport=Integer.parseInt(display.substring(display.indexOf(':')+1));
      session.setX11Host(xhost);
      session.setX11Port(xport+6000);
      */

            // username and password will be given via UserInfo interface.
            val ui: UserInfo = MyUserInfo()
            session.userInfo = ui
            session.connect()
            val command = JOptionPane.showInputDialog(
                "Enter command",
                "set|grep SSH"
            )
            val channel = session.openChannel("exec")
            (channel as ChannelExec).setCommand(command)

            // X Forwarding
            // channel.setXForwarding(true);

            //channel.setInputStream(System.in);
            channel.setInputStream(null)

            //channel.setOutputStream(System.out);

            //FileOutputStream fos=new FileOutputStream("/tmp/stderr");
            //((ChannelExec)channel).setErrStream(fos);
            channel.setErrStream(System.err)
            val `in` = channel.getInputStream()
            channel.connect()
            val tmp = ByteArray(1024)
            while (true) {
                while (`in`.available() > 0) {
                    val i = `in`.read(tmp, 0, 1024)
                    if (i < 0) break
                    print(String(tmp, 0, i))
                }
                if (channel.isClosed()) {
                    if (`in`.available() > 0) continue
                    println("exit-status: " + channel.getExitStatus())
                    break
                }
                try {
                    Thread.sleep(1000)
                } catch (ee: Exception) {
                }
            }
            channel.disconnect()
            session.disconnect()
        } catch (e: Exception) {
            println(e)
        }
    }

    class MyUserInfo : UserInfo, UIKeyboardInteractive {
        override fun getPassword(): String {
            return passwd!!
        }

        override fun promptYesNo(str: String): Boolean {
            val options = arrayOf<Any>("yes", "no")
            val foo = JOptionPane.showOptionDialog(
                null,
                str,
                "Warning",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null, options, options[0]
            )
            return foo == 0
        }

        var passwd: String? = null
        var passwordField = JPasswordField(20) as JTextField
        override fun getPassphrase(): String? {
            return null
        }

        override fun promptPassphrase(message: String): Boolean {
            return true
        }

        override fun promptPassword(message: String): Boolean {
            val ob = arrayOf<Any>(passwordField)
            val result = JOptionPane.showConfirmDialog(
                null, ob, message,
                JOptionPane.OK_CANCEL_OPTION
            )
            return if (result == JOptionPane.OK_OPTION) {
                passwd = passwordField.text
                true
            } else {
                false
            }
        }

        override fun showMessage(message: String) {
            JOptionPane.showMessageDialog(null, message)
        }

        val gbc = GridBagConstraints(
            0, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.NORTHWEST,
            GridBagConstraints.NONE,
            Insets(0, 0, 0, 0), 0, 0
        )
        private var panel = JPanel()
        override fun promptKeyboardInteractive(
            destination: String,
            name: String,
            instruction: String,
            prompt: Array<String>,
            echo: BooleanArray
        ): Array<String?> {
            panel.setLayout(GridBagLayout())
            gbc.weightx = 1.0
            gbc.gridwidth = GridBagConstraints.REMAINDER
            gbc.gridx = 0
            panel.add(JLabel(instruction), gbc)
            gbc.gridy++
            gbc.gridwidth = GridBagConstraints.RELATIVE
            val texts = arrayOfNulls<JTextField>(prompt.size)
            for (i in prompt.indices) {
                gbc.fill = GridBagConstraints.NONE
                gbc.gridx = 0
                gbc.weightx = 1.0
                panel.add(JLabel(prompt[i]), gbc)
                gbc.gridx = 1
                gbc.fill = GridBagConstraints.HORIZONTAL
                gbc.weighty = 1.0
                if (echo[i]) {
                    texts[i] = JTextField(20)
                } else {
                    texts[i] = JPasswordField(20)
                }
                panel.add(texts[i], gbc)
                gbc.gridy++
            }
            return if (JOptionPane.showConfirmDialog(
                    null, panel,
                    "$destination: $name",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                )
                == JOptionPane.OK_OPTION
            ) {
                val response = arrayOfNulls<String>(prompt.size)
                for (i in prompt.indices) {
                    response[i] = texts[i]!!.text
                }
                response
            } else emptyArray()
        }
    }
}