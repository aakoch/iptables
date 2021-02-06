/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
package com.adamkoch.iptables

import com.jcraft.jsch.JSch
import com.jcraft.jsch.UIKeyboardInteractive
import com.jcraft.jsch.UserInfo
import java.awt.Container
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.*

/**
 * This program will demonstrate the keyboard-interactive authentication.
 * $ CLASSPATH=.:../build javac UserAuthKI.java
 * $ CLASSPATH=.:../build java UserAuthKI
 * If the remote sshd supports keyboard-interactive authentication,
 * you will be prompted.
 *
 */
object UserAuthKI {
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
                            "@192.168.50.1"
                )
            }
            val user = host!!.substring(0, host.indexOf('@'))
            host = host.substring(host.indexOf('@') + 1)
            val session = jsch.getSession(user, host, 22)

            // username and passphrase will be given via UserInfo interface.
            val ui: UserInfo = MyUserInfo()
            session.userInfo = ui
            session.connect()
            val channel = session.openChannel("shell")
            channel.inputStream = System.`in`
            channel.outputStream = System.out
            channel.connect()
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
        override fun getPassphrase(): String {
            return ""
        }

        override fun promptPassphrase(message: String): Boolean {
            return false
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
//System.out.println("promptKeyboardInteractive");
            println("destination: $destination")
            println("name: $name")
            println("instruction: $instruction")
            println("prompt.length: " + prompt.size)
            println("prompt: " + prompt[0])
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